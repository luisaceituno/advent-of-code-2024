package dev.aceituno

fun main() = runWithInput(8) { input ->
    val grid = input.lines().nonBlank().matrix()

    val antennasByFreq = mutableMapOf<Char, MutableSet<Pos>>()
    val basicAntinodes = mutableSetOf<Pos>()
    val harmonicAntinodes = mutableSetOf<Pos>()

    fun addNextHarmonics(start: Pos, jump: Pos) {
        val nextPos = start.add(jump)
        if (grid.hasPos(nextPos)) {
            harmonicAntinodes.add(nextPos)
            addNextHarmonics(nextPos, jump)
        }
    }

    for ((field, pos) in grid.eachWithPos()) {
        if (field == '.') continue
        val otherAntennas = antennasByFreq.getOrPut(field) { mutableSetOf() }
        for (otherAntenna in otherAntennas) {
            val distance = pos.deltaTo(otherAntenna)
            basicAntinodes.add(otherAntenna.add(distance))
            basicAntinodes.add(pos.add(distance.inverted()))

            val minifiedDistance = minifyDistance(distance)
            harmonicAntinodes.add(pos)
            addNextHarmonics(pos, minifiedDistance)
            addNextHarmonics(pos, minifiedDistance.inverted())
        }
        otherAntennas.add(pos)
    }

    println("Part 1: ${basicAntinodes.filter(grid::hasPos).size}")
    println("Part 2: ${harmonicAntinodes.size}")
}

fun minifyDistance(distance: Pos): Pos {
    val gcd = distance.y.toBigInteger().gcd(distance.x.toBigInteger())
    return Pos(distance.y / gcd.toInt(), distance.x / gcd.toInt())
}
