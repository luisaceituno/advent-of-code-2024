package dev.aceituno

fun main() = runWithInput(14) { input ->
    val robots = input.lines().map {
        val (x, y, vx, vy) = it.ints()
        Robot(Pos(y, x), Pos(vy, vx))
    }

    val quadrants = mutableListOf(0, 0, 0, 0)
    val maxY = 103
    val maxX = 101
    for ((startPos, vel) in robots) {
        var curPos = startPos
        repeat(100) { curPos = curPos.addWrapping(vel, maxY, maxX) }
        when {
            curPos.y < maxY / 2 -> when {
                curPos.x < maxX / 2 -> quadrants[0]++
                curPos.x > maxX / 2 -> quadrants[1]++
            }

            curPos.y > maxY / 2 -> when {
                curPos.x < maxX / 2 -> quadrants[2]++
                curPos.x > maxX / 2 -> quadrants[3]++
            }
        }
    }

    val part1 = quadrants.reduce(Int::times)
    println("Part 1: $part1")

    var formation = robots
    for (seconds in 1..999999) {
        formation = formation.map { Robot(it.pos.addWrapping(it.vel, maxY, maxX), it.vel) }
        val positions = formation.mapTo(mutableSetOf()) { it.pos }
        if (positions.size != 500) continue
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                if (Pos(y, x) in positions) print('★')
                else print(' ')
            }
            println()
        }
        println("Part 2: $seconds")
        break
    }
}

private data class Robot(val pos: Pos, val vel: Pos)
