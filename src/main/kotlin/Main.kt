import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.MessageChannel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

lateinit var json: Json

lateinit var channel: MessageChannel

@Composable
@Preview
fun app() {
    val text = channel.messages.sortedBy { it.timestamp_ms }.joinToString("\n\n"){ msg ->
        "${
            SimpleDateFormat("[yyyy-MM-dd a hh:mm:ss]").format(
                Date(msg.timestamp_ms + (0L * 60 * 60 * 1000))
            )
        } ${msg.senderName}: ${
            if(msg.videos != null) "(영상 ${msg.videos.size}개: \n${
                msg.videos.joinToString("\n - "){
                    "(파일: ${it.uri.split("/").last()}, 올린 시간: ${
                        SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(
                            Date((it.creation_timestamp * 1000) + (0L * 60 * 60 * 1000))
                        )
                    })"
                }
            }\n)"
            else if(msg.photos != null) "(사진 ${msg.photos.size}개: \n${
                msg.photos.joinToString("\n - "){ 
                    "(파일: ${it.uri.split("/").last()}, 올린 시간: ${
                        SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(
                            Date((it.creation_timestamp * 1000) + (0L * 60 * 60 * 1000))
                        )
                    })" 
                }
            }\n)"
            else if(msg.share != null) "(공유한 컨텐츠: ${msg.share.link})"
            else "${
                if(msg.text != null) msg.text
                else "(메시지 내용 없음)"
            }"
        }${
            if(msg.call_duration != null) " (${msg.call_duration.timeText}간 통화) "
            else ""
        }${
            if(msg.reactions != null) "\n (${msg.reactions.joinToString(", "){ "${it.reactor}: ${it.emoji}" }})"
            else ""
        }"
    }

    MaterialTheme {
        Text(
            text = text,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )
    }

    File("log/${channel.titleText}.txt").let { file ->
        if(!file.canWrite()){
            if(!file.exists()) file.createNewFile()
            else return@let
        }

        file.writeText(
            text
        )
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun main() = application {
    json = Json {
        explicitNulls = true
        isLenient = true
    }

    channel = parseToMessageChannel(
            byteArrayWrapped(File(
                "C:\\Users\\AlphaGot\\Documents\\db601aa\\src\\main\\resources\\message_1.json"
            ).readBytes()).decodeToString()
    )

    val winTitle by remember { mutableStateOf("대화 기록 - ${channel.titleText}") }

    Window(title = winTitle, onCloseRequest = ::exitApplication) {
        app()
    }
}
