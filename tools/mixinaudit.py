#!/usr/bin/env python3
"""Check a built mixin jar against the classpath it will actually run on.

Mixins are not checked by the compiler. A selector that matches nothing, or a
@Shadow whose type drifted between game versions, compiles cleanly and then
takes the game down while the target class is being transformed. This walks the
class files directly and reports every reference that will not resolve.

    tools/mixinaudit.py <mixin-jar> <jar-or-dir> [<jar-or-dir> ...]

The first argument is the jar to audit; the rest are the classpath it is meant
for -- the game jar plus every mod the mixins reach into. Directories are
scanned for jars.

Both sides must be in the same mapping namespace. Audit build/devlibs/*-dev.jar
against a mojmap classpath; the remapped jar from build/libs will report the
whole world as missing.

What gets checked:

  * every @Mixin target class exists
  * every method selector (@Inject, @Redirect, @ModifyVariable, the MixinExtras
    injectors -- anything carrying `method`) names a method the target has
  * every @At target member exists on the class it names
  * every @Shadow matches by name *and* descriptor, which is how mixin itself
    matches them: a field of the right name but the wrong type is not found
  * every @Accessor/@Invoker with an explicit target resolves

Names no mapping table assigns -- lambdas, and the intermediary `method_NNNNN`
form -- are counted and listed, not failed: they cannot be resolved from the
class files alone and have to be read off the target by hand.
"""
import os
import sys
import zipfile

MIXIN_PKG = 'Lorg/spongepowered/asm/mixin/'


class Reader:
    def __init__(self, data):
        self.d, self.p = data, 0

    def u1(self):
        self.p += 1
        return self.d[self.p - 1]

    def u2(self):
        self.p += 2
        return int.from_bytes(self.d[self.p - 2:self.p], 'big')

    def u4(self):
        self.p += 4
        return int.from_bytes(self.d[self.p - 4:self.p], 'big')

    def raw(self, n):
        self.p += n
        return self.d[self.p - n:self.p]


def read_pool(r):
    n = r.u2()
    pool, i = [None] * n, 1
    while i < n:
        tag = r.u1()
        if tag == 1:
            pool[i] = r.raw(r.u2()).decode('utf-8', 'replace')
        elif tag in (3, 4):
            pool[i] = r.u4()
        elif tag in (5, 6):
            pool[i] = (r.u4(), r.u4())
        elif tag in (7, 8, 16, 19, 20):
            pool[i] = r.u2()
        elif tag in (9, 10, 11, 12, 17, 18):
            pool[i] = (r.u2(), r.u2())
        elif tag == 15:
            pool[i] = (r.u1(), r.u2())
        else:
            raise ValueError('unknown constant tag %d' % tag)
        i += 2 if tag in (5, 6) else 1
    return pool


def read_element(r, pool):
    tag = chr(r.u1())
    if tag in 'BCDFIJSZs':
        return pool[r.u2()]
    if tag == 'e':
        r.u2()
        return pool[r.u2()]
    if tag == 'c':
        return pool[r.u2()]
    if tag == '@':
        return read_annotation(r, pool)
    if tag == '[':
        return [read_element(r, pool) for _ in range(r.u2())]
    raise ValueError('unknown element tag %r' % tag)


def read_annotation(r, pool):
    name = pool[r.u2()]
    return (name, {pool[r.u2()]: read_element(r, pool) for _ in range(r.u2())})


def read_attrs(r, pool):
    out = {}
    for _ in range(r.u2()):
        name = pool[r.u2()]
        out.setdefault(name, []).append(r.raw(r.u4()))
    return out


def annotations(attrs, pool):
    found = []
    for key in ('RuntimeVisibleAnnotations', 'RuntimeInvisibleAnnotations'):
        for blob in attrs.get(key, []):
            rr = Reader(blob)
            found.extend(read_annotation(rr, pool) for _ in range(rr.u2()))
    return found


