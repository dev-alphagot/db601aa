package data

import kotlinx.serialization.Serializable

@Serializable
data class JoinableMode(
    val mode: Int,
    val link: String
)
