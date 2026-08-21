#!/usr/bin/env python3
"""Publish built jars to Modrinth, one version record per file.

    tools/publish.py <folder>          show what would be published
    tools/publish.py <folder> --go     publish it
    tools/publish.py <folder> --body <file>   also replace the project page

The token is read from ~/.modrinth-token or MODRINTH_TOKEN. It never appears on
a command line, so it stays out of the process list and the shell history.

Version number, loader and supported game versions are all read from the jar's
own metadata rather than passed in by hand, so the record on Modrinth cannot
drift from what the file actually declares.
"""
import json
import os
import re
import sys
import urllib.error
import urllib.request
import uuid
import zipfile

PROJECT = '2fOOeJ4d'
API = 'https://api.modrinth.com/v2'
UA = 'FAMJAM1/masaadditions-port'

# The 1.21 line in order. A file's range is expanded against this list.
LINE = ['1.21', '1.21.1', '1.21.2', '1.21.3', '1.21.4', '1.21.5', '1.21.6',
        '1.21.7', '1.21.8', '1.21.9', '1.21.10', '1.21.11']

# Only the library is required; the other three switch themselves on when the
# mod they extend is present.
DEPS = {
    'neoforge': [('SKI34J7B', 'required'),      # MaFgLib
                 ('dCKRaeBC', 'optional'),      # Forgematica
                 ('yke6wdGF', 'optional'),      # Tweakerge
                 ('zfPoD7Tm', 'optional')],     # BocchUD
    'fabric':   [('GcWjdA9I', 'required'),      # MaLiLib
                 ('bEpr0Arc', 'optional'),      # Litematica
                 ('t5wuYk45', 'optional'),      # Tweakeroo
                 ('UMxybHE8', 'optional')],     # MiniHUD
}


def key(v):
    return tuple(int(x) for x in v.split('.'))


def expand(spec):
    """Every release in the 1.21 line a version spec admits."""
    spec = spec.strip()
    if re.fullmatch(r'[\d.]+', spec):
        return [spec]
    lo = hi = None
    for m in re.finditer(r'(>=|<=|<|>)\s*([\d.]+)', spec):
        op, ver = m.group(1), m.group(2)
        if op.startswith('>'):
            lo = (ver, op == '>=')
        else:
            hi = (ver, op == '<=')
    out = []
    for v in LINE:
        if lo and (key(v) < key(lo[0]) or (not lo[1] and key(v) == key(lo[0]))):
            continue
        if hi and (key(v) > key(hi[0]) or (not hi[1] and key(v) == key(hi[0]))):
            continue
        out.append(v)
    return out


def describe(jar):
    """(version number, loader, [game versions]) straight out of the jar."""
    with zipfile.ZipFile(jar) as z:
        names = z.namelist()
        if 'fabric.mod.json' in names:
            j = json.loads(z.read('fabric.mod.json'))
            return j['version'], 'fabric', expand(j['depends']['minecraft'])
        toml = z.read('META-INF/neoforge.mods.toml').decode()
    ver = re.search(r'^version = "([^"]+)"', toml, re.M).group(1)
    block = re.search(r'modId = "minecraft".*?versionRange = "([^"]+)"', toml, re.S)
    lo, hi = re.match(r'\[([\d.]+),([\d.]+)\)', block.group(1)).groups()
    return ver, 'neoforge', expand('>=%s <%s' % (lo, hi))


def token():
    t = os.environ.get('MODRINTH_TOKEN')
    if t:
        return t.strip()
    path = os.path.expanduser('~/.modrinth-token')
    if os.path.exists(path):
        return open(path).read().strip()
    sys.exit('no token: put it in ~/.modrinth-token or MODRINTH_TOKEN')


