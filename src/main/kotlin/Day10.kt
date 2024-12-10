package dev.aceituno

fun main() = runWithInput(10) { input ->
    val map = input.lines().matrix(Char::digitToInt)

    fun findReachableNinesFrom(height: Int, pos: Pos): Set<Pos> {
        if (height == 9) return setOf(pos)
        return pos.cross().fold(emptySet()) { acc, next ->
            if (map.at(next) == height + 1) acc + findReachableNinesFrom(height + 1, next)
            else acc
        }
    }

    fun countReachedNinesFrom(height: Int, pos: Pos): Int {
        if (height == 9) return 1
        return pos.cross().fold(0) { acc, next ->
            if (map.at(next) == height + 1) acc + countReachedNinesFrom(height + 1, next)
            else acc
        }
    }

    var sumScores = 0
    var sumRatings = 0
    for ((height, pos) in map.eachWithPos()) {
        if (height != 0) continue
        sumScores += findReachableNinesFrom(0, pos).size
        sumRatings += countReachedNinesFrom(0, pos)
    }

    println("Part 1: $sumScores")
    println("Part 2: $sumRatings")
}
