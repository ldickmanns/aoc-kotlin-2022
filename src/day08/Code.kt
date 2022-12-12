package day08

import readInput

fun main() {
    val input = readInput("day08/input")
//    val input = readInput("day08/input_test")

    val grid = extractGrid(input)

    println(part1(grid))
    println(part2(grid))
}

private fun extractGrid(
    input: List<String>
): Array<IntArray> = input.map { row: String ->
    IntArray(row.length) { row[it].digitToInt() }
}.toTypedArray()

private val <T> Array<T>.nRows
    get() = size

private val Array<IntArray>.nColumns
    get() = first().size

fun part1(grid: Array<IntArray>): Int {
    val isVisible = Array(grid.nRows) { BooleanArray(grid.nColumns) }
    checkRows(grid, isVisible)
    checkColumns(grid, isVisible)
    return isVisible.sumOf { row -> row.count { it } }
}

private fun checkRows(
    grid: Array<IntArray>,
    isVisible: Array<BooleanArray>,
): Array<BooleanArray> {
    grid.forEachIndexed { rowIndex, row ->
        var highestTreeFromLeft = -1
        var highestTreeFromRight = -1

        repeat(row.size) { columnIndexFromLeft ->
            val treeFromLeft = row[columnIndexFromLeft]
            if (treeFromLeft > highestTreeFromLeft) {
                highestTreeFromLeft = treeFromLeft
                isVisible[rowIndex][columnIndexFromLeft] = true
            }

            val columnIndexFromRight = row.lastIndex - columnIndexFromLeft
            val treeFromRight = row[columnIndexFromRight]
            if (treeFromRight > highestTreeFromRight) {
                highestTreeFromRight = treeFromRight
                isVisible[rowIndex][columnIndexFromRight] = true
            }
        }
    }

    return isVisible
}

private fun checkColumns(
    grid: Array<IntArray>,
    isVisible: Array<BooleanArray>,
) {
    repeat(grid.nColumns) { columnIndex ->
        var highestTreeFromTop = -1
        var highestTreeFromBottom = -1

        repeat(grid.nRows) { rowIndexFromTop ->
            val treeFromTop = grid[rowIndexFromTop][columnIndex]
            if (treeFromTop > highestTreeFromTop) {
                highestTreeFromTop = treeFromTop
                isVisible[rowIndexFromTop][columnIndex] = true
            }

            val rowIndexFromBottom = grid.lastIndex - rowIndexFromTop
            val treeFromBottom = grid[rowIndexFromBottom][columnIndex]
            if (treeFromBottom > highestTreeFromBottom) {
                highestTreeFromBottom = treeFromBottom
                isVisible[rowIndexFromBottom][columnIndex] = true
            }
        }
    }
}

fun part2(
    grid: Array<IntArray>,
): Int {
    val scenicScore = Array(grid.nRows) {
        IntArray(grid.nColumns) { 1 }
    }

    checkRows(grid, scenicScore)
    checkColumns(grid, scenicScore)

    return scenicScore.maxOf { it.max() }
}

private const val N_DIGITS = 10

private fun checkRows(
    grid: Array<IntArray>,
    scenicScore: Array<IntArray>,
) {
    repeat(grid.nRows) { rowIndex ->
        val canSeeTreesToLeft = IntArray(N_DIGITS)  // going from left to right
        val canSeeTreesToRight = IntArray(N_DIGITS) // going from right to left

        repeat(grid.nColumns) { columnIndexFromLeft ->
            val treeFromLeft = grid[rowIndex][columnIndexFromLeft]
            scenicScore[rowIndex][columnIndexFromLeft] *= canSeeTreesToLeft[treeFromLeft]
            for (i in 0..treeFromLeft) canSeeTreesToLeft[i] = 1
            for (i in treeFromLeft + 1 until N_DIGITS) canSeeTreesToLeft[i] += 1

            val columnIndexFromRight = grid[rowIndex].lastIndex - columnIndexFromLeft
            val treeFromRight = grid[rowIndex][columnIndexFromRight]
            scenicScore[rowIndex][columnIndexFromRight] *= canSeeTreesToRight[treeFromRight]
            for (i in 0..treeFromRight) canSeeTreesToRight[i] = 1
            for (i in treeFromRight + 1 until N_DIGITS) canSeeTreesToRight[i] += 1
        }
    }
}

private fun checkColumns(
    grid: Array<IntArray>,
    scenicScore: Array<IntArray>,
) {
    repeat(grid.nColumns) { columnIndex ->
        val canSeeTreesToTop = IntArray(N_DIGITS)       // going from top to bottom
        val canSeeTreesToBottom = IntArray(N_DIGITS)    // going from bottom to top

        repeat(grid.nRows) { rowIndexFromTop ->
            val treeFromTop = grid[rowIndexFromTop][columnIndex]
            scenicScore[rowIndexFromTop][columnIndex] *= canSeeTreesToTop[treeFromTop]
            for (i in 0..treeFromTop) canSeeTreesToTop[i] = 1
            for (i in treeFromTop + 1 until N_DIGITS) canSeeTreesToTop[i] += 1

            val rowIndexFromBottom = grid.lastIndex - rowIndexFromTop
            val treeFromBottom = grid[rowIndexFromBottom][columnIndex]
            scenicScore[rowIndexFromBottom][columnIndex] *= canSeeTreesToBottom[treeFromBottom]
            for (i in 0..treeFromBottom) canSeeTreesToBottom[i] = 1
            for (i in treeFromBottom + 1 until N_DIGITS) canSeeTreesToBottom[i] += 1
        }
    }
}
