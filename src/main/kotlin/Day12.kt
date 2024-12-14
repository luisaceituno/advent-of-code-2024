package dev.aceituno

fun main() = runWithInput(12) { input ->
    val map = input.matrix()

    val visited = mutableSetOf<Pos>()
    fun survey(type: Char, pos: Pos, region: Region) {
        if (pos in visited) return
        visited.add(pos)
        region.area += 1

        val left = map.at(pos.move(Dir.L))
        val right = map.at(pos.move(Dir.R))
        val up = map.at(pos.move(Dir.U))
        val down = map.at(pos.move(Dir.D))
        val upRight = map.at(pos.move(Dir.UR))
        val upLeft = map.at(pos.move(Dir.UL))
        val downRight = map.at(pos.move(Dir.DR))
        val downLeft = map.at(pos.move(Dir.DL))

        if (left == type && up == type && upLeft != type) region.sides += 1
        if (left == type && down == type && downLeft != type) region.sides += 1
        if (right == type && up == type && upRight != type) region.sides += 1
        if (right == type && down == type && downRight != type) region.sides += 1

        if (left != type && up != type) region.sides += 1
        if (left != type && down != type) region.sides += 1
        if (right != type && up != type) region.sides += 1
        if (right != type && down != type) region.sides += 1

        for (neighborPos in pos.cross()) {
            if (map.at(neighborPos) == type) {
                survey(type, neighborPos, region)
            } else {
                region.perimeter += 1
            }
        }
    }

    var part1 = 0L
    var part2 = 0L
    for ((type, pos) in map.eachWithPos()) {
        val region = Region()
        survey(type, pos, region)
        part1 += region.area * region.perimeter
        part2 += region.area * region.sides
    }

    println("Part 1: $part1")
    println("Part 2: $part2")
}

private data class Region(
    var area: Int = 0,
    var perimeter: Int = 0,
    var sides: Int = 0,
)
