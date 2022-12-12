package day09

import readInput
import kotlin.math.abs

fun main() {
    val input = readInput("day09/input")
//    val input = readInput("day09/input_test")
//    val input = readInput("day09/input_test_2")


    println(part1(input))
    println(part2(input))
}

data class Coordinates(
    val x: Int,
    val y: Int,
) {
    operator fun plus(other: Coordinates) = Coordinates(
        x = this.x + other.x,
        y = this.y + other.y,
    )

    operator fun minus(other: Coordinates) = Coordinates(
        x = this.x - other.x,
        y = this.y - other.y,
    )

    infix fun isNotAdjacentTo(
        other: Coordinates
    ) = abs(this.x - other.x) > 1 || abs(this.y - other.y) > 1

    infix fun dragTo(
        other: Coordinates
    ): Coordinates {
        val xDelta = when (other.x - this.x) {
            -2, -1 -> -1
            0 -> 0
            1, 2 -> 1
            else -> error("Further than two apart")
        }

        val yDelta = when (other.y - this.y) {
            -2, -1 -> -1
            0 -> 0
            1, 2 -> 1
            else -> error("Further than two apart")
        }

        return this + Coordinates(xDelta, yDelta)
    }
}

val UP = Coordinates(0, 1)
val RIGHT = Coordinates(1, 0)
val DOWN = Coordinates(0, -1)
val LEFT = Coordinates(-1, 0)

fun part1(input: List<String>): Int {
    var headPosition = Coordinates(0, 0)
    var tailPosition = Coordinates(0, 0)
    val visitedPositionCounter = mutableMapOf(tailPosition to 1)

    input.forEach { command ->
        val direction = command.direction
        val distance = command.substringAfter(' ').toInt()

        repeat(distance) {
            headPosition += direction
            if (headPosition isNotAdjacentTo tailPosition) {
                tailPosition = updateTail(direction, headPosition)
                val previousCounter = visitedPositionCounter[tailPosition] ?: 0
                visitedPositionCounter[tailPosition] = previousCounter + 1
            }
        }
    }

    return visitedPositionCounter.size
}

private val String.direction: Coordinates
    get() = when {
        startsWith("U") -> UP
        startsWith("R") -> RIGHT
        startsWith("D") -> DOWN
        startsWith("L") -> LEFT
        else -> error("Illegal direction")
    }

private fun updateTail(direction: Coordinates, headPosition: Coordinates) = when (direction) {
    UP -> headPosition + DOWN
    RIGHT -> headPosition + LEFT
    DOWN -> headPosition + UP
    LEFT -> headPosition + RIGHT
    else -> error("Illegal direction")
}

fun part2(input: List<String>): Int {
    var headPosition = Coordinates(0, 0)
    val tailPositions = Array(9) { Coordinates(0, 0) }
    val visitedPositionCounter = mutableMapOf(tailPositions.last() to 1)

    input.forEach { command ->
        val direction = command.direction
        val distance = command.substringAfter(' ').toInt()

        repeat(distance) {
            headPosition += direction

            var previousTilePosition = headPosition

            for (i in tailPositions.indices) {
                val currentTilePosition = tailPositions[i]
                if (currentTilePosition isNotAdjacentTo previousTilePosition) {
                    tailPositions[i] = tailPositions[i] dragTo previousTilePosition
                    previousTilePosition = tailPositions[i]
                } else return@repeat
            }

            val previousCounter = visitedPositionCounter[tailPositions.last()] ?: 0
            visitedPositionCounter[tailPositions.last()] = previousCounter + 1
        }
    }

    return visitedPositionCounter.size
}
