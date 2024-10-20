# Architecture of Cascadia

This file contains all usefull information about Cascadia game.  

First of all, we want thank you!  

## 1. Core Module
- Game.java:  
    Handles the overall game state, turn order, setup, and game-ending conditions.
    Uses a TurnManager to switch between players and trigger game events.
    Manages the GameBoard, ensuring it refreshes after every turn.

- Player.java:  
    Stores player-specific information, such as the player's environment (tiles + wildlife), nature tokens, and score.
    Provides methods for drafting tiles and tokens, placing them into the environment, and spending nature tokens.

- TurnManager.java:  
    Controls the turn-taking mechanism.
    Ensures rules like overpopulation are followed, and nature tokens are used properly.

- GameBoard.java:  
    Stores the 4 available habitat tile and wildlife token pairs.
    Provides a mechanism for drawing new tiles and tokens after each turn.
    Implements overpopulation handling (wiping excess tokens and drawing new ones).


## 2. Tile Management Module

- Tile.java:  
    Represents a habitat tile, storing the terrain type (mountains, forests, etc.) and possible wildlife that can be placed.
    Contains methods for adjacency checking to ensure legal placement.

- KeystoneTile.java:  
    Inherits from Tile, but provides additional logic for handling keystone features (earning nature tokens when wildlife is placed).

- Deck.java:  
    Manages the shuffling and drawing of habitat tiles.


## 3. Wildlife Management Module

- WildlifeToken.java:  
    Represents the individual wildlife tokens (bear, elk, etc.).
    Each token is linked to a Tile for placement validation.

- Bag.java:  
    Handles the random drawing of wildlife tokens.

- WildlifeScoringCard.java:  
    Represents one of the wildlife scoring cards. It contains the specific rules for scoring (e.g., group sizes for bears, runs for salmon).
    Implement different scoring strategies for each animal type.


## 4. Scoring Module

- ScoreCalculator.java:  
    Implements the scoring logic for each player based on their environment.
    Uses WildlifeScoringCard to determine points based on player wildlife arrangement.
    Handles contiguous habitat scoring (e.g., scoring for the largest contiguous forests, rivers, etc.).


## 5. Graphics Module

// to think about it:
- GameRenderer.java:  
    Renders the game board visually.
    Updates after each turn to display the current state of the game (player's environment, available tiles/tokens, etc.).

- PlayerView.java:  
    Displays each player’s environment (tiles and tokens they have placed).
    Could handle mouse clicks and interactions for tile/token placement.


## 6. User Interface Module

- MainMenu.java:  
    Displays the start screen, allowing the player to start a new game, choose player counts, and load any settings.

- EndScreen.java:  
    Displays final scores and the winner once the game ends.
