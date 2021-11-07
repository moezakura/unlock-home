package si.mox.unlock.services

class UrlService {
    companion object {
        const val baseURL = "https://unlock.mox.si/exec"

        fun accessURL(token: String): String {
            return "${baseURL}?token=${token}"
        }

        fun apiURL(token: String, isDoor: Boolean): String {
            return "${baseURL}?token=${token}&is_door=${isDoor}"
        }
    }
}