def changelog(loader, versions):
    """Written per loader. The NeoForge text is the 1.21.1 range fix; a later
    NeoForge upload would need its own words here."""
    def covers(joiner):
        if len(versions) == 1:
            return versions[0]
        return '%s %s %s' % (', '.join(versions[:-1]), joiner, versions[-1])

    if loader == 'fabric':
        if len(versions) == 1:
            spread_en = 'This file is bound to %s alone.' % versions[0]
            spread_ru = 'Файл привязан только к %s.' % versions[0]
        else:
            spread_en = ('One file covers %s: the game code this mod patches is '
                         'the same across them, and the build was checked '
                         'against each one before release.' % covers('and'))
            spread_ru = ('Один файл покрывает %s: код игры, который правит мод, '
                         'на этих версиях одинаковый, и сборка проверена против '
                         'каждой из них.' % covers('и'))
        return """First Fabric release for this game version.

%s

Only MaLiLib is required. Litematica, Tweakeroo and MiniHUD are optional —
each part switches itself on when the mod it extends is installed.

Первый выпуск на Fabric под эту версию игры. %s

Обязателен только MaLiLib. Litematica, Tweakeroo и MiniHUD необязательны:
каждая часть включается сама, если установлен мод, который она расширяет.""" % (
            spread_en, spread_ru)

    return """Fixes the version ranges: the file declared no game version and no upper
bound on the loader, so it would install onto later releases and then fail.

Исправлены диапазоны версий: файл не объявлял версию игры и не имел верхней
границы по загрузчику, поэтому устанавливался на более поздние выпуски и падал."""


def upload(jar, ver, loader, versions, tok):
    data = {
        'name': 'MasaAdditions %s' % ver,
        'version_number': ver,
        'changelog': changelog(loader, versions),
        'dependencies': [{'project_id': p, 'dependency_type': d}
                         for p, d in DEPS[loader]],
        'game_versions': versions,
        'version_type': 'release',
        'loaders': [loader],
        'featured': False,
        'project_id': PROJECT,
        'file_parts': ['file'],
        'primary_file': 'file',
    }
    boundary = uuid.uuid4().hex
    body = bytearray()

    def part(headers, payload):
        body.extend(('--%s\r\n%s\r\n\r\n' % (boundary, headers)).encode())
        body.extend(payload)
        body.extend(b'\r\n')

    part('Content-Disposition: form-data; name="data"\r\n'
         'Content-Type: application/json', json.dumps(data).encode())
    part('Content-Disposition: form-data; name="file"; filename="%s"\r\n'
         'Content-Type: application/java-archive' % os.path.basename(jar),
         open(jar, 'rb').read())
    body.extend(('--%s--\r\n' % boundary).encode())

    req = urllib.request.Request(API + '/version', data=bytes(body), method='POST')
    req.add_header('Authorization', tok)
    req.add_header('User-Agent', UA)
    req.add_header('Content-Type', 'multipart/form-data; boundary=' + boundary)
    with urllib.request.urlopen(req) as r:
        return json.load(r)


def set_body(path, tok):
    text = open(path).read()
    req = urllib.request.Request(API + '/project/' + PROJECT,
                                 data=json.dumps({'body': text}).encode(),
                                 method='PATCH')
    req.add_header('Authorization', tok)
    req.add_header('User-Agent', UA)
    req.add_header('Content-Type', 'application/json')
    urllib.request.urlopen(req).read()
    return len(text)


def main():
    if len(sys.argv) < 2:
        sys.exit(__doc__)
    folder, go = sys.argv[1], '--go' in sys.argv
    body = sys.argv[sys.argv.index('--body') + 1] if '--body' in sys.argv else None
    jars = sorted(f for f in os.listdir(folder) if f.endswith('.jar'))
    if not jars:
        sys.exit('no jars in that folder')

    plan = [(os.path.join(folder, n),) + describe(os.path.join(folder, n))
            for n in jars]
    print('project: %s' % PROJECT)
    print('%-26s %-9s %-34s %s' % ('version', 'loader', 'game versions', 'file'))
    for path, ver, loader, versions in plan:
        print('%-26s %-9s %-34s %s' % (ver, loader, ', '.join(versions),
                                       os.path.basename(path)))
    if body:
        print('\nproject page would be replaced by %s (%d chars)'
              % (body, len(open(body).read())))
    if not go:
        print('\nthis was a dry run. add --go to publish')
        return 0

    tok = token()
    if body:
        print('project page updated: %d chars' % set_body(body, tok))
    for path, ver, loader, versions in plan:
        try:
            res = upload(path, ver, loader, versions, tok)
            print('published: %s -> %s' % (ver, res.get('id')))
        except urllib.error.HTTPError as e:
            print('FAILED %s: %s\n   %s' % (ver, e.code, e.read().decode()[:300]))
            return 1
    return 0


if __name__ == '__main__':
    sys.exit(main())
