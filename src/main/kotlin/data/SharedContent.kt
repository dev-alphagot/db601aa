package data

import kotlinx.serialization.Serializable
import parseUnicodeEscape

@Serializable
data class SharedContent(
    val link: String,
    private val share_text: String,
    val original_content_owner: String
) {
    val shareText: String
        get() = parseUnicodeEscape(share_text)
}
