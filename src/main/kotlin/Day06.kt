package dev.aceituno

fun main() = runWithInput(6) { input ->
    val field = input.lines().nonBlank().matrix()
    val start = field.findPos { it == '^' } ?: error("No start position found")

    fun loopsIfObstructed(startPos: Pos, startDir: Dir): Boolean {
        val obstruction = startPos.move(startDir)
        var pos = startPos
        var dir = startDir
        val memories = mutableSetOf<Memory>()
        while (true) {
            val memory = Memory(pos, dir)
            if (!memories.add(memory)) return true
            val nextPos = pos.move(dir)
            val inFront = field.at(nextPos) ?: return false
            if (inFront == '#' || nextPos == obstruction) {
                dir = dir.turnRight()
            } else {
                pos = nextPos
            }
        }
    }

    val visited = mutableSetOf<Pos>()
    val obstructionSpots = mutableSetOf<Pos>()
    var dir = Dir.U
    var pos = start
    while (true) {
        val next = pos.move(dir)
        when (val fieldValue = field.at(next)) {
            '.' -> {
                if (next !in visited && loopsIfObstructed(pos, dir)) {
                    obstructionSpots += next
                }
                visited += pos
                pos = next
            }

            '^' -> {
                visited += pos
                pos = next
            }

            '#' -> {
                dir = dir.turnRight()
            }

            null -> {
                visited += pos
                break
            }

            else -> error("Invalid field value found: $fieldValue")
        }
    }

    println("Part 1: ${visited.size}")
    println("Part 2: ${obstructionSpots.size}")
}

data class Memory(val pos: Pos, val dir: Dir)
