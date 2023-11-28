import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.ApplicationScope
import java.awt.FileDialog
import java.io.File
import kotlin.system.exitProcess

fun openFileDialog(window: ComposeWindow, title: String, allowedExtensions: List<String>, allowMultiSelection: Boolean = false): File {
    return try {
        FileDialog(window, title, FileDialog.LOAD).apply {
            isMultipleMode = allowMultiSelection

            // windows
            file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

            // linux
            setFilenameFilter { _, name ->
                allowedExtensions.any {
                    name.endsWith(it)
                }
            }

            isVisible = true
        }.files.first()
    } catch(e: Exception){
        exitProcess(0)
    }
}