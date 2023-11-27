package data

import kotlinx.serialization.Serializable
import parseUnicodeEscape

@Serializable
data class Reaction(
    private val reaction: String,
    private val actor: String
) {
    val emoji: String
        get() = parseUnicodeEscape(reaction)

    val reactor: String
        get() = parseUnicodeEscape(actor)
}
