import data.MessageChannel
import kotlinx.serialization.decodeFromString

fun parseToMessageChannel(s: String): MessageChannel {
    return json.decodeFromString<MessageChannel>(s)
}