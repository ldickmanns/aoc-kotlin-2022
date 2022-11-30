package aoc2021.day01

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(Day01.part1(testInput) == 1)

    val input = readInput("day01").map { it.toInt() }


    println(part1(input))
    println(part2(input))
}

fun part1(input: List<Int>): Int {
    var previousValue = Int.MAX_VALUE
    return input.fold(0) { acc, currentValue ->
        val next = if (currentValue > previousValue) acc + 1 else acc
        previousValue = currentValue
        next
    }
}

fun part2(input: List<Int>): Int {
    val windowedSums = input.windowed(
        size = 3,
        step = 1,
        partialWindows = false,
    ) { it.sum() }
    return part1(windowedSums)
}
