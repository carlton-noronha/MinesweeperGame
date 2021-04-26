# Minesweeper Game

## About the game
Minesweeper is a single-player puzzle video game. The objective of the game is to clear a rectangular board containing hidden "mines" or bombs without detonating any of them, with help 
from clues about the number of neighboring mines in each field. <a href="https://en.wikipedia.org/wiki/Minesweeper_(video_game)" target="_blank">Learn more</a>.

## About the program
The code written is in Kotlin. It aims to replicate the classic Minesweeper game. The users must find all the slots free of mines based on hints shown about the number of neighboring 
mines in each field.

## Technical details
The code was divided into functions (Functional Decomposition) to improve readability. Concepts like classes, objects and Simpleton pattern (companion object, in Kotlin) were used.
<strong>To complete this project an important algorithm called <a href="https://en.wikipedia.org/wiki/Flood_fill" target="_blank">Flood Fill</a> was used</strong>. The algorithm was
useful when it came to providing clues to the user when they selected a particular cell. Below is pseudocode of the algorithm as used in the code:


<pre>
<code>
Input:
x -> The X coordinate (in 2D matrix the row index),
y -> The Y coordinate (in 2D matrix the row index)
<strong><i>Algorithm FloodFill(x, y, board[][])</i></strong>

if x and y are invalid:
  return
  
if board[x][y] is a number or board[x][y] is '/' or board[x][y] = 'X'
  return

FloodFill(x - 1, y - 1, board)
FloodFill(x - 1, y, board)
FloodFill(x - 1, y + 1, board)
FloodFill(x, y - 1, board)
FloodFill(x, y + 1, board)
FloodFill(x + 1, y - 1, board)
FloodFill(x + 1, y, board)
FloodFill(x + 1, y + 1, board)
</code>
</pre>



Comments are added so that those who wish to read the code can do so with ease.

## How to play

1. Enter the number of mines
2. Enter the cell coordinates when prompted, the format is: <strong>column-number row-number command</strong>
The <strong>command</strong> can be either <b><i>free</i></b> or <b><i>mine</i></b>.

The game starts with an unexplored minefield that has a user-defined number of mines.

The player can:

    1. Mark unexplored cells as cells that potentially have a mine, and also remove those marks. Any empty cell can be marked, not just the cells that contain a mine. The mark is 
    removed by marking the previously marked cell.

    2. Explore a cell if they think it does not contain a mine.

There are three possibilities after exploring a cell:

    1. If the cell is empty and has no mines around, all the cells around it, including the marked ones, can be explored, and it should be done automatically. Also, if next to the 
    explored cell there is another empty one with no mines around, all the cells around it should be explored as well, and so on, until no more can be explored automatically.

    2. If a cell is empty and has mines around it, only that cell is explored, revealing a number of mines around it.

    3. If the explored cell contains a mine, the game ends and the player loses.

There are two possible ways to win:

    1. Marking all the cells that have mines correctly (<b>mine</b> command should be used).

    2. Opening all the safe cells so that only those with unexplored mines are left (<b>free</b> command should be used).
    
## Screenshots

1. Winning by opening all the safe cells
<img src="https://raw.githubusercontent.com/carlton-noronha/MinesweeperGame/master/Screenshots/openSafeCells.gif">

2. Stepping on a mine results in a loss which then discloses all mine spots
<img src="https://raw.githubusercontent.com/carlton-noronha/MinesweeperGame/master/Screenshots/mineStep.gif">

3. Playing using <b>mine</b> command
<img src="https://raw.githubusercontent.com/carlton-noronha/MinesweeperGame/master/Screenshots/playingByMine.jpg">
