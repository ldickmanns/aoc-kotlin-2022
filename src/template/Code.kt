package template

import readInput

fun main() {
    val input = readInput("dayXX/input") // TODO change me
    val inputAsInt = input.map { it.toInt() }

    println(part1(inputAsInt))
    println(part2(inputAsInt))
}

fun part1(input: List<Int>): Int {
    return input.size
}

fun part2(input: List<Int>): Int {
    return input.size
}
