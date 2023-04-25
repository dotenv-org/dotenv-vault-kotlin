package org.dotenv.vault

import io.github.cdimascio.dotenv.Configuration
import io.github.cdimascio.dotenv.Dotenv

class DotenvVault {
    lateinit var dotenvDelegate: Dotenv

    fun load() {
        Dotenv.load()
    }

    fun decrypt(): Boolean {
        return true
    }
}


fun dotenvVault(block: Configuration.() -> Unit = {}): DotenvVault {
    val config = Configuration()
    block(config)
    val dotenv = Dotenv
        .configure()
        .directory(config.directory)
        .filename(config.filename)
    if (config.ignoreIfMalformed) dotenv.ignoreIfMalformed()
    if (config.ignoreIfMissing) dotenv.ignoreIfMissing()
    if (config.systemProperties) dotenv.systemProperties()
    val delegate = dotenv.load()
    val vault = DotenvVault()
    vault.dotenvDelegate = delegate
    return vault
}
