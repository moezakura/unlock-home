package si.mox.unlock.models

import kotlinx.serialization.*

@Serializable
data class Secret (
    val apiKey: String
)