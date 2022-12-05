package day05

import readInput

fun main() {
    val input = readInput("day05/input")
//    val input = readInput("day05/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): String {
    val separatorLine = input.indexOfFirst { it.isEmpty() }

    val startConfiguration = input.slice(0 until separatorLine).dropLast(1)
    val instructions = input.slice((separatorLine + 1) until input.size)

    val configuration = extractStartConfiguration(startConfiguration)
    configuration.carryOutInstructions(instructions)

    return buildString {
        configuration.forEach { append(it.last()) }
    }
}

fun extractStartConfiguration(input: List<String>): Array<ArrayDeque<Char>> {
    val size = input.maxOf { getPositionRange(it).toList().size }
    val configuration = Array(size) { ArrayDeque<Char>() }

    input.forEach { line ->
        val range = getPositionRange(line)
        range.forEachIndexed { index, position ->
            val char = line[position]
            if (char == ' ') return@forEachIndexed
            configuration[index].addFirst(char)
        }
    }

    return configuration
}

fun getPositionRange(line: String) = 1 until line.length step 4

fun Array<ArrayDeque<Char>>.carryOutInstructions(
    instructions: List<String>
) = instructions.forEach { instruction ->
    val split = instruction.split(" ")

    val amount = split[1].toInt()
    val from = split[3].toInt() - 1
    val to = split[5].toInt() - 1

    repeat(amount) {
        val item = this[from].removeLast()
        this[to].addLast(item)
    }
}

fun part2(input: List<String>): String {
    val separatorLine = input.indexOfFirst { it.isEmpty() }

    val startConfiguration = input.slice(0 until separatorLine).dropLast(1)
    val instructions = input.slice((separatorLine + 1) until input.size)

    val configuration = extractStartConfiguration(startConfiguration)
    configuration.carryOutInstructionsWith9001(instructions)

    return buildString {
        configuration.forEach { append(it.last()) }
    }
}

fun Array<ArrayDeque<Char>>.carryOutInstructionsWith9001(
    instructions: List<String>
) = instructions.forEach { instruction ->
    val split = instruction.split(" ")

    val amount = split[1].toInt()
    val from = split[3].toInt() - 1
    val to = split[5].toInt() - 1

    val tmp = ArrayDeque<Char>()
    repeat(amount) {
        val item = this[from].removeLast()
        tmp.addFirst(item)
    }
    tmp.forEach { this[to].addLast(it) }
}
