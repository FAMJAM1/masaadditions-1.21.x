# MasaAdditions — Fabric port

An unofficial Fabric port of [MasaAdditions](https://github.com/hp3721/masaadditions),
an add-on for masa's mods: Tweakeroo, MiniHUD and Litematica.

Неофициальный порт [MasaAdditions](https://github.com/hp3721/masaadditions) на Fabric.
Дополнение к модам masa: Tweakeroo, MiniHUD и Litematica.

Supported game versions are listed on the [releases page](https://github.com/FAMJAM1/masaadditions-port/releases).
Поддерживаемые версии игры — на [странице релизов](https://github.com/FAMJAM1/masaadditions-port/releases).

[English](#english) · [Русский](#русский)

---

## English

### What's inside

- [TweakerooAdditions](https://github.com/hp3721/masaadditions/wiki/TweakerooAdditions)
- [MiniHUDAdditions](https://github.com/hp3721/masaadditions/wiki/MiniHUDAdditions)
- [LitematicaAdditions](https://github.com/hp3721/masaadditions/wiki/LitematicaAdditions)

Each part stands on its own: without the mod it extends, it simply stays quiet.

### What you need alongside it

This port sits directly on masa's own mods.

| Mod | What for | Required |
|---|---|---|
| [malilib](https://modrinth.com/mod/malilib) | the shared base | yes |
| [Fabric API](https://modrinth.com/mod/fabric-api) | malilib needs it | yes |
| [Litematica](https://modrinth.com/mod/litematica) | | for LitematicaAdditions |
| [Tweakeroo](https://modrinth.com/mod/tweakeroo) | | for TweakerooAdditions |
| [MiniHUD](https://modrinth.com/mod/minihud) | | for MiniHUDAdditions |

### Installing

1. Install [Fabric Loader](https://fabricmc.net/use/) at the version the release names.
2. Download malilib, Fabric API and whichever mods from the table you want, and drop them in `.minecraft/mods`.
3. Drop the MasaAdditions jar from the [releases page](https://github.com/FAMJAM1/masaadditions-port/releases) in there too.

This is a client mod; the server does not need it.

### Building

```
./gradlew build
```

The jar lands in `build/libs/`. Gradle fetches the JDK itself, so nothing needs installing by hand.

---

## Русский

### Что внутри

- [TweakerooAdditions](https://github.com/hp3721/masaadditions/wiki/TweakerooAdditions)
- [MiniHUDAdditions](https://github.com/hp3721/masaadditions/wiki/MiniHUDAdditions)
- [LitematicaAdditions](https://github.com/hp3721/masaadditions/wiki/LitematicaAdditions)

Каждый раздел включается сам по себе: если соответствующего мода нет, его дополнения просто молчат.

### Что нужно поставить

Порт работает поверх оригинальных модов masa.

| Мод | Зачем | Обязателен |
|---|---|---|
| [malilib](https://modrinth.com/mod/malilib) | общая база | да |
| [Fabric API](https://modrinth.com/mod/fabric-api) | нужен malilib | да |
| [Litematica](https://modrinth.com/mod/litematica) | | для LitematicaAdditions |
| [Tweakeroo](https://modrinth.com/mod/tweakeroo) | | для TweakerooAdditions |
| [MiniHUD](https://modrinth.com/mod/minihud) | | для MiniHUDAdditions |

### Установка

1. Поставить [Fabric Loader](https://fabricmc.net/use/) той же версии, что указана у релиза.
2. Скачать malilib, Fabric API и нужные из таблицы моды, положить в `.minecraft/mods`.
3. Туда же положить джарник MasaAdditions со [страницы релизов](https://github.com/FAMJAM1/masaadditions-port/releases).

Мод клиентский: на сервер ставить не нужно.

### Сборка

```
./gradlew build
```

Джарник появится в `build/libs/`. JDK Gradle скачает сам, ставить руками ничего не нужно.

---

## License · Лицензия

GPL-3.0, see [LICENSE](LICENSE) · см. [LICENSE](LICENSE).

The original is [hp3721/masaadditions](https://github.com/hp3721/masaadditions). Some of the code goes
back to [UsefulMod](https://github.com/Nessiesson/UsefulMod) and
[CutelessMod](https://github.com/Nessiesson/CutelessMod) by [nessie](https://github.com/Nessiesson),
licensed under [MIT](LICENSE_MIT).

Оригинал — [hp3721/masaadditions](https://github.com/hp3721/masaadditions). Часть кода восходит к
[UsefulMod](https://github.com/Nessiesson/UsefulMod) и [CutelessMod](https://github.com/Nessiesson/CutelessMod)
за авторством [nessie](https://github.com/Nessiesson), под лицензией [MIT](LICENSE_MIT).
