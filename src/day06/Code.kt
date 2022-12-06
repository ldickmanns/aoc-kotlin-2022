package day06

import readInput

fun main() {
    val input = readInput("day06/input")
//    val input = readInput("day06/input_test")

    println("asdfa".toSet())

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int = findFirstDistinctSequence(input.first(), 4)

fun findFirstDistinctSequence(input: String, sequenceLength: Int): Int {
    var counter = sequenceLength
    val windowed = input.windowed(
        size = sequenceLength,
        step = 1,
    )
    for (window in windowed) {
        if (window.toSet().size == sequenceLength) break
        ++counter
    }
    return counter
}

fun part2(input: List<String>): Int = findFirstDistinctSequence(input.first(), 14)
