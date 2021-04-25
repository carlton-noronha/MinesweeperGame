package minesweeper

import kotlin.random.Random

class MineSweeperGame(private var numberOfMines: Int) {

    init {
        initializeMines()
    }

    /**
     *  This singleton will store two boards (Arrays).
     *  The first board 'mineSweeperBoard' will be used for display the state of
     *  the board during gameplay.
     *  The second board 'mineSweeperBoardWithMines' will be used only for storing
     *  the mine locations
     */
    companion object {

        /* These are direction vectors that will help navigate across the board
            TopLeft         Top     TopRight
            Left            .       Right
            BottomLeft      Bottom  BottomRight
        */
        val dr: IntArray = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)
        val dc: IntArray = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)

        val mineSweeperBoard: Array<CharArray> = arrayOf(
            charArrayOf(' ', '|', '1', '2', '3', '4', '5', '6', '7', '8', '9', '|'),
            charArrayOf('-', '|', '-', '-', '-', '-', '-', '-', '-', '-', '-', '|'),
            charArrayOf('1', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('2', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('3', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('4', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('5', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('6', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('7', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('8', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('9', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('-', '|', '-', '-', '-', '-', '-', '-', '-', '-', '-', '|')
        )

        val mineSweeperBoardWithMines: Array<CharArray> = arrayOf(
            charArrayOf(' ', '|', '1', '2', '3', '4', '5', '6', '7', '8', '9', '|'),
            charArrayOf('-', '|', '-', '-', '-', '-', '-', '-', '-', '-', '-', '|'),
            charArrayOf('1', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('2', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('3', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('4', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('5', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('6', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('7', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('8', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('9', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|'),
            charArrayOf('-', '|', '-', '-', '-', '-', '-', '-', '-', '-', '-', '|')
        )
    }

    /**
     *  This method sets the specified number of mines of the board
     */
    private fun initializeMines() {
        var row: Int
        var col: Int

        for (mine in 0 until this.numberOfMines) {
            do {
                row = Random.nextInt(2, 11)
                col = Random.nextInt(2, 11)
            } while (mineSweeperBoardWithMines[row][col] == 'X')

            mineSweeperBoardWithMines[row][col] = 'X'
        }
    }
}

/**
 * This function gets the number of mines to set for gameplay
 */
fun getNumberOfMines(): Int {
    print("How many mines do you want on the field? ")
    return readLine()!!.toInt()
}

/**
 * This function controls the game during gameplay
 */
fun playGame(numberOfMines: Int) {
    var numberOfFieldsMarked = 0
    var numberOfMinesMarked = 0
    do {
        print("Set/unset mines marks or claim a cell as free: ")
        val (yStr, xStr, command) = readLine()!!.split(" ")
        // 1 is added to get right coordinates (see board structure)
        val y = yStr.toInt() + 1
        val x = xStr.toInt() + 1

        /*
            If the command is 'mine' then we can only set or unset the location these fields are still unexplored
            and need to be explored

            If the command is 'free' we will explore the fields
         */
        when (command.trim().toLowerCase()) {
            "mine" -> {
                val mineDetails = exploreCellForMine(x, y, numberOfMinesMarked, numberOfFieldsMarked)
                numberOfMinesMarked = mineDetails[0]
                numberOfFieldsMarked = mineDetails[1]

                // Win Condition 1: FInd all mine locations
                if (numberOfMines == numberOfMinesMarked &&
                    numberOfMinesMarked == numberOfFieldsMarked
                ) {
                    displayBoard()
                    println("Congratulations! You found all the mines!")
                    break
                }
            }

            "free" -> {
                // If entered cell has mine the player lost the game
                if (MineSweeperGame.mineSweeperBoardWithMines[x][y] == 'X') {
                    displayBoard(true)
                    println("You stepped on a mine and failed!")
                    break
                } else {
                    floodFillUtil(x, y)
                    val numberOfUnExploredMines: Int = countUnexploredMines()

                    // Win Condition 2: Opening all the safe cells
                    if(numberOfUnExploredMines == numberOfMines) {
                        displayBoard()
                        println("Congratulations! You found all the mines!")
                        break
                    }
                }
            }
        }

        displayBoard()

    } while (true)

}

/**
 * This function counts the number of unexplored cells (both marked and un-marked).
 * If the value it returns is equal to the number of mines in the game we have the
 * game
 *
 * @return
 * numberOfUnexploredCells -> the number of unexplored cells
 * (both marked and un-marked)
 */
fun countUnexploredMines(): Int {
    var numberOfUnexploredCells = 0
    for(i in 2 until MineSweeperGame.mineSweeperBoard.lastIndex) {
        for(j in 2 until MineSweeperGame.mineSweeperBoard[i].lastIndex) {
            if(MineSweeperGame.mineSweeperBoard[i][j] == '.' ||
                MineSweeperGame.mineSweeperBoard[i][j] == '*') {
                ++numberOfUnexploredCells
            }
        }
    }
    return numberOfUnexploredCells
}

/**
 * This method implements the flood fill algorithm
 *
 * @param
 * x -> x coordinate
 * y -> y coordinate
 *
 */
fun floodFillUtil(x: Int, y: Int) {

    // Check whether the coordinate are valid
    if (x <= 1 || y <= 1 ||
        x >= MineSweeperGame.mineSweeperBoardWithMines.lastIndex ||
        y >= MineSweeperGame.mineSweeperBoardWithMines[x].lastIndex
    ) {
        return
    }

    /*
        If the cell has a number it is explored and can't be explored farther or
        if it is cell having zero surrounding mines don't explore it farther
     */
    if(MineSweeperGame.mineSweeperBoard[x][y] in '1'..'8' ||
        MineSweeperGame.mineSweeperBoard[x][y] == '/' ||
        MineSweeperGame.mineSweeperBoardWithMines[x][y] == 'X') {
        return
    }

    val numberOfSurroundingMines = exploreCellNoMine(x, y)
    if(numberOfSurroundingMines > 0) {
        MineSweeperGame.mineSweeperBoard[x][y] = '0' + numberOfSurroundingMines
        return
    } else if(numberOfSurroundingMines == 0) {
        MineSweeperGame.mineSweeperBoard[x][y] = '/'
    }

    floodFillUtil(x - 1, y - 1)
    floodFillUtil(x - 1, y)
    floodFillUtil(x - 1, y + 1)
    floodFillUtil(x, y - 1)
    floodFillUtil(x, y + 1)
    floodFillUtil(x + 1, y - 1)
    floodFillUtil(x + 1, y)
    floodFillUtil(x + 1, y + 1)
}

/**
 * @param
 * x -> x coordinate
 * y -> y coordinate
 * numberOfMinesMarked -> number of mines correctly marked
 * numberOfFieldsMarked -> number of Fields marked in total
 *
 * @return
 * The number of mines correctly marked and the number of fields marked
 */
fun exploreCellForMine(x: Int, y: Int, numberOfMinesMarked: Int, numberOfFieldsMarked: Int): IntArray {
    var countMines = numberOfMinesMarked
    var countFieldsMarked = numberOfFieldsMarked
    if (MineSweeperGame.mineSweeperBoard[x][y] == '.' &&
        MineSweeperGame.mineSweeperBoardWithMines[x][y] == 'X'
    ) { // Unexplored cell with mine
        ++countMines
        ++countFieldsMarked
        MineSweeperGame.mineSweeperBoard[x][y] = '*'
    } else if (MineSweeperGame.mineSweeperBoard[x][y] == '.') { // Unexplored cell without mine
        ++countFieldsMarked
        MineSweeperGame.mineSweeperBoard[x][y] = '*'
    } else if (MineSweeperGame.mineSweeperBoard[x][y] == '*' &&
        MineSweeperGame.mineSweeperBoardWithMines[x][y] == 'X'
    ) { // Un-marking cell with mine
        --countMines
        --countFieldsMarked
        MineSweeperGame.mineSweeperBoard[x][y] = '.'
    } else { // Un-marking cell with no mine
        --countFieldsMarked
        MineSweeperGame.mineSweeperBoard[x][y] = '.'
    }
    return intArrayOf(countMines, countFieldsMarked)
}

/**
 * @param
 * x -> x coordinate
 * y -> y coordinate
 */
private fun exploreCellNoMine(xCoordinate: Int, yCoordinate: Int): Int {
    if (MineSweeperGame.mineSweeperBoardWithMines[xCoordinate][yCoordinate] != 'X') {
        var numberOfSurroundingMines = 0
        for (k in 0..MineSweeperGame.dr.lastIndex) {
            val x: Int = xCoordinate + MineSweeperGame.dr[k]
            val y: Int = yCoordinate + MineSweeperGame.dc[k]

            /*
                Info: Check if x coordinate and y coordinate is valid
            */

            if (x <= 1 || y <= 1 ||
                x >= MineSweeperGame.mineSweeperBoardWithMines.lastIndex ||
                y >= MineSweeperGame.mineSweeperBoardWithMines[xCoordinate].lastIndex
            ) {
                continue
            }

            if (MineSweeperGame.mineSweeperBoardWithMines[x][y] == 'X') {
                ++numberOfSurroundingMines
            }
        }
        return numberOfSurroundingMines
    }
    return -1
}

/**
 * This function display the board with / without mines based on the withMines
 * parameter, default false
 *
 * @param
 * withMines -> display board with mines
 */
fun displayBoard(withMines: Boolean = false) {
    println()
    for (i in 0..MineSweeperGame.mineSweeperBoard.lastIndex) {
        for (j in 0..MineSweeperGame.mineSweeperBoard[i].lastIndex) {
            if(withMines) {
                if(MineSweeperGame.mineSweeperBoardWithMines[i][j] == 'X') {
                    print("${MineSweeperGame.mineSweeperBoardWithMines[i][j]}")
                } else {
                    print("${MineSweeperGame.mineSweeperBoard[i][j]}")
                }
            } else {
                print("${MineSweeperGame.mineSweeperBoard[i][j]}")
            }
        }
        println()
    }
}

fun main() {

    val numberOfMines = getNumberOfMines()
    MineSweeperGame(numberOfMines)

    displayBoard()
    playGame(numberOfMines)
}