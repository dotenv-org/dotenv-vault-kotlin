package org.dotenv.vault

import org.dotenv.vault.utilities.fromHexString
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class VaultCryptoTest {
    @Test
    fun verifyEncryptionDecryption() {
        val key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        val message = "HELLO"

        val secretKey = createKeyFromBytes(key.fromHexString())

        val encryptedMessage = encrypt(secretKey, message)
        val decryptedMessage = decrypt(secretKey, encryptedMessage)

        assertEquals(message, decryptedMessage)
    }

    /**
     *  $cat .env_test.keys
     *  # DOTENV_KEYs (generated with npx dotenv-vault local build)
     *   DOTENV_KEY_DEVELOPMENT="dotenv://:key_14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6@dotenv.local/vault/.env_test.vault?environment=development"
     *   decryption key is: 14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6
     *
     *  $cat .env_test.vault
     *   # .env_test.vault (generated with npx dotenv-vault local build)
     *   DOTENV_VAULT_DEVELOPMENT="BuEEZbR/YQKR2Rj/bHvOQbv+gtIxPoik1BCqpvIdqmv/lhLr5PAKs0r9iIZ4dBWNlQE7WdQ8vak00cQuEV5/tmRUkN1g/HFcRuJMN5slL+xuUzK5YJo1XYRwsdHvMG2BUGST"
     */
    @Test
    fun verifyDecryptionOfVaultWithActualKey() {
        val originalDotEnvContent = "# Good test EVs\n" +
                "MY_TEST_EV1=\"my test ev 1\"\n" +
                "MY_TEST_EV2=\"my test ev 2\"\n\n"

        val key = "14968ef8b3f56cbcfcaa83197efa34dba567e715e82ee69e050258a5522100f6"
        val encryptedMessage =
            "BuEEZbR/YQKR2Rj/bHvOQbv+gtIxPoik1BCqpvIdqmv/lhLr5PAKs0r9iIZ4dBWNlQE7WdQ8vak00cQuEV5/tmRUkN1g/HFcRuJMN5slL+xuUzK5YJo1XYRwsdHvMG2BUGST"

        val secretKey = createKeyFromBytes(key.fromHexString())
        println("encryptedMessage ${encryptedMessage}")

        val decodedEncryptedMessage = Base64.getDecoder().decode(encryptedMessage)
        val decryptedMessage = decrypt(secretKey, decodedEncryptedMessage)
        println("decryptedMessage ${decryptedMessage}")
        assertEquals(originalDotEnvContent, decryptedMessage)
    }
}
