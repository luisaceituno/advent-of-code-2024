package dev.aceituno

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files

fun runWithInput(day: Int, block: (input: String) -> Unit) {
    val inputForDay = readStringFromCacheFile("$day.txt") ?: getInputFromWebsite(day).also {
        writeStringToCacheFile("$day.txt", it)
    }
    block(inputForDay)
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

private fun getInputFromWebsite(day: Int): String {
    println("Input not found in cache, fetching...")
    val sessionId = System.getenv("AOC_SESSION_ID")
        ?: error("Please copy/paste the value of the advent of code website session cookie into the environment variable AOC_SESSION_ID")
    val requestForInput = HttpRequest.newBuilder()
        .uri(URI("https://adventofcode.com/2024/day/${day}/input"))
        .header("Cookie", "session=$sessionId")
        .build()
    val inputResponse = HttpClient.newHttpClient().send(requestForInput, HttpResponse.BodyHandlers.ofString())
    return inputResponse.body()
}
