package org.dotenv.vault

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry

class DotenvVaultImpl: Dotenv {
    override fun entries(): Set<DotenvEntry> {
        TODO("Not yet implemented")
    }

    override fun entries(filter: Dotenv.Filter): Set<DotenvEntry> {
        TODO("Not yet implemented")
    }

    override fun get(key: String): String {
        TODO("Not yet implemented")
    }

    override fun get(key: String, defaultValue: String): String {
        TODO("Not yet implemented")
    }
}