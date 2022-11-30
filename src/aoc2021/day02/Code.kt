package aoc2021.day02

import readInput

fun main() {
    val input = readInput("aoc2021/day02/input")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var horizontalPosition = 0
    var depth = 0
    input.forEach { line: String ->
        when {
            "forward" in line -> horizontalPosition += line.removePrefix("forward ").toInt()
            "down" in line -> depth += line.removePrefix("down ").toInt()
            "up" in line -> depth -= line.removePrefix("up ").toInt()
            else -> error("Invalid Input")
        }
    }
    println("Depth $depth")
    println("Horizontal position $horizontalPosition")
    return depth * horizontalPosition
}

fun part2(input: List<String>): Int {
    var horizontalPosition = 0
    var depth = 0
    var aim = 0
    input.forEach { line: String ->
        when {
            "forward" in line -> {
                val asInt = line.removePrefix("forward ").toInt()
                horizontalPosition += asInt
                depth += aim * asInt
            }
            "down" in line -> aim += line.removePrefix("down ").toInt()
            "up" in line -> aim -= line.removePrefix("up ").toInt()
            else -> error("Invalid Input")
        }
    }
    println("Depth $depth")
    println("Horizontal position $horizontalPosition")
    return depth * horizontalPosition
}
