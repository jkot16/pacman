# 🟡 Pacman – Java Maze Arcade Game

![Java](https://img.shields.io/badge/Java-17+-red?logo=java)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?logo=oracle)
![CustomAssets](https://img.shields.io/badge/Assets-Custom-yellow)
![Game](https://img.shields.io/badge/Genre-Arcade-brightgreen)

<img width="975" height="589" alt="pacman" src="https://github.com/user-attachments/assets/54b95aa5-93a0-4a7c-a96b-7f69cf2d2367" />


---

## 📑 Table of Contents

1. [🔍 Project Overview](#1-project-overview)  
2. [🧠 Key Features](#2-key-features)  
3. [⚙️ Tech Stack](#3-tech-stack)  
4. [🕹️ Gameplay & Controls](#4-gameplay--controls)  
5. [👾 Ghosts & Power-Ups](#5-ghosts--power-ups)  
6. [💾 High Score System](#6-high-score-system)  
7. [🎨 Graphics & Animations](#7-graphics--animations)  
8. [📦 Installation & Usage](#8-installation--usage)

---

## 1. 🔍 Project Overview

**Pacman** is a Java-based arcade game with modern design, animated sprites, and selectable map sizes.  
Built entirely with Swing and custom assets, the game offers smooth gameplay, upgrades, and a local leaderboard.

## 🎥 Demo

See **Pacman** in action:  
▶️ [Watch on YouTube](https://youtu.be/DBJ_XIjbmwY)

---

## 2. 🧠 Key Features

- 5 playable map sizes:
  - 🟢 Mini (13×9)  
  - 🟡 Small (15×11)  
  - 🟠 Medium (25×17)  
  - 🔴 Large (35×23)  
  - 🟣 Extra Large (45×29)

- Real-time Pacman animation in 4 directions (Up / Down / Left / Right)
- AI-controlled ghosts with collision detection and respawn logic
- Randomly spawning power-ups:
  - ⚡ `SPEEDBOOST` – temporary +50% player speed  
  - ❤️ `EXTRALIFE` – grants 1 additional life  
  - ✨ `DOUBLEPOINTS` – doubles score for collected dots  
  - 👻 `INVISIBILITY` – makes Pacman immune to ghosts  
  - 🐌 `SLOWENEMIES` – halves ghost speed temporarily

- Custom game-over screen with nickname input
- Local leaderboard system sorted by score (per map)
- Integrated Swing menu bar with:
  - New Game
  - High Scores (for each map)
  - Exit option
 

<img width="1183" height="781" alt="game" src="https://github.com/user-attachments/assets/ca6f5c0e-4da0-483f-912d-d4cb2ea3cb19" />

---

## 3. ⚙️ Tech Stack

| Component     | Technology     |
|---------------|----------------|
| Language      | Java 17+       |
| GUI           | Swing          |
| Animation     | ImageIcon + Threads |
| Data Storage  | Serializable `.dat` files |
| Assets        | PNG icons and backgrounds |

---

## 4. 🕹️ Gameplay & Controls

| Action             | Key               |
|--------------------|-------------------|
| Move               | Arrow keys        |
| Pause / Exit       | Esc               |
| Start Game         | Menu → New Game   |
| View High Scores   | Menu → High Scores |

> Each map offers a different level size, ghost count, and difficulty.

---

## 5. 👾 Ghosts & Power-Ups

### 👻 Ghosts
- Move randomly each frame  
- Colliding with player reduces lives  
- Spawn upgrades with chance

### ⚡ Power-Ups
| Type           | Effect              |
|----------------|---------------------|
| Speed Boost    | +50% player speed   |
| Extra Life     | +1 life             |
| Double Points  | 2x points per dot   |
| Invisibility   | Ghosts ignore player |
| Slow Enemies   | Ghosts move slower  |

Upgrades last for 8 seconds.

---

## 6. 💾 High Score System

- After game over, players enter a nickname  
- Scores are saved locally based on map size:  
  `highscores_Mini.dat`, `highscores_Large.dat`, etc.  
- Leaderboard keeps top scores, sorted by points  
- Viewable via menu at any time

---

## 7. 🎨 Graphics & Animations

- 3-frame directional animation for player movement  
- Ghosts and power-ups rendered on-grid using `JPanel`  
- Visual layering for player, ghosts, and dots  
- Assets stored in `src/images/` and `src/images/animation/`


---

## 8. 📦 Installation & Usage

### Requirements:
- Java 17 or newer
- Any IDE (IntelliJ / Eclipse) or command-line

### Run the Game:

```bash
git clone https://github.com/yourusername/pacman-java.git
cd pacman-java
javac src/Pacman.java
java -cp src Pacman