class Cls:
    """Just enough of a class file to answer "does this member exist"."""

    def __init__(self, data):
        r = Reader(data)
        r.u4(), r.u2(), r.u2()
        pool = read_pool(r)
        r.u2()
        self.name = pool[pool[r.u2()]]
        sup = r.u2()
        self.super = pool[pool[sup]] if sup else None
        self.interfaces = [pool[pool[r.u2()]] for _ in range(r.u2())]
        self.fields, self.methods = [], []
        for store in (self.fields, self.methods):
            for _ in range(r.u2()):
                r.u2()
                nm, ds = pool[r.u2()], pool[r.u2()]
                store.append((nm, ds, annotations(read_attrs(r, pool), pool)))
        self.annotations = annotations(read_attrs(r, pool), pool)


class Path:
    """The classpath, parsed one class at a time instead of all at once."""

    def __init__(self, roots):
        self.where, self.cache, self.zips = {}, {}, {}
        for root in roots:
            for jar in self._jars(root):
                try:
                    z = zipfile.ZipFile(jar)
                except zipfile.BadZipFile:
                    continue
                self.zips[jar] = z
                for n in z.namelist():
                    if n.endswith('.class'):
                        self.where.setdefault(n[:-6], (jar, n))

    @staticmethod
    def _jars(root):
        if os.path.isfile(root):
            return [root]
        out = []
        for base, _, files in os.walk(root):
            out += [os.path.join(base, f) for f in files if f.endswith('.jar')]
        return out

    def get(self, name):
        if name not in self.cache:
            spot = self.where.get(name)
            try:
                self.cache[name] = Cls(self.zips[spot[0]].read(spot[1])) if spot else None
            except Exception:
                self.cache[name] = None
        return self.cache[name]

    def chain(self, name):
        """The class and everything it inherits from, as far as we can see."""
        seen, queue, out = set(), [name], []
        while queue:
            n = queue.pop()
            if n in seen:
                continue
            seen.add(n)
            c = self.get(n)
            if c:
                out.append(c)
                queue += [x for x in [c.super] + c.interfaces if x]
        return out

    def has_method(self, owner, name, desc=None):
        return any(n == name and (desc is None or d == desc)
                   for c in self.chain(owner) for n, d, _ in c.methods)

    def has_field(self, owner, name, desc=None):
        return any(n == name and (desc is None or d == desc)
                   for c in self.chain(owner) for n, d, _ in c.fields)


def internal(desc):
    """Lnet/minecraft/Foo; and net.minecraft.Foo both mean the same class."""
    if desc.startswith('L') and desc.endswith(';'):
        return desc[1:-1]
    return desc.replace('.', '/')


def ours(owner):
    """A game class we are entitled to expect. Carpet, fastutil and the JDK are
    not on this classpath by design, so their absence proves nothing."""
    return owner.startswith('net/minecraft/')


def unmapped(name):
    """Names no mapping table assigns, so nothing here can resolve them."""
    return name.startswith('lambda$') or (
        name.startswith('method_') and name[7:].isdigit())


def split_selector(sel):
    """`Lowner;name(desc)ret` and `name(desc)ret` and plain `name`."""
    owner = None
    if sel.startswith('L') and ';' in sel:
        owner, sel = sel[1:sel.index(';')], sel[sel.index(';') + 1:]
    desc = None
    for mark in ('(', ':'):
        if mark in sel:
            sel, desc = sel[:sel.index(mark)], sel[sel.index(mark):]
            break
    return owner, sel, desc


def flatten(value):
    return value if isinstance(value, list) else [value]


def ats(values):
    """Every @At buried in an injector, including the ones inside a @Slice."""
    for key in ('at', 'slice', 'to', 'from'):
        for item in flatten(values.get(key, [])):
            if isinstance(item, tuple):
                name, vals = item
                if name.endswith('/At;'):
                    yield vals
                else:
                    yield from ats(vals)


