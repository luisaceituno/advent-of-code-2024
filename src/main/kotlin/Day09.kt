package dev.aceituno

fun main() = runWithInput(9) { input ->
    val blocks = input.trim().map { it.digitToInt() }.mapIndexed { index, size ->
        if (index % 2 == 0) Block.File(index / 2, size)
        else Block.Space(size)
    }

    println("Part 1: ${calculateChecksum(blocks.toMutableList().compressedWithFragmentation())}")
    println("Part 2: ${calculateChecksum(blocks.toMutableList().compressedWithoutFragmentation())}")
}

private sealed interface Block {
    data class File(val index: Int, val size: Int) : Block
    data class Space(val size: Int) : Block {
        fun fillWith(file: File): Triple<File?, Space?, File?> {
            if (file.size == 0) return Triple(null, this, null)
            if (file.size < this.size) return Triple(file, copy(size = this.size - file.size), null)
            if (file.size == this.size) return Triple(file, null, null)
            return Triple(file.copy(size = this.size), null, file.copy(size = file.size - this.size))
        }
    }
}

private fun MutableList<Block>.compressedWithFragmentation(): MutableList<Block> {
    fun removeLastFile(): Block.File = when (val last = this.removeLast()) {
        is Block.Space -> removeLastFile()
        is Block.File -> last
    }

    for (index in 0..Int.MAX_VALUE) {
        if (index >= this.size - 1) break
        val block = this[index]
        if (block is Block.Space) {
            val moveTarget = removeLastFile()
            val (filePart1, remainingSpace, filePart2) = block.fillWith(moveTarget)
            val toInsert = listOfNotNull(filePart1, remainingSpace)
            this.removeAt(index)
            this.addAll(index, toInsert)
            filePart2?.let(this::add)
        }
    }

    return this
}

private fun MutableList<Block>.compressedWithoutFragmentation(): MutableList<Block> {
    for (block in this.reversed()) {
        if (block is Block.File) {
            for (i in this.indices) {
                val candidate = this[i]
                if (candidate == block) break
                if (candidate is Block.Space && candidate.size >= block.size) {
                    val originalIndex = this.indexOf(block)
                    val (_, remaining) = candidate.fillWith(block)
                    this[originalIndex] = Block.Space(block.size)
                    this[i] = block
                    remaining?.let { this.add(i + 1, it) }
                    break
                }
            }
        }
    }
    return this
}

private fun calculateChecksum(blocks: List<Block>): Long {
    var checksum = 0L
    var virtualIndex = 0
    for (block in blocks) {
        when (block) {
            is Block.Space -> virtualIndex += block.size
            is Block.File -> {
                for (i in virtualIndex until virtualIndex + block.size) checksum += i * block.index
                virtualIndex += block.size
            }
        }
    }
    return checksum
}
