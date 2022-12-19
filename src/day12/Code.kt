package day12

import readInput

typealias Grid = Array<IntArray>

val Grid.nRows get() = size
val Grid.nColumns get() = first().size

operator fun Grid.get(coordinates: Coordinates) = this[coordinates.y][coordinates.x]
operator fun Grid.set(coordinates: Coordinates, int: Int) {
    this[coordinates.y][coordinates.x] = int
}

typealias Coordinates = Pair<Int, Int>

val Coordinates.x get() = first
val Coordinates.y get() = second

val Coordinates.up get() = Coordinates(x, y + 1)
val Coordinates.right get() = Coordinates(x + 1, y)
val Coordinates.down get() = Coordinates(x, y - 1)
val Coordinates.left get() = Coordinates(x - 1, y)

private infix fun Coordinates.inside(grid: Grid) = x >= 0 && x < grid.nColumns && y >= 0 && y < grid.nRows

fun main() {
    val input = readInput("day12/input")
//    val input = readInput("day12/input_test")

    val heightMap = transformToHeightMap(input)

    println(part1(heightMap))
    println(part2(heightMap))
}

private fun transformToHeightMap(
    input: List<String>
): Grid = input.map { row ->
    row.map { it.code }.toIntArray()
}.toTypedArray()

fun part1(grid: Grid): Int {
    val startingPoint = findLetter(letter = 'S', grid = grid)
    return computeDistance(grid = grid, startingPoint = startingPoint)
}

private fun computeDistance(grid: Grid, startingPoint: Coordinates): Int {
    val distances = Array(grid.nRows) { IntArray(grid.nColumns) { Int.MAX_VALUE } }
    distances[startingPoint] = 0

    dijkstra(
        currentPoint = startingPoint,
        grid = grid,
        distances = distances,
    )

    val targetPoint = findLetter(letter = 'E', grid = grid)

    return distances[targetPoint]
}

private fun findLetter(letter: Char, grid: Grid): Coordinates {
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, code ->
            if (code == letter.code) return Coordinates(columnIndex, rowIndex)
        }
    }
    error("Letter not found")
}

private fun dijkstra(
    currentPoint: Coordinates,
    currentCode: Int = 'a'.code,
    grid: Grid,
    steps: Int = 0,
    distances: Grid,
): Unit = with(currentPoint) {
    val directions = mutableListOf<Coordinates>()

    // up
    val up = currentPoint.up
    if (up inside grid) directions.add(up)

    // right
    val right = currentPoint.right
    if (right inside grid) directions.add(Coordinates(x + 1, y))

    // down
    val down = currentPoint.down
    if (down inside grid) directions.add(down)

    // left
    val left = currentPoint.left
    if (left inside grid) directions.add(left)

    val nextSteps = steps + 1
    directions.forEach { destinationCoordinates ->
        val destinationCode = grid[destinationCoordinates]
        if (destinationCode == 'E'.code && currentCode == 'z'.code) {
            distances[destinationCoordinates] = nextSteps
            return
        }
        if (destinationCode > currentCode + 1 || destinationCode == 'E'.code) return@forEach

        if (distances[destinationCoordinates] > nextSteps) {
            distances[destinationCoordinates] = nextSteps
            dijkstra(
                currentPoint = destinationCoordinates,
                currentCode = destinationCode,
                grid = grid,
                steps = nextSteps,
                distances = distances,
            )
        }
    }
}

fun part2(grid: Grid): Int {
    val startingPoint = findLetter(letter = 'S', grid = grid)
    grid[startingPoint] = 'a'.code

    val startingPoints = findLetters('a', grid = grid)

    var shortestDistance = Int.MAX_VALUE

    startingPoints.forEach { potentialStartingPoint ->
        val distance = computeDistance(grid = grid, startingPoint = potentialStartingPoint)

        if (distance < shortestDistance) shortestDistance = distance
    }

    return shortestDistance
}

private fun findLetters(letter: Char, grid: Grid): List<Coordinates> {
    val coordinates = mutableListOf<Coordinates>()
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, code ->
            if (code == letter.code) coordinates.add(Coordinates(columnIndex, rowIndex))
        }
    }
    return coordinates
}
