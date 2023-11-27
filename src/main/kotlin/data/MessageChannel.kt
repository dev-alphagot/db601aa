package data

import kotlinx.serialization.Serializable
import parseUnicodeEscape

@Serializable
data class MessageChannel(
    val participants: List<Participant>,
    val messages: List<Message>,
    private val title: String,
    val is_still_participant: Boolean,
    val thread_path: String,
    private val magic_words: List<String>,
    val joinable_mode: JoinableMode? = null
) {
    val titleText: String
        get() = parseUnicodeEscape(title)

    val magicWords: List<String>
        get() = magic_words.map { parseUnicodeEscape(it) }
}
