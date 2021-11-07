package si.mox.unlock.api

import android.util.Log
import si.mox.unlock.services.UrlService
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class Client(private val token: String) {

    fun unlock(isDoor: Boolean = true): Result<String> {
        val url = URL(UrlService.apiURL(this.token, isDoor))

        (url.openConnection() as? HttpURLConnection)?.run {
            try {
                connectTimeout = 10 * 1000
                readTimeout = 10 * 1000

                requestMethod = "GET"
                connect()

                val content = inputStream.bufferedReader().use(BufferedReader::readText)
                return Result.success(content)
            } catch (exception: Exception) {
                Log.e("Error", exception.toString())
            } finally {
                disconnect()
            }
        }

        return Result.failure(Exception("Cannot open HttpURLConnection"))
    }
}