package day14

import day09.Coordinates
import readInput

// x -> distance to right
// y -> distance down
// Sand falls (at each step)
//   - down, if not possible
//   - down-left, if not possible
//   - down-right, if not possible
//   - comes to a rest

typealias RockStroke = List<Coordinates>

val startingPoint = Coordinates(x = 500, y = 0)

operator fun Array<BooleanArray>.get(coordinates: Coordinates) = this[coordinates.y][coordinates.x]
operator fun Array<BooleanArray>.set(coordinates: Coordinates, boolean: Boolean) {
    this[coordinates.y][coordinates.x] = boolean
}

val Coordinates.down: Coordinates
    get() = copy(y = y + 1)

val Coordinates.downLeft: Coordinates
    get() = copy(x = x - 1, y = y + 1)

val Coordinates.downRight: Coordinates
    get() = copy(x = x + 1, y = y + 1)

private infix fun Coordinates.inside(grid: Array<BooleanArray>) =
    x >= 0 && x < grid.first().size && y >= 0 && y < grid.size

private infix fun Coordinates.outside(grid: Array<BooleanArray>) = !inside(grid)

fun main() {
    val input = readInput("day14/input")
//    val input = readInput("day14/input_test")

//    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val rockStrokes = parseInput(input)

    val boundingBox = getBoundingBox(rockStrokes)

    val occupationGrid = getOccupationGrid(rockStrokes, boundingBox)

    return computeRestingUnits(boundingBox, occupationGrid)
}

private fun parseInput(input: List<String>): List<RockStroke> = input.map { line ->
    val coordinatesStrings = line.split(" -> ")
    coordinatesStrings.map { coordinatesString ->
        val coordinates = coordinatesString.split(",").map { it.toInt() }
        Coordinates(coordinates.first(), coordinates.last())
    }
}

private fun getBoundingBox(
    rockStrokes: List<RockStroke>,
    maxYOffset: Int = 0,
    xPadding: Int = 0,
): Pair<Coordinates, Coordinates> {
    var minCoordinates = startingPoint
    var maxCoordinates = startingPoint

    rockStrokes.forEach { rockStroke: RockStroke ->
        rockStroke.forEach { coordinates: Coordinates ->
            if (coordinates.x > maxCoordinates.x) maxCoordinates = maxCoordinates.copy(x = coordinates.x)
            if (coordinates.x < minCoordinates.x) minCoordinates = minCoordinates.copy(x = coordinates.x)
            if (coordinates.y > maxCoordinates.y) maxCoordinates = maxCoordinates.copy(y = coordinates.y)
            if (coordinates.y < minCoordinates.y) minCoordinates = minCoordinates.copy(y = coordinates.y)
        }
    }

    minCoordinates = minCoordinates.copy(x = minCoordinates.x - xPadding)
    maxCoordinates = maxCoordinates.copy(x = maxCoordinates.x + xPadding, y = maxCoordinates.y + maxYOffset)

    return Pair(minCoordinates, maxCoordinates)
}

private fun getOccupationGrid(
    rockStrokes: List<RockStroke>,
    boundingBox: Pair<Coordinates, Coordinates>
): Array<BooleanArray> {
    val (minCoordinates, maxCoordinates) = boundingBox

    val deltaCoordinates = maxCoordinates - minCoordinates

    val occupationGrid = Array(deltaCoordinates.y + 1) { BooleanArray(deltaCoordinates.x + 1) }

    rockStrokes.forEach { rockStroke: RockStroke ->
        rockStroke.windowed(2) {
            val lineStart = it.first()
            val lineEnd = it.last()

            val xRange = if (lineStart.x <= lineEnd.x) lineStart.x..lineEnd.x else lineEnd.x..lineStart.x
            val yRange = if (lineStart.y <= lineEnd.y) lineStart.y..lineEnd.y else lineEnd.y..lineStart.y


            xRange.forEach { x ->
                yRange.forEach { y ->
                    val normalizedX = x - minCoordinates.x
                    val normalizedY = y - minCoordinates.y
                    occupationGrid[normalizedY][normalizedX] = true
                }
            }
        }
    }

    return occupationGrid
}

private fun computeRestingUnits(
    boundingBox: Pair<Coordinates, Coordinates>,
    occupationGrid: Array<BooleanArray>
): Int {
    var sandPosition = startingPoint - boundingBox.first
    var restingUnits = 0
    while (true) {
        when {
            sandPosition.down outside occupationGrid -> break
            !occupationGrid[sandPosition.down] -> sandPosition = sandPosition.down
            sandPosition.downLeft outside occupationGrid -> break
            !occupationGrid[sandPosition.downLeft] -> sandPosition = sandPosition.downLeft
            sandPosition.downRight outside occupationGrid -> break
            !occupationGrid[sandPosition.downRight] -> sandPosition = sandPosition.downRight
            else -> {
                if (occupationGrid[sandPosition]) break
                occupationGrid[sandPosition] = true
                ++restingUnits
                sandPosition = startingPoint - boundingBox.first
            }
        }
    }

    occupationGrid.forEach { row ->
        row.forEach { print(if (it) '#' else '.') }
        println()
    }

    println("-".repeat(25))

    return restingUnits
}

fun part2(input: List<String>): Int {
    val rockStrokes = parseInput(input)

    val boundingBox = getBoundingBox(rockStrokes, maxYOffset = 2, xPadding = 200)

    val occupationGrid = getOccupationGrid(rockStrokes, boundingBox)

    occupationGrid.last().indices.forEach {
        occupationGrid.last()[it] = true
    }

    return computeRestingUnits(boundingBox, occupationGrid)
}
