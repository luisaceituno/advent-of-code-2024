package dev.aceituno

fun main() = runWithInput(4) { input ->
    val field = input.lines().nonBlank().matrix()

    var count = 0
    fun search(pos: Pos, dir: Dir, remaining: String): Int {
        if (remaining.isEmpty()) return 1
        if (field.at(pos) != remaining[0]) return 0
        return search(pos.move(dir), dir, remaining.drop(1))
    }
    for (pos in field.coords()) {
        if (field.at(pos) == 'X') {
            count += Dir.entries.sumOf { dir -> search(pos.move(dir), dir, "MAS") }
        }
    }
    println("Part 1: $count")

    var countCrossed = 0
    val combos = listOf("MS", "SM")
    for (pos in field.coords()) {
        if (field.at(pos) == 'A') {
            val ur = field.at(pos.move(Dir.UR))
            val dl = field.at(pos.move(Dir.DL))
            val ul = field.at(pos.move(Dir.UL))
            val dr = field.at(pos.move(Dir.DR))
            if ("$ur$dl" in combos && "$ul$dr" in combos) {
                countCrossed += 1
            }
        }
    }
    println("Part 2: $countCrossed")
}
