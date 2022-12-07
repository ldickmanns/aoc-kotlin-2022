package day07

import readInput

fun main() {
    val input = readInput("day07/input")
//    val input = readInput("day07/input_test")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val fileTree = parseToTree(input)
    return sumSizes(fileTree, 0)
}

sealed interface Node {
    val name: String
    val size: Int
    val parent: Directory?
}

data class Directory(
    override val name: String,
    override val parent: Directory?,
    val children: MutableList<Node> = mutableListOf(),
) : Node {
    override val size: Int
        get() = children.sumOf { it.size }
}

data class File(
    override val name: String,
    override val size: Int,
    override val parent: Directory?,
) : Node

private fun parseToTree(input: List<String>): Node {
    val rootNode = Directory("/", null)
    var currentDirectory = rootNode

    input.forEach { line ->
        when {
            line.startsWith("$ cd ") -> {
                val destination = line.substringAfter("$ cd ")
                currentDirectory = when (destination) {
                    "/" -> rootNode
                    ".." -> currentDirectory.parent ?: rootNode
                    else -> currentDirectory.children.find { it.name == destination } as Directory
                }
            }

            line.startsWith("$ ls") -> {}

            line.startsWith("dir ") -> {
                val name = line.substringAfter("dir ")
                currentDirectory.children.add(Directory(name, currentDirectory))
            }

            else -> {
                val parts = line.split(" ")
                val size = parts.first().toInt()
                val name = parts.last()
                currentDirectory.children.add(File(name, size, currentDirectory))
            }
        }
    }

    return rootNode
}

fun sumSizes(node: Node, sizeSum: Int): Int = when (node) {
    is File -> 0
    is Directory -> {
        var newSizeSum = if (node.size <= 100_000) sizeSum + node.size else sizeSum
        node.children.forEach {
            newSizeSum += sumSizes(it, 0)
        }
        newSizeSum
    }
}

fun part2(input: List<String>): Int {
    val fileTree = parseToTree(input)
    val totalSize = 70_000_000
    val requiredForUpdate = 30_000_000
    val totalOccupied = fileTree.size
    val free = totalSize - totalOccupied
    val atLeastDelete = requiredForUpdate - free

    return findCandidate(
        node = fileTree,
        currentCandidate = totalOccupied,
        atLeastDelete = atLeastDelete,
    )
}

fun findCandidate(
    node: Node,
    currentCandidate: Int,
    atLeastDelete: Int,
): Int {
    when (node) {
        is File -> return 0
        is Directory -> {
            val nodeSize = node.size

            if (nodeSize < atLeastDelete) return currentCandidate

            var newCandidate = currentCandidate
            if (nodeSize in atLeastDelete until currentCandidate) newCandidate = nodeSize
            node.children.forEach {
                val childSize = findCandidate(it, newCandidate, atLeastDelete)
                if (childSize in atLeastDelete until newCandidate) newCandidate = childSize
            }
            return newCandidate
        }
    }
}


