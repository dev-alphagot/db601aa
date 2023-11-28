import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.InputStream

fun extractFile(inputStream: InputStream, destFilePath: String) {
    val bos = BufferedOutputStream(FileOutputStream(destFilePath))
    val bytesIn = ByteArray(BUFFER_SIZE)
    var read: Int
    while (inputStream.read(bytesIn).also { read = it } != -1) {
        bos.write(bytesIn, 0, read)
    }
    bos.close()
}

/**
 * Size of the buffer to read/write data
 */
private const val BUFFER_SIZE = 4096