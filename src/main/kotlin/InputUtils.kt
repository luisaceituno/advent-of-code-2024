package dev.aceituno

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import kotlin.time.measureTime

fun runWithInput(day: Int, block: (input: String) -> Unit) {
    val inputForDay = readStringFromCacheFile("$day.txt")
        ?: getInputFromWebsite(day)?.also { writeStringToCacheFile("$day.txt", it) }
        ?: getInputFromStdin()
    val runtime = measureTime { block(inputForDay) }
    println("Took ${runtime.inWholeMilliseconds} ms")
}

private fun readStringFromCacheFile(fileName: String): String? = File("cache/${fileName}").let {
    if (it.exists()) it.readText()
    else null
}

private fun writeStringToCacheFile(fileName: String, content: String) {
    File("cache/${fileName}").let {
        if (!it.exists()) {
            Files.createDirectories(it.toPath().parent)
        }
        it.writeText(content)
    }
}

private fun getInputFromWebsite(day: Int): String? {
    println("Input not found in cache, fetching...")
    val sessionId = System.getenv("AOC_SESSION_ID")
    if (sessionId == null) {
        println("Please copy/paste the value of the advent of code website session cookie into the environment variable AOC_SESSION_ID so I can grab your input for you!")
        return null
    }
    val requestForInput = HttpRequest.newBuilder()
        .uri(URI("https://adventofcode.com/2024/day/${day}/input"))
        .header("Cookie", "session=$sessionId")
        .build()
    val inputResponse = HttpClient.newHttpClient().send(requestForInput, HttpResponse.BodyHandlers.ofString())
    return inputResponse.body().trim()
}

fun getInputFromStdin(): String {
    println("Please enter the input (end with Ctrl/Cmd + D):")
    val input = StringBuilder()
    while (true) {
        val line = readlnOrNull() ?: break
        input.appendLine(line)
    }
    return input.toString().trim()
}
