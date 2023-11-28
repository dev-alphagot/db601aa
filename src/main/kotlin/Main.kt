import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.MessageChannel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipFile
import kotlin.io.path.createTempDirectory
import kotlin.io.path.pathString

lateinit var json: Json

lateinit var channels: List<MessageChannel>

@Composable
fun app() {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    var textW by remember { mutableStateOf("") }

    var isClicked by mutableStateOf(false)

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(channels[selectedIndex].titleText, color = Color.White,  modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(
                Color.Black))
            DropdownMenu(
                modifier = Modifier.align(
                    Alignment.TopStart
                ).fillMaxWidth(0.9f),
                expanded = expanded, onDismissRequest = { expanded = false }
            ) {
                channels.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false

                        val text = s.messages.sortedBy { it.timestamp_ms }.joinToString("\n\n"){ msg ->
                            "${
                                SimpleDateFormat("[yyyy-MM-dd a hh:mm:ss]").format(
                                    Date(msg.timestamp_ms + (0L * 60 * 60 * 1000))
                                )
                            } ${msg.senderName}: ${
                                if(msg.videos != null) "(영상 ${msg.videos.size}개: \n${
                                    msg.videos.joinToString("\n - "){
                                        "(파일: ${it.uri.split("/").last()}, 올린 시간: ${
                                            SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(
                                                Date(((it.creation_timestamp ?: 0L) * 1000) + (0L * 60 * 60 * 1000))
                                            )
                                        })"
                                    }
                                }\n)"
                                else if(msg.photos != null) "(사진 ${msg.photos.size}개: \n${
                                    msg.photos.joinToString("\n - "){
                                        "(파일: ${it.uri.split("/").last()}, 올린 시간: ${
                                            SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(
                                                Date(((it.creation_timestamp ?: 0) * 1000) + (0L * 60 * 60 * 1000))
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

                        textW = text

                        File("log/${s.titleText}.txt").let { file ->
                            if(!file.canWrite()){
                                if(!file.exists()) file.createNewFile()
                                else return@let
                            }

                            file.writeText(
                                text
                            )
                        }
                    }) {
                        Text(text = s.titleText)
                    }
                }
            }

            Button(modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxWidth(0.1f)
                .height(20.dp),
                contentPadding = PaddingValues(
                    start = 4.dp,
                    top = 4.dp,
                    end = 4.dp,
                    bottom = 4.dp,
                ),
                onClick = {
                channels.parallelStream().forEach { s ->
                    File("log/${s.titleText}.txt").let { file ->
                        if(!file.canWrite()){
                            if(!file.exists()) file.createNewFile()
                            else return@let
                        }

                        file.writeText(
                            s.messages.sortedBy { it.timestamp_ms }.joinToString("\n\n"){ msg ->
                                "${
                                    SimpleDateFormat("[yyyy-MM-dd a hh:mm:ss]").format(
                                        Date(msg.timestamp_ms + (0L * 60 * 60 * 1000))
                                    )
                                } ${msg.senderName}: ${
                                    if(msg.videos != null) "(영상 ${msg.videos.size}개: \n${
                                        msg.videos.joinToString("\n - "){
                                            "(파일: ${it.uri.split("/").last()}, 올린 시간: ${
                                                SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(
                                                    Date(((it.creation_timestamp ?: 0L) * 1000) + (0L * 60 * 60 * 1000))
                                                )
                                            })"
                                        }
                                    }\n)"
                                    else if(msg.photos != null) "(사진 ${msg.photos.size}개: \n${
                                        msg.photos.joinToString("\n - "){
                                            "(파일: ${it.uri.split("/").last()}, 올린 시간: ${
                                                SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(
                                                    Date(((it.creation_timestamp ?: 0) * 1000) + (0L * 60 * 60 * 1000))
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
                        )
                    }

                    isClicked = true
                }
            }){
                Text(text = "전부 저장", modifier = Modifier.fillMaxSize(), fontSize = TextUnit(10.0f, TextUnitType.Sp), textAlign = TextAlign.Center)
            }

            Text(
                text = textW,
                modifier = Modifier
                    .offset(0.dp, 20.dp)
                    .verticalScroll(rememberScrollState())
            )
        }

        if(isClicked){
            Dialog(onDismissRequest = { isClicked = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "대화 내역이 전부 저장되었습니다.\n(어두운 부분을 눌러 창 닫기)",
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun main() = application {
    json = Json {
        explicitNulls = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    val winTitle by remember { mutableStateOf("인스타그램 대화내역 아카이브 변환기") }

    Window(title = winTitle, onCloseRequest = ::exitApplication) {
        if(!::channels.isInitialized){
            channels = openFileDialog(
                window,"인스타그램 계정 정보 아카이브 파일을 선택해주세요", listOf(".zip")
            ).run {
                val tempDir = createTempDirectory("db601aa").toAbsolutePath().pathString

                ZipFile(this).use { zip ->
                    zip.entries().asSequence().forEach { entry ->
                        zip.getInputStream(entry).use { input ->
                            val filePath = tempDir + File.separator + entry.name

                            if (!entry.isDirectory) {
                                // if the entry is a file, extracts it
                                extractFile(input, filePath)
                            } else {
                                // if the entry is a directory, make the directory
                                val dir = File(filePath)
                                dir.mkdir()
                            }
                        }
                    }
                }

                val fms = (File(tempDir, "messages/inbox").listFiles() ?: return@Window).toList().parallelStream()
                    .filter { it.isDirectory }
                    .filter {
                        it.list()
                            ?.any {
                                (it ?: "").split(File.separator).last() == "message_1.json"
                            } ?: false
                    }.map { File(it, "message_1.json") }.toList()

                File(tempDir).deleteOnExit()

                fms
            }.map {
                parseToMessageChannel(byteArrayWrapped(it.readBytes()).decodeToString())
            }
        }
        else {
            app()
        }
    }
}
