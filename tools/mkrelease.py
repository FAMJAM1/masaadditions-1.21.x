#!/usr/bin/env python3
"""Keep one GitHub release per game version, carrying both loaders' jars.

    tools/mkrelease.py            show what would change
    tools/mkrelease.py --go       upload the jars and rewrite the notes

The tag sits on the NeoForge branch for that line; the Fabric jar in the same
release is built from the matching fabric-* branch, and the notes name both.
"""
import os
import subprocess
import sys

NEO = os.path.expanduser('~/Загрузки/masaadditions-release')
FAB = os.path.expanduser('~/Загрузки/masaadditions-fabric-release')

# tag, base version, NeoForge deps, Fabric deps (None when the line has no
# Fabric build), the game versions the Fabric file covers, upstream warning
LINES = [
    ('1.21.1-1.2.7', '1.21.1', '1.2.7',
     ('21.1.248', '0.4.3', '0.4.1', '0.4.3', '0.4.1'), None, None, None),
    ('1.21.3-1.3.0', '1.21.3', '1.3.0',
     ('21.3.97', '0.4.4', '0.4.2', '0.4.1', '0.4.1'),
     ('0.22.8', '0.20.9', '0.22.8', '0.33.10'), '1.21.2 and 1.21.3', None),
    ('1.21.4-1.3.0', '1.21.4', '1.3.0',
     ('21.4.157', '0.4.3', '0.4.4', '0.4.1', '0.4.1'),
     ('0.23.5', '0.21.7', '0.23.6', '0.34.8'), '1.21.4', None),
    ('1.21.5-1.3.0', '1.21.5', '1.3.0',
     ('21.5.98', '0.4.3', '0.4.3', '0.4.1', '0.4.1'),
     ('0.24.3', '0.22.5', '0.24.3', '0.35.4'), '1.21.5', None),
    ('1.21.8-1.3.0', '1.21.8', '1.3.0',
     ('21.8.54', '0.4.5', '0.4.2', '0.4.2', '0.4.1'),
     ('0.25.7', '0.23.7', '0.25.6', '0.36.7'), '1.21.6, 1.21.7 and 1.21.8',
     'Forgematica **0.4.3 and 0.4.4** for this version leave one mixin target '
     'as an unmapped name, so it matches nothing and the game stops while '
     'loading. Use 0.4.2.'),
    ('1.21.10-1.3.0', '1.21.10', '1.3.0',
     ('21.10.64', '0.4.11', '0.4.3', '0.4.2', '0.4.3'),
     ('0.26.8', '0.24.9', '0.26.5', '0.37.6'), '1.21.9 and 1.21.10',
     'Forgematica **0.4.5** for this version ships its metadata URLs as '
     'unfilled build placeholders, and the loader refuses the file outright. '
     'Use 0.4.3.'),
    ('1.21.11-1.3.0', '1.21.11', '1.3.0',
     ('21.11.45', '0.4.4', '0.4.2', '0.4.0', '0.4.1'),
     ('0.27.16', '0.26.12', '0.27.11', '0.38.13'), '1.21.11',
     'MaFgLib **0.4.5** for this version hooks a call NeoForge no longer makes '
     'while loading language files. Use 0.4.4.'),
]

HEAD = """Unofficial port of MasaAdditions by hp3721, built for Minecraft {mc}.

### NeoForge

| NeoForge | MaFgLib | Forgematica | Tweakerge | BoccHUD |
|---|---|---|---|---|
| {nf} | {ml} | {fm} | {tw} | {bh} |

Only MaFgLib is required. The other three are optional — each module switches
itself on only when the mod it extends is installed.
"""

FABRIC = """
### Fabric

| MaLiLib | Litematica | Tweakeroo | MiniHUD |
|---|---|---|---|
| {ma} | {li} | {tk} | {mh} |

Only MaLiLib is required, and it brings Fabric API with it. The Fabric file
covers {covers} — the game code this mod patches is the same across them, and
the build was checked against each one.
"""

WARN = """
### Do not install the newest of everything

{text}

The dependency ranges in the NeoForge file already exclude it, so your launcher
should warn you rather than let you into a crash. This is a problem in that
mod, not in this one.
"""

TAIL = """
Built from {branches}. Client-side only — not needed on the server.

Also on [Modrinth](https://modrinth.com/mod/masaadditions-port).
"""


def notes(mc, base, neo, fab, covers, warn):
    body = HEAD.format(mc=mc, nf=neo[0], ml=neo[1], fm=neo[2], tw=neo[3], bh=neo[4])
    if fab:
        body += FABRIC.format(ma=fab[0], li=fab[1], tk=fab[2], mh=fab[3],
                              covers=covers)
    else:
        body += '\nThere is no Fabric build for this line.\n'
    if warn:
        body += WARN.format(text=warn)
    branches = '`neoforge-%s`' % mc + (' and `fabric-%s`' % mc if fab else '')
    return body + TAIL.format(branches=branches)


def main():
    go = '--go' in sys.argv
    for tag, mc, base, neo, fab, covers, warn in LINES:
        jars = [os.path.join(NEO, 'MasaAdditions-%s+%s.neoforge.jar' % (base, mc))]
        if fab:
            jars.append(os.path.join(FAB, 'MasaAdditions-%s+%s.fabric.jar' % (base, mc)))
        missing = [j for j in jars if not os.path.exists(j)]
        if missing:
            print('MISSING:', *missing)
            return 1
        title = 'MasaAdditions %s — Minecraft %s' % (base, mc)
        if not go:
            print('%-16s %-38s %s' % (tag, title,
                                      ', '.join(os.path.basename(j) for j in jars)))
            continue
        r = subprocess.run(['gh', 'release', 'upload', tag] + jars + ['--clobber'],
                           capture_output=True, text=True)
        if r.returncode:
            print('upload failed', tag, r.stderr.strip()[:200])
            return 1
        r = subprocess.run(['gh', 'release', 'edit', tag, '--title', title,
                            '--notes', notes(mc, base, neo, fab, covers, warn)],
                           capture_output=True, text=True)
        if r.returncode:
            print('edit failed', tag, r.stderr.strip()[:200])
            return 1
        print('updated:', tag)
    if not go:
        print('\nthis was a dry run. add --go to apply')
    return 0


if __name__ == '__main__':
    sys.exit(main())
