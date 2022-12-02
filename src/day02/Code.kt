package day02

import readInput

fun main() {
    val input = readInput("day02/input")

    println(part1(input))
    println(part2(input))
}

// A, X -> Rock, 1
// B, Y -> Paper, 2
// C, Z -> Scissors, 3
fun part1(input: List<String>): Int {
    var score = 0

    input.forEach {
        score += when {
            "X" in it -> 1
            "Y" in it -> 2
            "Z" in it -> 3
            else -> 0
        }

        score += when(it) {
            "A X" -> 3
            "A Y" -> 6
            "B Y" -> 3
            "B Z" -> 6
            "C Z" -> 3
            "C X" -> 6
            else -> 0
        }
    }

    return score
}

// A -> Rock, 1
// B -> Paper, 2
// C -> Scissors, 3
// X -> loose
// Y -> draw
// Z -> win
fun part2(input: List<String>): Int {
    var score = 0

    input.forEach {
        score += when {
            "X" in it -> 0
            "Y" in it -> 3
            "Z" in it -> 6
            else -> 0
        }

        score += when(it) {
            "A X" -> 3
            "A Y" -> 1
            "A Z" -> 2
            "B X" -> 1
            "B Y" -> 2
            "B Z" -> 3
            "C X" -> 2
            "C Y" -> 3
            "C Z" -> 1
            else -> 0
        }
    }

    return score
}
