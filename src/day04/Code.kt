package day04

import readInput

fun main() {
    val input = readInput("day04/input")
//    val input = readInput("day04/input_test")

    println(part1(input))
    println(part2(input))
}

typealias IntPair = Pair<Int, Int>

val IntPair.lowerBound: Int get() = first

val IntPair.upperBound: Int get() = second

infix fun IntPair.isIn(other: IntPair) = (lowerBound >= other.lowerBound) and (upperBound <= other.upperBound)

infix fun IntPair.overlaps(other: IntPair) =
    ((lowerBound <= other.upperBound) and (lowerBound >= other.lowerBound)) or
        ((upperBound >= other.lowerBound) and (upperBound <= other.upperBound))

fun part1(input: List<String>): Int = input.fold(0) { acc: Int, line: String ->
    val (firstBounds, secondBounds) = getBounds(line)
    if ((firstBounds isIn secondBounds) or (secondBounds isIn firstBounds)) acc + 1 else acc
}

fun getBounds(line: String): Pair<IntPair, IntPair> {
    val firstRange = line.substringBefore(',')
    val secondRange = line.substringAfter(',')

    val firstBounds = extractBounds(firstRange)
    val secondBounds = extractBounds(secondRange)

    return Pair(firstBounds, secondBounds)
}

fun extractBounds(string: String): Pair<Int, Int> {
    val firstNumber = string.substringBefore('-').toInt()
    val secondNumber = string.substringAfter('-').toInt()
    return Pair(firstNumber, secondNumber)
}



fun part2(input: List<String>): Int = input.fold(0) { acc: Int, line: String ->
    val (firstBounds, secondBounds) = getBounds(line)
    if ((firstBounds overlaps secondBounds) or (secondBounds overlaps firstBounds)) acc + 1 else acc
}