package org.dotenv.vault

import io.github.cdimascio.dotenv.internal.DotenvReader

class DotenvVaultReader(val content: String): DotenvReader(null, null) {

    override fun read(): List<String> {
        return content.lines()
    }
}