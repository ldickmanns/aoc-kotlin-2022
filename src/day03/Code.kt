package day03

import readInput

fun main() {
    val input = readInput("day03/input")
//    val input = readInput("day03/input_test")

    println("asdfasdfasdf".toSet())

    println(part1(input))
    println(part2(input))
}

val Char.priority: Int
    get() = if (isLowerCase()) {
        code - 'a'.code + 1
    } else code - 'A'.code + 27

fun part1(input: List<String>): Int {
    var sumOfPriorities = 0
    outer@ for (line in input) {
        val halfLength = line.length / 2
        val firstCompartment = line.take(halfLength)
        val secondCompartment = line.takeLast(halfLength)

        for (char in firstCompartment) {
            if (char in secondCompartment) {
                sumOfPriorities += char.priority
                continue@outer
            }
        }
    }
    return sumOfPriorities
}

fun part2(input: List<String>): Int {
    var sumOfPriorities = 0
    input.windowed(
        size = 3,
        step = 3,
    ) { lines: List<String> ->
        val charSets = lines.map { it.toSet() }
        val intersectionChar = charSets[0].intersect(charSets[1]).intersect(charSets[2]).first()
        sumOfPriorities += intersectionChar.priority
    }
    return sumOfPriorities
}
