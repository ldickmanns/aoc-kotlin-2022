package day10

import readInput

fun main() {
    val input = readInput("day10/input")
//    val input = readInput("day10/input_test")

    println(part1(input))
    println(part2(input))
}

private const val NOOP = "noop"
private const val ADDX = "addx "

fun part1(input: List<String>): Int {
    var cycle = 1
    var x = 1
    var signalStrength = 0
    var nextCycleToCheck = 20
    var timeToCommandCompletion = 0
    var valueToAdd = 0

    val inputIterator = input.iterator()

    while (true) {
        // start i-th cycle
        if (timeToCommandCompletion == 0) {
            val command = inputIterator.next()
            when {
                command.startsWith(NOOP) -> {
                    timeToCommandCompletion = 1
                    valueToAdd = 0
                }

                command.startsWith(ADDX) -> {
                    timeToCommandCompletion = 2
                    valueToAdd = command.removePrefix(ADDX).toInt()
                }
            }
        }

        if (cycle == nextCycleToCheck) {
            nextCycleToCheck += 40
            signalStrength += cycle * x
        }

        // end of i-th cycle
        --timeToCommandCompletion
        if (timeToCommandCompletion == 0) x += valueToAdd
        ++cycle
        if (cycle > 220) break
    }

    return signalStrength
}

fun part2(input: List<String>): String {
    var cycle = 1
    var x = 1
    var timeToCommandCompletion = 0
    var valueToAdd = 0

    val inputIterator = input.iterator()

    val builder = StringBuilder()

    while (true) {
        // start i-th cycle
        if (timeToCommandCompletion == 0) {
            val command = inputIterator.next()
            when {
                command.startsWith(NOOP) -> {
                    timeToCommandCompletion = 1
                    valueToAdd = 0
                }

                command.startsWith(ADDX) -> {
                    timeToCommandCompletion = 2
                    valueToAdd = command.removePrefix(ADDX).toInt()
                }
            }
        }

        // draw pixel
        val currentPixel = (cycle - 1) % 40
        val spriteRange = x - 1..x + 1
        if (currentPixel in spriteRange) builder.append('#') else builder.append('.')
        if (cycle % 40 == 0) builder.append('\n')

        // end of i-th cycle
        --timeToCommandCompletion
        if (timeToCommandCompletion == 0) x += valueToAdd
        ++cycle
        if (cycle > 240) break
    }

    return builder.toString()
}
