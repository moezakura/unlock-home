package si.mox.unlock.storage

import android.content.Context
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import si.mox.unlock.models.Secret
import java.io.BufferedReader
import java.io.File

class SecretStorage {
    private val fileName = "secret.json"

    fun save(context: Context, v: Secret) {
        val data = Json.encodeToString(v)
        context.openFileOutput(this.fileName, Context.MODE_PRIVATE).use {
            it.write(data.toByteArray())
        }
    }

    fun load(context: Context): Secret? {
        val readFile = File(context.filesDir, this.fileName)
        if (!readFile.exists()) {
            return null
        }
        val contents = readFile.bufferedReader().use(BufferedReader::readText)
        return Json.decodeFromString<Secret>(contents)
    }
}