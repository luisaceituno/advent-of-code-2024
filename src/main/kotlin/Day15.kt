package dev.aceituno

fun main() = runWithInput(15) { input ->
    val (mapSpec, movesSpec) = input.split("\n\n")
    val map = mapSpec.matrix()
    val moves = movesSpec.mapNotNull(Dir::from)

    fun solveFattening(fatness: Int = 1): Int {
        val walls = mutableSetOf<FatPos>()
        val boxes = mutableSetOf<FatPos>()
        var robot = FatPos(0, 0..0)
        for ((field, pos) in map.eachWithPos()) when (field) {
            '#' -> walls.add(FatPos(pos.y, pos.x * fatness until pos.x * fatness + fatness))
            'O' -> boxes.add(FatPos(pos.y, pos.x * fatness until pos.x * fatness + fatness))
            '@' -> robot = FatPos(pos.y, pos.x * fatness until pos.x * fatness + 1)
        }

        fun moveRobot(dir: Dir) {
            val nextRobotPos = robot.move(dir)
            if (walls.any(nextRobotPos::overlaps)) return

            val removedBoxes = mutableSetOf<FatPos>()
            val confirmedBoxes = mutableSetOf<FatPos>()
            val pendingBoxes = ArrayDeque<FatPos>()

            boxes.firstOrNull(nextRobotPos::overlaps)?.let(pendingBoxes::add)

            while (pendingBoxes.isNotEmpty()) {
                val box = pendingBoxes.removeLast()
                boxes.remove(box)
                removedBoxes.add(box)
                val nextBoxPos = box.move(dir)
                if (walls.any(nextBoxPos::overlaps)) {
                    boxes.addAll(removedBoxes)
                    return
                }
                boxes.filter { it.overlaps(nextBoxPos) }.forEach(pendingBoxes::add)
                confirmedBoxes.add(nextBoxPos)
            }

            boxes.addAll(confirmedBoxes)
            robot = nextRobotPos
        }
        for (dir in moves) moveRobot(dir)
        return boxes.fold(0) { acc, box -> acc + 100 * box.y + box.x.first }
    }

    println("Part 1: ${solveFattening()}")
    println("Part 2: ${solveFattening(2)}")
}

private data class FatPos(
    val y: Int,
    val x: IntRange
) {
    fun overlaps(other: FatPos) = other.y == this.y && (other.x.first in x || other.x.last in x)
    fun move(dir: Dir) = FatPos(y + dir.y, IntRange(x.first + dir.x, x.last + dir.x))
}
