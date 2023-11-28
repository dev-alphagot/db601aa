import java.text.Normalizer

fun parseUnicodeEscape(s: String): String {
    val sBuffer = StringBuilder()

    var uniBuffer = 0

    val cs = s.toCharArray()

    var i = 0

    while(i < cs.size){
        val c = cs[i]

        if(c == '\\'){
            if(cs[i + 4] >= 'c'){
                if(uniBuffer != 0){
                    sBuffer.append(uniBuffer.toBigInteger().toByteArray().decodeToString())
                    uniBuffer = 0
                }
            }

            uniBuffer = uniBuffer shl 8
            uniBuffer = uniBuffer or (cs[i + 4].digitToInt(16) shl 4) or (cs[i + 5].digitToInt(16))
            i += 6
        }
        else {
            if(uniBuffer != 0){
                sBuffer.append(uniBuffer.toBigInteger().toByteArray().decodeToString())
                uniBuffer = 0
            }

            sBuffer.append(c)
            i++
        }
    }

    if(uniBuffer != 0){
        sBuffer.append(uniBuffer.toBigInteger().toByteArray().decodeToString())
    }

    return Normalizer.normalize(sBuffer.toString().replace(0.toChar().toString(), ""), Normalizer.Form.NFC)
}