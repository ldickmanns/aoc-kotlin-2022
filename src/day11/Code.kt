package day11

import readInput
import java.util.*

fun main() {
    val input = readInput("day11/input")
//    val input = readInput("day11/input_test")

    println(part1(input))
    println(part2(input))
}

typealias WorryLevel = Long

data class Monkey(
    val items: Deque<WorryLevel>,
    val operation: (WorryLevel) -> WorryLevel,
    val divisor: Int,
    val onTrueMonkeyIndex: Int,
    val onFalseMonkeyIndex: Int,
    var numberOfInspections: Int = 0,
)

fun part1(input: List<String>): Int {
    val monkeys = getMonkeys(input)

    repeat(20) {
        monkeys.forEach { monkey ->
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.pop()
                val updatedWorryLevel = monkey.operation(item)
                ++monkey.numberOfInspections
                val lostInterest = updatedWorryLevel / 3
                val recipient = if (lostInterest % monkey.divisor == 0L) {
                    monkeys[monkey.onTrueMonkeyIndex]
                } else monkeys[monkey.onFalseMonkeyIndex]
                recipient.items.add(lostInterest)
            }
        }
    }

    return monkeys.map { it.numberOfInspections }.sortedDescending().take(2).reduce(Int::times)
}

private fun getMonkeys(input: List<String>) = input
    .filter { it.isNotEmpty() }
    .chunked(6) { monkeyChunk ->
        Monkey(
            items = getStartingItems(monkeyChunk[1]),
            operation = getOperation(monkeyChunk[2]),
            divisor = getDivisor(monkeyChunk[3]),
            onTrueMonkeyIndex = getTrueMonkey(monkeyChunk[4]),
            onFalseMonkeyIndex = getFalseMonkey(monkeyChunk[5]),
        )
    }

private const val STARTING_ITEMS = "Starting items: "
private const val COMMA = ", "

fun getStartingItems(
    string: String
): Deque<WorryLevel> {
    val trimmed = string.trim()
    val withoutPrefix = trimmed.removePrefix(STARTING_ITEMS)
    val split = withoutPrefix.split(COMMA)
    val worryLevelList = split.map { it.toLong() }
    return LinkedList(worryLevelList)
}

private const val OPERATION = "Operation: new = old "

fun getOperation(
    string: String
): (WorryLevel) -> WorryLevel {
    val trimmed = string.trim()
    val withoutPrefix = trimmed.removePrefix(OPERATION)
    val operation: WorryLevel.(WorryLevel) -> WorryLevel = when (withoutPrefix.substringBefore(' ')) {
        "+" -> WorryLevel::plus
        "*" -> WorryLevel::times
        else -> error("Invalid operator")
    }
    val secondOperand = when (val secondOperandString = withoutPrefix.substringAfter(' ')) {
        "old" -> null
        else -> secondOperandString.toLong()
    }
    return { it.operation(secondOperand ?: it) }
}

private const val TEST = "Test: divisible by "

fun getDivisor(
    string: String
): Int {
    val trimmed = string.trim()
    val withoutPrefix = trimmed.removePrefix(TEST)
    return withoutPrefix.toInt()
}

private const val TRUE_MONKEY = "If true: throw to monkey "

fun getTrueMonkey(
    string: String
): Int {
    val trimmed = string.trim()
    val withoutPrefix = trimmed.removePrefix(TRUE_MONKEY)
    return withoutPrefix.toInt()
}

private const val FALSE_MONKEY = "If false: throw to monkey "

fun getFalseMonkey(
    string: String
): Int {
    val trimmed = string.trim()
    val withoutPrefix = trimmed.removePrefix(FALSE_MONKEY)
    return withoutPrefix.toInt()
}

fun part2(input: List<String>): Long {
    val monkeys = getMonkeys(input)

    val greatestCommonMultiple = monkeys.map { it.divisor.toLong() }.reduce(WorryLevel::times)

    repeat(10000) {
        monkeys.forEach { monkey ->
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.pop()
                val updatedWorryLevel = monkey.operation(item) % greatestCommonMultiple
                ++monkey.numberOfInspections
                val recipient = if (updatedWorryLevel % monkey.divisor == 0L) {
                    monkeys[monkey.onTrueMonkeyIndex]
                } else monkeys[monkey.onFalseMonkeyIndex]
                recipient.items.add(updatedWorryLevel)
            }
        }
    }

    return monkeys
        .asSequence()
        .map { it.numberOfInspections.toLong() }
        .sortedDescending()
        .take(2)
        .reduce(Long::times)
}
