package dev.aceituno

fun main() = runWithInput(23) { Day23(it).run() }

data class Day23(val input: String) {
    fun run() {
        val connections = mutableMapOf<String, MutableSet<String>>()
        var biggestParty = setOf<String>()
        var triples = 0
        input.lines().asSequence().map { it.split("-") }.forEach { (l, r) ->
            val connectionsL = connections.getOrPut(l) { mutableSetOf(l) }
            val connectionsR = connections.getOrPut(r) { mutableSetOf(r) }
            val intersection = connectionsL.intersect(connectionsR)
            if (intersection.isNotEmpty()) {
                triples += if (l.startsWith('t') || r.startsWith('t')) {
                    intersection.size
                } else {
                    intersection.filter { it.startsWith('t') }.size
                }
            }
            connectionsL.add(r)
            connectionsR.add(l)
            val maybeParty = (intersection + l + r)
            if (maybeParty.all { connections[it]?.containsAll(intersection) == true }) {
                if (maybeParty.size > biggestParty.size) {
                    biggestParty = maybeParty
                }
            }
        }
        println("Part 1: ${triples}")
        println("Part 2: ${biggestParty.sorted().joinToString(",")}")
    }
}
