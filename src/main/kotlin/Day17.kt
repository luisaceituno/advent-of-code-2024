package dev.aceituno

fun main() = runWithInput(17) { input ->
    val lines = input.lines()
    val initialA = lines[0].longs()[0]
    val initialB = lines[1].longs()[0]
    val initialC = lines[2].longs()[0]
    val program = lines[4].ints()

    // got inspired from reddit for this one - I'm not smart enough :)
    fun findMagicA(currentA: Long = 0): Long? =
        (currentA..currentA + 7).firstNotNullOfOrNull { a ->
            val output = runProgram(program, a).toList()
            if (program.takeLast(output.size) == output) {
                if (program == output) a else findMagicA(maxOf(a shl 3, 8))
            } else {
                null
            }
        }

    println("Part 1: ${runProgram(program, initialA, initialB, initialC).joinToString(",")}")
    println("Part 2: ${findMagicA()}")
}

fun runProgram(
    instructions: List<Int>,
    initA: Long,
    initB: Long = 0,
    initC: Long = 0,
) = sequence<Int> {
    var pointer = 0
    var workingA = initA
    var workingB = initB
    var workingC = initC

    fun combo(operand: Int) = when (operand) {
        0, 1, 2, 3 -> operand.toLong()
        4 -> workingA
        5 -> workingB
        6 -> workingC
        else -> throw IllegalArgumentException("unknown combo operand $operand")
    }

    while (pointer in instructions.indices) {
        val operand = instructions[pointer + 1]
        when (instructions[pointer]) {
            0 -> workingA = workingA shr combo(operand).toInt()
            1 -> workingB = workingB xor operand.toLong()
            2 -> workingB = combo(operand) % 8
            3 -> if (workingA != 0L) {
                pointer = operand
                continue
            }

            4 -> workingB = workingB xor workingC
            5 -> yield((combo(operand) % 8).toInt())
            6 -> workingB = workingA shr combo(operand).toInt()
            7 -> workingC = workingA shr combo(operand).toInt()
        }
        pointer += 2
    }
}
