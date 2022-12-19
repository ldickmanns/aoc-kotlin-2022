package day13

import readInput

fun main() {
    val input = readInput("day13/input")
//    val input = readInput("day13/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var index = 0

    return input.filter { it.isNotEmpty() }.chunked(2) {
        val leftPacket = parsePacket(it.first())
        val rightPacket = parsePacket(it.last())

        ++index
        if (areCorrectlyOrdered(leftPacket, rightPacket)!!) index else 0
    }.sum()
}

private fun parsePacket(packet: String): List<Any> {
    var currentList = ListWithParent(null)
    val contents = packet.removeSurrounding("[", suffix = "]")

    var priorDigit: Int? = null

    for (content in contents) when {
        content.isDigit() -> {
            val digit = content.digitToInt()
            priorDigit?.let {
                val newValue = it * 10 + digit
                currentList.items.removeLast()
                currentList.items.add(newValue)
                priorDigit = newValue
            } ?: run { priorDigit = digit }
            currentList.items.add(digit)
        }

        content == '[' -> {
            val newList = ListWithParent(currentList)
            currentList.items.add(newList.items)
            currentList = newList
            priorDigit = null
        }

        content == ']' -> {
            currentList = currentList.parent ?: error("No parent available")
            priorDigit = null
        }

        content == ',' -> {
            priorDigit = null
            continue
        }

        else -> error("Invalid input")
    }
    return currentList.items
}

private data class ListWithParent(
    val parent: ListWithParent?,
    val items: MutableList<Any> = mutableListOf(),
)

private fun areCorrectlyOrdered(left: Any, right: Any): Boolean? = when {
    left is Int && right is Int -> when {
        left < right -> true
        left > right -> false
        left == right -> null
        else -> error("Invalid input")
    }

    left is Int && right is List<*> -> areCorrectlyOrdered(listOf(left), right)
    left is List<*> && right is Int -> areCorrectlyOrdered(left, listOf(right))
    left is List<*> && right is List<*> -> when {
        left.isEmpty() and right.isEmpty() -> null
        left.isEmpty() and right.isNotEmpty() -> true
        left.isNotEmpty() and right.isEmpty() -> false
        left.isNotEmpty() and right.isNotEmpty() -> {
            val result = areCorrectlyOrdered(left.first()!!, right.first()!!)
            result ?: areCorrectlyOrdered(left.drop(1), right.drop(1))
        }

        else -> error("Invalid input")
    }

    else -> error("Invalid input")
}

fun part2(input: List<String>): Int = input
    .filter { it.isNotEmpty() }
    .toMutableList()
    .apply {
        add("[[2]]")
        add("[[6]]")
    }
    .map { parsePacket(it) }
    .sortedWith { o1, o2 -> areCorrectlyOrdered(o1, o2)?.let { if (it) -1 else 1 } ?: 0 }
    .foldIndexed(1) { index: Int, acc: Int, currentPacket: List<Any> ->
        if (currentPacket == listOf(listOf(2)) || currentPacket == listOf(listOf(6))) acc * (index + 1) else acc
    }
