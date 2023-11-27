package data

import kotlinx.serialization.Serializable
import parseUnicodeEscape

@Serializable
data class Participant(
    private val name: String
) {
    val nameText: String
        get() = parseUnicodeEscape(name)
}
