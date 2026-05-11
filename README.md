# MasaAdditions — Unofficial Port

[![License: GPL-3.0](https://img.shields.io/badge/license-GPL--3.0-blue.svg)](LICENSE)
[![License: MIT](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE_MIT)
[![Fabric](https://img.shields.io/badge/Minecraft-1.21.11%20%7C%20Fabric-blue)](https://fabricmc.net)
[![NeoForge](https://img.shields.io/badge/Minecraft-1.21.1%20%7C%20NeoForge-orange)](https://neoforged.net)

> ⚠️ **Unofficial port** of [masaadditions](https://github.com/hp3721/masaadditions)
> The original is archived. This port maintains support for modern versions

---

## About
Addon for **Tweakeroo**, **MiniHUD**, and **Litematica** with extra QoL features, settings, and improvements

### Modules
| Module | Description |
|--------|-------------|
| **TweakerooAdditions** | Inventory tweaks, extended hotkeys, auto-actions, etc. |
| **MiniHUDAdditions** | Custom HUD overlays, expanded chunk/mob info, etc. |
| **LitematicaAdditions** | Advanced previews, bulk placement, sync tools, etc. |

> 💡 Full config options available in-game: `Mods → MasaAdditions`

---

## Installation

### Requirements
- **Fabric**: Minecraft `1.21.11` + Fabric Loader + [Fabric API](https://modrinth.com/mod/fabric-api)
- **NeoForge**: Minecraft `1.21.1` + NeoForge Installer
- **Dependencies**: Matching versions of Tweakeroo / MiniHUD / Litematica for your loader

### Steps
1. Download the correct build from [Releases](/releases)
2. Place the JAR + required dependencies into your `mods/` folder
3. Launch the game

---

## Building from Source
```bash
git clone https://github.com/FAMJAM1/masaadditions-1.21.x.git
cd masaadditions-1.21.x
./gradlew build  # Windows: gradlew build
```
Output: `build/libs/`. IntelliJ IDEA recommended

---

## Contributing
1. Open an **Issue** (bug report or feature request).
2. Fork → create `feature/...` branch → implement → test
3. Submit a **Pull Request**. Discuss major changes first

---

## License
- Main code: **GPL-3.0** → [LICENSE](LICENSE)
- Adapted parts (UsefulMod/CutelessMod): **MIT** → [LICENSE_MIT](LICENSE_MIT)

---

## 🔗 Links
- [Original mod (archived)](https://github.com/hp3721/masaadditions)
- [Masa's Mods](https://masa.dy.fi/mcmods/)
- [Report a Bug](/issues)
- [Masa Discord](https://discord.gg/5Fz3F3q)
