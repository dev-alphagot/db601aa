fun byteArrayWrapped(b: ByteArray): ByteArray {
    val ba = mutableListOf<Byte>()

    for(i in b.indices){
        val it = b[i]

        if(it == 0x5C.toByte() && (b[i + 1] == 117.toByte())) ba.add(0x5C.toByte())

        ba.add(it)
    }

    return ba.toByteArray()
}