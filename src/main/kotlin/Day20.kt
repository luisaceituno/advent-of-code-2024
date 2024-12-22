package dev.aceituno

fun main() = runWithInput(20) { Day20(it).run() }

data class Day20(val input: String) {
    val map = input.matrix()
    val goal = map.findPos { it == 'E' } ?: error("No goal pos")

    fun run() {
        val timesToGoal = mutableMapOf<Pos, Int>()
        val bigCheatsDeprecated = mutableMapOf<Pair<Pos, Pos>, Int>()
        val bigCheats = mutableMapOf<Pair<Pos, Pos>, Int>()
        var cur: Pos? = goal
        var time = 0
        while (cur != null) {
            for ((jumpPos, jumpTime) in timesToGoal) {
                val distance = jumpPos.manhattanTo(cur)
                if (distance <= 20) {
                    val timeSavings = time - (jumpTime + distance)
                    if (timeSavings >= 100) {
                        bigCheats.put(Pair(cur, jumpPos), timeSavings)
                        if (distance <= 2) {
                            bigCheatsDeprecated.put(Pair(cur, jumpPos), timeSavings)
                        }
                    }
                }
            }
            timesToGoal[cur] = time
            cur = cur.cross().find { map.at(it) != '#' && it !in timesToGoal }
            time++
        }

        println("Part 1: ${bigCheatsDeprecated.size}")
        println("Part 2: ${bigCheats.size}")
    }
}
