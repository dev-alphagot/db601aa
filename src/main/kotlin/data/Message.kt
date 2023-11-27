package data

import kotlinx.serialization.Serializable
import parseUnicodeEscape

@Serializable
data class Message(
    private val sender_name: String,
    val timestamp_ms: Long,
    val is_geoblocked_for_viewer: Boolean,
    private val content: String? = null,
    val share: SharedContent? = null,
    val photos: List<Photo>? = null,
    val videos: List<Video>? = null,
    val reactions: List<Reaction>? = null,
    val call_duration: Int? = null
) {
    val senderName: String
        get() = parseUnicodeEscape(sender_name)

    val text: String?
        get() { return parseUnicodeEscape(content ?: return null) }
}
