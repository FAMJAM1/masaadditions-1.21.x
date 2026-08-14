# MasaAdditions — NeoForge port

An unofficial NeoForge port of [MasaAdditions](https://github.com/hp3721/masaadditions),
an add-on for masa's mods: Tweakeroo, MiniHUD and Litematica.

Неофициальный порт [MasaAdditions](https://github.com/hp3721/masaadditions) на NeoForge.
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

masa's own mods have no NeoForge builds, so this port sits on top of their ports instead.

| Mod | What for | Required |
|---|---|---|
| [MaFgLib](https://modrinth.com/mod/mafglib) | the malilib port, the shared base | yes |
| [Forgematica](https://modrinth.com/mod/forgematica) | the Litematica port | for LitematicaAdditions |
| [Tweakerge](https://modrinth.com/mod/tweakerge) | the Tweakeroo port | for TweakerooAdditions |
| [BoccHUD](https://modrinth.com/mod/bocchud) | the MiniHUD port | for MiniHUDAdditions |

### Installing

1. Install [NeoForge](https://neoforged.net/) at the version the release names.
2. Download MaFgLib and whichever mods from the table you want, and drop them in `.minecraft/mods`.
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

Оригинальные моды masa под NeoForge не выходят, поэтому порт работает поверх их портов.

| Мод | Зачем | Обязателен |
|---|---|---|
| [MaFgLib](https://modrinth.com/mod/mafglib) | порт malilib, общая база | да |
| [Forgematica](https://modrinth.com/mod/forgematica) | порт Litematica | для LitematicaAdditions |
| [Tweakerge](https://modrinth.com/mod/tweakerge) | порт Tweakeroo | для TweakerooAdditions |
| [BoccHUD](https://modrinth.com/mod/bocchud) | порт MiniHUD | для MiniHUDAdditions |

### Установка

1. Поставить [NeoForge](https://neoforged.net/) той же версии, что указана у релиза.
2. Скачать MaFgLib и нужные из таблицы моды, положить в `.minecraft/mods`.
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
