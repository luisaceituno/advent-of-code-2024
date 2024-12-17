package dev.aceituno

fun main() = runWithInput(16) { input ->
    val map = input.matrix()
    val start = map.findPos { it == 'S' } ?: throw IllegalArgumentException("unable to find start position")
    val goal = map.findPos { it == 'E' } ?: throw IllegalArgumentException("unable to find end position")

    val scores = mutableMapOf(Deer(start, Dir.R) to 0)
    val trails = mutableMapOf(Deer(start, Dir.R) to listOf(start))
    val next = ArrayDeque<Deer>().also { it.add(Deer(start, Dir.R)) }
    while (next.isNotEmpty()) {
        val deer = next.removeFirst()
        val score = scores.getValue(deer)
        val trail = trails.getValue(deer)

        for (deerTurned in listOf(deer.turnLeft(), deer.turnRight())) {
            val prevScore = scores[deerTurned] ?: Int.MAX_VALUE
            if (prevScore == score + 1000) {
                trails[deerTurned] = trails[deerTurned].orEmpty() + trail
            }
            if (prevScore > score + 1000) {
                scores[deerTurned] = score + 1000
                trails[deerTurned] = trail
                next.addLast(deerTurned)
            }
        }

        val deerForward = deer.advance()
        if (map.at(deerForward.pos) == '#') continue
        val prevScore = scores[deerForward] ?: Int.MAX_VALUE
        if (prevScore == score + 1) {
            trails[deerForward] = trails[deerForward].orEmpty() + trail
        }
        if (prevScore > score + 1) {
            scores[deerForward] = score + 1
            trails[deerForward] = trail + deerForward.pos
            next.addLast(deerForward)
        }
    }

    val part1 = scores.filterKeys { it.pos == goal }.values.min()
    val part2 = trails.getValue(scores.filterKeys { it.pos == goal }.minBy { it.value }.key).toSet().size
    println("Part 1: $part1")
    println("Part 2: $part2")
}

private data class Deer(val pos: Pos, val dir: Dir) {
    fun advance() = Deer(pos.move(dir), dir)
    fun turnLeft() = Deer(pos, dir.turnRight().turnRight().turnRight())
    fun turnRight() = Deer(pos, dir.turnRight())
}
