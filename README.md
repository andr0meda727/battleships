# Battleships - Strategy Game

Classic battleship game with three AI difficulty levels, built using JavaFX.

## Description

Battleships is an implementation of the popular naval warfare game where the player faces off against an intelligent AI opponent. The game offers an intuitive graphical interface and advanced AI algorithms providing challenges at various difficulty levels.

## Features

### Game Mechanics
- **10x10 Board** - Classic battlefield layout
- **Ship Fleet**: 
  - 1x Carrier (5 squares)
  - 1x Battleship (4 squares)
  - 2x Cruisers (3 squares each)
  - 1x Destroyer (2 squares)

### Difficulty Levels

#### Easy
- Completely random attacks
- Perfect for beginners
- No targeting strategy

#### Medium
- Implements "Hunt/Target" system
- Continues attacks in vicinity after a hit
- Detects ship orientation (horizontal/vertical)
- Remembers previous hits

#### Hard
- Advanced probability algorithm
- Analyzes possible placements of remaining ships
- Dynamically calculates optimal targets
- Prioritizes squares around hits with +1000 bonus
- Excludes squares around sunken ships

### User Interface
- Two boards - player and opponent
- Random ship placement button
- Game reset at any time
- End game popup with result information

## Project Structure

```
battleships/
├── src/main/java/battleships/
│   ├── controllers/          # JavaFX controllers
│   │   ├── ButtonController.java
│   │   ├── DifficultyController.java
│   │   └── MainMenuController.java
│   ├── models/               # Game data models
│   │   ├── bots/            # AI implementations
│   │   │   ├── AiEasy.java
│   │   │   ├── AiMedium.java
│   │   │   └── AiHard.java
│   │   ├── AttackOutcome.java
│   │   ├── Board.java
│   │   ├── Cell.java
│   │   ├── GameState.java
│   │   ├── Player.java
│   │   └── Ship.java
│   ├── enums/               # Enum types
│   │   ├── Difficulty.java
│   │   ├── Mode.java
│   │   ├── Orientation.java
│   │   └── attackResult.java
│   ├── interfaces/          # Interfaces
│   │   └── AiBot.java
│   ├── utils/              # Utility tools
│   │   └── UIUtils.java
│   └── Battleships.java    # Main application class
└── src/main/resources/battleships/views/  # FXML files
```

## How to Play

1. **Select difficulty level** from the list (easy/medium/hard)
2. **Place your ships** - click "Rozmieść statki" (Place Ships) to randomly position your fleet (you can repeat this)
3. **Start the game** - click the "Start" button
4. **Attack** - click on the opponent's board to shoot
5. **Win** - sink all enemy ships before they sink yours!

## Implementation Details

### Probability System (Hard AI)
The hard difficulty AI uses an advanced probability calculation system:
- Calculates for each square how many ships could be placed there
- Considers remaining ship lengths
- Adds bonus for squares adjacent to hits
- Detects hit ship orientation
- Excludes squares around sunken ships according to the rules

### End Game Detection
The game automatically ends when:
- All player ships are sunk (defeat)
- All AI ships are sunk (victory)
