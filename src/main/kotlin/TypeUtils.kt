package dev.aceituno

data class Pos(val y: Int, val x: Int) {
    fun move(dir: Dir) = Pos(y + dir.y, x + dir.x)
}

enum class Dir(val y: Int, val x: Int) {
    R(0, 1),
    UR(1, 1),
    U(1, 0),
    UL(1, -1),
    L(0, -1),
    DL(-1, -1),
    D(-1, 0),
    DR(-1, 1)
}

fun <T> List<List<T>>.at(y: Int, x: Int) = getOrNull(y)?.getOrNull(x)
fun <T> List<List<T>>.at(pos: Pos) = at(pos.y, pos.x)
fun <T> List<List<T>>.coords() = indices.flatMap { y -> get(y).indices.map { x -> Pos(y, x) } }

fun List<String>.nonBlank() = filter { it.isNotBlank() }
fun List<String>.matrix() = map { it.toList() }