package data

import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val uri: String,
    val creation_timestamp: Long? = null
)
