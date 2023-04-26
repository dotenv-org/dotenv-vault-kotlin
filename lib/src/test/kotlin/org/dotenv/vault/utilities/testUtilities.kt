package org.dotenv.vault.utilities

import io.github.cdimascio.dotenv.Dotenv

fun Dotenv.printEntries() {
    for (e in entries()) {
        println("${e.key} = ${e.value}")
    }
}