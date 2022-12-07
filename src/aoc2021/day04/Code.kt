package aoc2021.day04

import readInput

fun main() {
    val input = readInput("aoc2021/day04/input")

    println(part1(input))
    println(part2(input))
}

typealias NumberBoard = Array<IntArray> // IntArrays are the rows
typealias BingoBoard = Array<BooleanArray> // BooleanArrays are the rows

fun part1(input: List<String>): Int {
    val inputNumbers = input.first().split(",").map { it.toInt() }
    val (numberBoards, bingoBoards) = getBoards(input)

    for (inputNumber in inputNumbers) {
        val boardBingoZip = numberBoards zip bingoBoards
        boardBingoZip.forEach { (boardArray, bingoArray) ->
            val bingo = checkNumber(boardArray, bingoArray, inputNumber)
            if (bingo) {
                val unmarkedSum = getUnmarkedSum(boardArray, bingoArray)
                return unmarkedSum * inputNumber
            }
        }
    }

    return -1
}

fun getBoards(input: List<String>): Pair<List<NumberBoard>, List<BingoBoard>> {
    val boardStrings = input
        .drop(2) // input numbers + blank line
        .filter { it.isNotEmpty() } // remove blank lines
    val boardChunks = boardStrings.chunked(5)

    val numberBoards: List<NumberBoard> = boardChunks.map { board: List<String> ->
        val boardArray = Array(5) { IntArray(5) }
        board.forEachIndexed { rowIndex: Int, row: String ->
            val numbers = row
                .split(" ")
                .filter { it.isNotEmpty() } // single digit numbers result in empty strings
                .map { it.toInt() }
            numbers.forEachIndexed { columnIndex, number ->
                boardArray[rowIndex][columnIndex] = number
            }
        }
        boardArray
    }

    val bingoBoards: List<BingoBoard> = List(numberBoards.size) {
        Array(5) {
            BooleanArray(5) { false }
        }
    }

    return Pair(numberBoards, bingoBoards)
}

fun checkNumber(
    boardArray: Array<IntArray>,
    bingoArray: Array<BooleanArray>,
    inputNumber: Int
): Boolean {
    boardArray.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, number ->
            if (number == inputNumber) {
                bingoArray[rowIndex][columnIndex] = true
                return checkBingo(bingoArray, rowIndex, columnIndex)
            }
        }
    }
    return false
}

fun checkBingo(
    bingoArray: Array<BooleanArray>,
    rowIndex: Int,
    columnIndex: Int,
): Boolean {
    val rowBingo = bingoArray[rowIndex].all { it }
    val columnBingo = bingoArray.map { it[columnIndex] }.all { it }
    return rowBingo or columnBingo
}

fun getUnmarkedSum(
    boardArray: Array<IntArray>,
    bingoArray: Array<BooleanArray>,
): Int {
    var sum = 0
    bingoArray.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, isMarked ->
            if (isMarked.not()) sum += boardArray[rowIndex][columnIndex]
        }
    }
    return sum
}

fun part2(input: List<String>): Int {
    val inputNumbers = input.first().split(",").map { it.toInt() }
    val (numberBoards, bingoBoards) = getBoards(input)
    val boardFinished = BooleanArray(numberBoards.size) { false }

    for (inputNumber in inputNumbers) {
        val boardBingoZip = numberBoards zip bingoBoards
        boardBingoZip.forEachIndexed { index, (boardArray, bingoArray) ->
            if (boardFinished[index]) return@forEachIndexed

            val bingo = checkNumber(boardArray, bingoArray, inputNumber)
            if (bingo) boardFinished[index] = true
            if (boardFinished.all { it }) {
                val unmarkedSum = getUnmarkedSum(boardArray, bingoArray)
                return unmarkedSum * inputNumber
            }
        }
    }

    return -1
}
