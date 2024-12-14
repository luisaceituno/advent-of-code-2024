package dev.aceituno

data class Pos(val y: Int, val x: Int) {
    fun move(dir: Dir) = Pos(y + dir.y, x + dir.x)
    fun deltaTo(other: Pos) = Pos(other.y - y, other.x - x)
    fun add(other: Pos) = Pos(other.y + y, other.x + x)
    fun inverted() = Pos(-y, -x)
    fun cross() = listOf(move(Dir.L), move(Dir.R), move(Dir.U), move(Dir.D))
}

enum class Dir(val y: Int, val x: Int) {
    R(0, 1),
    UR(-1, 1),
    U(-1, 0),
    UL(-1, -1),
    L(0, -1),
    DL(1, -1),
    D(1, 0),
    DR(1, 1);

    fun turnRight() = when (this) {
        R -> D
        UR -> DR
        U -> R
        UL -> UR
        L -> U
        DL -> UL
        D -> L
        DR -> DL
    }
}

fun <T> List<List<T>>.at(y: Int, x: Int) = getOrNull(y)?.getOrNull(x)
fun <T> List<List<T>>.at(pos: Pos) = at(pos.y, pos.x)
fun <T> List<List<T>>.hasPos(y: Int, x: Int) = y in indices && x in get(y).indices
fun <T> List<List<T>>.hasPos(pos: Pos) = hasPos(pos.y, pos.x)
fun <T> List<List<T>>.positions() = indices.flatMap { y -> get(y).indices.map { x -> Pos(y, x) } }
fun <T> List<List<T>>.eachWithPos() = indices.flatMap { y -> get(y).mapIndexed { x, t -> Pair(t, Pos(y, x)) } }
fun <T> List<List<T>>.findPos(predicate: (el: T) -> Boolean): Pos? {
    for (y in indices) {
        for (x in get(y).indices) {
            if (predicate(this[y][x])) {
                return Pos(y, x)
            }
        }
    }
    return null
}

fun String.chunkedByEmptyLine() = trim().split("\n\n").map(String::lines)
fun String.matrix() = lines().matrix()
fun List<String>.nonBlank() = filter { it.isNotBlank() }
fun List<String>.matrix() = map { it.toList() }
fun <T> List<String>.matrix(fieldMapper: (Char) -> T) = map { it.map(fieldMapper) }
