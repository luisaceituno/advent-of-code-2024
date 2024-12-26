package dev.aceituno

fun main() = runWithInput(24) { Day24(it).run() }

data class Day24(val input: String) {
    val initialWires = input.substringBefore("\n\n").lines().associate {
        val (name, value) = it.split(": ")
        name to (value != "0")
    }
    val gates = input.substringAfter("\n\n").lines().map {
        val (i1, op, i2, _, out) = it.split(" ")
        Gate(op, i1, i2, out)
    }

    data class Gate(val op: String, val in1: String, val in2: String, val out: String) {
        override fun toString(): String {
            return "$in1 $op $in2 -> $out"
        }
    }

    fun run() {
        val wires = initialWires.toMutableMap()
        val dependencies = mutableMapOf<String, MutableList<Gate>>()
        val toEvaluate = ArrayDeque<Gate>()
        for (gate in gates) {
            dependencies.getOrPut(gate.in1) { mutableListOf() }.add(gate)
            dependencies.getOrPut(gate.in2) { mutableListOf() }.add(gate)
            if (gate.in1 in wires && gate.in2 in wires) toEvaluate.addLast(gate)
        }
        fun setWire(wire: String, value: Boolean) {
            if (wires.putIfAbsent(wire, value) == null) {
                toEvaluate.addAll(dependencies[wire].orEmpty())
            }
        }
        while (toEvaluate.isNotEmpty()) {
            val gate = toEvaluate.removeFirst()
            if (gate.out in wires) continue
            val in1 = wires[gate.in1]
            val in2 = wires[gate.in2]
            if (in1 == null || in2 == null) continue
            when (gate.op) {
                "AND" -> setWire(gate.out, in1 && in2)
                "OR" -> setWire(gate.out, in1 || in2)
                "XOR" -> setWire(gate.out, in1 xor in2)
            }
        }

        println("Part 1: ${extractLongByPrefix(wires, 'z')}")
//        println("Part 2: ${}")
    }

    private fun extractLongByPrefix(wires: MutableMap<String, Boolean>, prefix: Char): Long = wires.keys
        .filter { it[0] == prefix }
        .sortedDescending()
        .map { if (wires[it] == true) '1' else '0' }
        .joinToString("")
        .toLong(2)
}
