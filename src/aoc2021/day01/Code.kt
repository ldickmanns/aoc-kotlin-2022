package aoc2021.day01

import readInput

fun main() {
    val input = readInput("aoc2021/day01/input")
    val inputAsInt = input.map { it.toInt() }


    println(part1(inputAsInt))
    println(part2(inputAsInt))
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
