package data

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val uri: String,
    val creation_timestamp: Long? = null
)
