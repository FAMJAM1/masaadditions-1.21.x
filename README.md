# MasaAdditions — Unofficial Port

[![License: GPL-3.0](https://img.shields.io/badge/license-GPL--3.0-blue.svg)](LICENSE)
[![License: MIT](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE_MIT)
[![Fabric](https://img.shields.io/badge/Minecraft-1.21.11%20%7C%20Fabric-blue)](https://fabricmc.net)
[![NeoForge](https://img.shields.io/badge/Minecraft-1.21.1%20%7C%20NeoForge-orange)](https://neoforged.net)

> ⚠️ **Unofficial port** of [masaadditions](https://github.com/hp3721/masaadditions) by hp3721.  
> The original is archived. This port maintains support for modern versions of Minecraft.

---

## About

**MasaAdditions** extends Masa's mods — Tweakeroo, MiniHUD, and Litematica — with dozens of extra tweaks and quality-of-life options not present in the originals.

This port supports **Minecraft 1.21.x** on both Fabric and NeoForge:

| Branch | Loader | Minecraft |
|--------|--------|-----------|
| [`masaadditions-1.21.11`](https://github.com/FAMJAM1/masaadditions-1.21.x/tree/masaadditions-1.21.1) | Fabric | 1.21.11 |
| [`masaadditions-1.21.1`](https://github.com/FAMJAM1/masaadditions-1.21.x/tree/neoforge-1.21.1) | NeoForge | 1.21.1 |

---

## Features

### TweakerooAdditions
Extra configuration options and tweaks on top of Tweakeroo / Tweakerge:
- **Block breaking particle tweaks** — control count, scale, and speed
- **Click recipe craft** — craft by clicking a recipe with CTRL+SHIFT
- **Auto respawn on death** — no need to click the respawn button
- **Mining ghost block fix** — reduces ghost blocks while mining
- **Force swap gear** — equip armor from main hand by right-clicking while sneaking
- **Llama steering** — control llamas while riding them
- **Blink drive** — teleport to the block you're looking at (requires op)
- **Perimeter wall dig helper** — prevents mining below specified block types
- **Disable honey block slowdown / jumping** — fine-grained honey block control
- **Override sky time / window title** — client-side cosmetic overrides
- **Disable rendering options** — beacon beams, boss bars, footstep particles, scoreboard, Realms button, etc.

Full list: [TweakerooAdditions wiki](https://github.com/hp3721/masaadditions/wiki/TweakerooAdditions)

### MiniHUDAdditions
Extra overlays and expanded info for MiniHUD / BoccHUD.

Full list: [MiniHUDAdditions wiki](https://github.com/hp3721/masaadditions/wiki/MiniHUDAdditions)

### LitematicaAdditions
Additional tools for Litematica / Forgematica, including **Render Held Item Only** — highlights only the schematic blocks matching the item currently held in your hand.

Full list: [LitematicaAdditions wiki](https://github.com/hp3721/masaadditions/wiki/LitematicaAdditions)

> 💡 All options are configurable in-game via **Mods → MasaAdditions**

---

## Installation

### Fabric (Minecraft 1.21.11)

**Requirements:**
- [Fabric Loader](https://fabricmc.net/use)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [MaLiLib](https://modrinth.com/mod/malilib)
- [Tweakeroo](https://modrinth.com/mod/tweakeroo), [MiniHUD](https://modrinth.com/mod/minihud), [Litematica](https://modrinth.com/mod/litematica)

**Steps:**
1. Install Fabric Loader
2. Download MasaAdditions from [Releases](https://github.com/FAMJAM1/masaadditions-1.21.x/releases)
3. Place the JAR and all required dependencies into your `mods/` folder
4. Launch the game

### NeoForge (Minecraft 1.21.1)

**Requirements:**
- NeoForge 1.21.1
- [MaFgLib](https://modrinth.com/mod/mafglib)
- [Forgematica](https://modrinth.com/mod/forgematica), [Tweakerge](https://modrinth.com/mod/tweakerge), [BoccHUD](https://modrinth.com/mod/bocchud)

**Steps:**
1. Install NeoForge 1.21.1
2. Download MasaAdditions (NeoForge build) from [Releases](https://github.com/FAMJAM1/masaadditions-1.21.x/releases)
3. Place the JAR and all required dependencies into your `mods/` folder
4. Launch the game

> **Client-side only** — no server installation required.

---

## Building from Source

```bash
git clone https://github.com/FAMJAM1/masaadditions-1.21.x.git
cd masaadditions-1.21.x

# Fabric branch
git checkout masaadditions-1.21.1
./gradlew build

# NeoForge branch
git checkout neoforge-1.21.1
./gradlew build
```

Output: `build/libs/`. IntelliJ IDEA recommended.

---

## Contributing

1. Open an [Issue](https://github.com/FAMJAM1/masaadditions-1.21.x/issues) for bug reports or feature requests
2. Fork → create a `feature/...` branch → implement → test
3. Submit a [Pull Request](https://github.com/FAMJAM1/masaadditions-1.21.x/pulls) — discuss major changes first

---

## License

- Main code: **GPL-3.0** → [LICENSE](LICENSE)
- Adapted parts (UsefulMod / CutelessMod by nessie): **MIT** → [LICENSE_MIT](LICENSE_MIT)

---

## 🔗 Links

- [Original mod (archived)](https://github.com/hp3721/masaadditions)
- [Masa's Mods](https://masa.dy.fi/mcmods/)
- [Modrinth page](https://modrinth.com/mod/masaadditions-port)
- [Report a Bug](https://github.com/FAMJAM1/masaadditions-1.21.x/issues)