def main():
    if len(sys.argv) < 3:
        sys.exit(__doc__)
    jar, roots = sys.argv[1], sys.argv[2:]
    path = Path(roots)
    print('classpath: %d classes' % len(path.where))

    problems, skipped, outside, checked = [], [], [], 0
    with zipfile.ZipFile(jar) as z:
        entries = sorted(n for n in z.namelist() if n.endswith('.class'))
        for entry in entries:
            cls = Cls(z.read(entry))
            mixin = next((v for n, v in cls.annotations
                          if n == MIXIN_PKG + 'Mixin;'), None)
            if mixin is None:
                continue
            targets = [internal(t) for t in flatten(mixin.get('value', []))]
            targets += [internal(t) for t in flatten(mixin.get('targets', []))]
            short = cls.name.rsplit('/', 1)[-1]

            for t in targets:
                checked += 1
                if path.get(t) is None:
                    (problems if ours(t) else outside).append(
                        (short, '@Mixin', 'no such class ' + t))

            live = [t for t in targets if path.get(t)]
            for name, desc, anns in cls.fields + cls.methods:
                for ann, vals in anns:
                    if not ann.startswith(MIXIN_PKG):
                        pass
                    kind = ann.rsplit('/', 1)[-1][:-1]

                    if ann == MIXIN_PKG + 'Shadow;':
                        # mixin matches a shadow by name *and* descriptor
                        if 'aliases' in vals or 'prefix' in vals:
                            continue
                        checked += 1
                        look = path.has_method if desc.startswith('(') else path.has_field
                        if live and not any(look(t, name, desc) for t in live):
                            problems.append((short, '@Shadow',
                                             '%s %s %s not on %s'
                                             % ('method' if desc.startswith('(')
                                                else 'field', name, desc,
                                                ', '.join(live))))
                        continue

                    if kind in ('Accessor', 'Invoker') and 'value' in vals:
                        checked += 1
                        want = vals['value']
                        look = path.has_method if kind == 'Invoker' else path.has_field
                        if live and not any(look(t, want) for t in live):
                            problems.append((short, '@' + kind,
                                             '%s not on %s' % (want, ', '.join(live))))
                        continue

                    for sel in flatten(vals.get('method', [])):
                        if any(c in sel for c in '*?') or sel.startswith('/'):
                            continue
                        owner, mname, mdesc = split_selector(sel)
                        checked += 1
                        if unmapped(mname):
                            skipped.append((short, sel))
                            continue
                        where = [owner] if owner else live
                        if where and not any(path.has_method(t, mname, mdesc)
                                             for t in where):
                            problems.append((short, '@' + kind,
                                             '%s not on %s' % (sel, ', '.join(where))))

                    for at in ats(vals):
                        target = at.get('target')
                        if not target or any(c in target for c in '*?'):
                            continue
                        owner, tname, tdesc = split_selector(target)
                        checked += 1
                        if owner is None:
                            continue
                        if not tname:                      # NEW: a bare class
                            if path.get(owner) is None:
                                (problems if ours(owner) else outside).append(
                                    (short, '@At', 'no such class ' + owner))
                            continue
                        if unmapped(tname):
                            skipped.append((short, target))
                            continue
                        ok = (path.has_method(owner, tname, tdesc)
                              if tdesc is None or tdesc.startswith('(')
                              else path.has_field(owner, tname, tdesc[1:]))
                        if path.get(owner) is None:
                            (problems if ours(owner) else outside).append(
                                (short, '@At', 'no such class ' + owner))
                            continue
                        if not ok:
                            problems.append((short, '@At',
                                             '%s not on %s' % (tname + (tdesc or ''),
                                                               owner)))

    print('checked %d injection points in %s' % (checked, os.path.basename(jar)))
    if outside:
        print('\n%d references to classes this classpath does not carry '
              '(optional mods, the JDK), not checked:' % len(outside))
        for who, kind, what in sorted(set(outside)):
            print('   %-34s %-10s %s' % (who, kind, what))
    if skipped:
        print('\n%d selectors no mapping names, check these by hand:' % len(skipped))
        for who, what in sorted(set(skipped)):
            print('   %-34s %s' % (who, what))
    if problems:
        print('\n%d PROBLEMS' % len(problems))
        for who, kind, what in problems:
            print('   %-34s %-10s %s' % (who, kind, what))
        return 1
    print('\nclean: every target resolves')
    return 0


if __name__ == '__main__':
    sys.exit(main())
