package data

import kotlinx.serialization.Serializable
import parseUnicodeEscape

@Serializable
data class SharedContent(
    val link: String? = null,
    private val share_text: String? = null,
    val original_content_owner: String? = null
) {
    val shareText: String
        get() = parseUnicodeEscape(share_text ?: "")
}
