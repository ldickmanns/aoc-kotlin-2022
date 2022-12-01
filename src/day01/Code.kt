package day01

import readInput

fun main() {
    val input = readInput("day01/input")
    val inputAsInt = input.map {
        if (it.isEmpty()) null else it.toInt()
    }

    println(part1(inputAsInt))
    println(part2(inputAsInt))
}

fun part1(input: List<Int?>): Int {
    var max = Int.MIN_VALUE
    input.fold(0) { acc, curr ->
        curr?.let {
            acc + curr
        } ?: run {
            if (acc > max) max = acc
            0
        }
    }
    return max
}

fun part2(input: List<Int?>): Int {
    val maxes = mutableListOf(0, 0, 0)
    input.fold(0) { acc, curr ->
        curr?.let {
            acc + curr
        } ?: run {
            if (maxes.min() < acc) {
                maxes.removeFirst()
                maxes.add(acc)
                maxes.sort()
            }
            0
        }
    }
    println(maxes)
    return maxes.sum()
}
