package dev.aceituno

fun main() = runWithInput(18) { Day18(it, 71).run() }

data class Day18(
    val input: String,
    val fieldSize: Int
) {
    val fallingBytes = input.lines().map { it.split(",").map(String::toInt) }

    fun run() {
        val part1 = calculateShortestPathAtNanos(1024)
        val part2 = binarySearchFirstBlocker().joinToString(",")
        println("Part 1: $part1")
        println("Part 2: $part2")
    }

    fun calculateShortestPathAtNanos(nanos: Int): Int? {
        val maxSteps = fieldSize * fieldSize
        var map = List(fieldSize) { MutableList(fieldSize) { maxSteps } }
        map[0][0] = 0
        fallingBytes.take(nanos).forEach { (x, y) -> map[y][x] = -1 }
        val next = ArrayDeque(listOf(Pos(0, 0)))
        while (next.isNotEmpty()) {
            val curPos = next.removeFirst()
            val nextValue = (map.at(curPos) ?: 0) + 1
            for (nextPos in curPos.cross()) {
                if ((map.at(nextPos) ?: -1) > nextValue) {
                    next.addLast(nextPos)
                    map[nextPos.y][nextPos.x] = nextValue
                }
            }
        }
        val stepsToGoal = map[fieldSize - 1][fieldSize - 1]
        return if (stepsToGoal < maxSteps) stepsToGoal else null
    }

    fun binarySearchFirstBlocker(): List<Int> {
        var floor = 0
        var ceil = fallingBytes.size
        while (floor < ceil) {
            val mid = (ceil + floor) / 2
            val midValue = calculateShortestPathAtNanos(mid)
            if (midValue == null) {
                ceil = mid
            } else {
                floor = mid + 1
            }
        }
        return fallingBytes[floor - 1]
    }
}
