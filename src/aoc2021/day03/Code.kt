package aoc2021.day03

import readInput

fun main() {
    val input = readInput("aoc2021/day03/input")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val nColumns = input.first().length
    val oneCounters = IntArray(nColumns)
    input.forEach { line: String ->
        line.forEachIndexed {index: Int, c: Char ->
            if (c == '1') oneCounters[index] += 1
        }
    }
    val gammaRateString = buildString {
        oneCounters.forEach {
            if (it > 500) append("1") else append("0")
        }
    }
    val epsilonRateString = buildString {
        oneCounters.forEach {
            if (it > 500) append("0") else append("1")
        }
    }
    val gammaRate = Integer.parseInt(gammaRateString, 2)
    val epsilonRate = Integer.parseInt(epsilonRateString, 2)
    return gammaRate * epsilonRate
}

fun part2(input: List<String>): Int {
    return input.size
}
