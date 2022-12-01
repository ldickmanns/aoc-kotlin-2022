package aoc2021.day03

import readInput

fun main() {
    val input = readInput("aoc2021/day03/input")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val nColumns = input.first().length
    val oneCounters = IntArray(nColumns)
    input.forEach { line: String ->
        line.forEachIndexed { index: Int, c: Char ->
            if (c == '1') oneCounters[index] += 1
        }
    }
    val gammaRateString = buildString {
        oneCounters.forEach {
            if (it > 500) append("1") else append("0")
        }
    }
    val epsilonRateString = buildString {
        oneCounters.forEach {
            if (it > 500) append("0") else append("1")
        }
    }
    val gammaRate = Integer.parseInt(gammaRateString, 2)
    val epsilonRate = Integer.parseInt(epsilonRateString, 2)
    return gammaRate * epsilonRate
}

fun part2(input: List<String>): Int {
    val nColumns = input.first().length
    val zeroCounter = IntArray(nColumns)
    val oneCounter = IntArray(nColumns)
    input.forEach { line ->
        line.forEachIndexed { index: Int, c: Char ->
            if (c == '0') zeroCounter[index] += 1 else oneCounter[index] += 1
        }
    }

    var oxygenArray = input
    var co2Array = input

    for (i in 0 until nColumns) {
        val nZerosOxygen = oxygenArray.map { it[i] }.filter { it == '0' }.size
        val nOnesOxygen = oxygenArray.size - nZerosOxygen
        val nZerosCO2 = co2Array.map { it[i] }.filter { it == '0' }.size
        val nOnesCO2 = co2Array.size - nZerosCO2


        val hasMoreOrEqualOnes = nOnesOxygen >= nZerosOxygen
        oxygenArray = if (oxygenArray.size > 1) oxygenArray.filter {
            if (hasMoreOrEqualOnes) it[i] == '1' else it[i] == '0'
        } else oxygenArray

        val hasLessOrEqualZeros = nZerosCO2 <= nOnesCO2
        co2Array = if (co2Array.size > 1) co2Array.filter {
            if (hasLessOrEqualZeros) it[i] == '0' else it[i] == '1'
        } else co2Array
    }

    println(oxygenArray.size)
    println(co2Array.size)

    val oxygenRating = Integer.parseInt(oxygenArray.first(), 2)
    val co2Rating = Integer.parseInt(co2Array.first(), 2)

    return oxygenRating * co2Rating
}
