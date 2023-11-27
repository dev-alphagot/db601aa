fun byteArrayWrapped(b: ByteArray): ByteArray {
    val ba = mutableListOf<Byte>()

    b.forEach {
        if(it == 0x5C.toByte()) ba.add(0x5C.toByte())

        ba.add(it)
    }

    return ba.toByteArray()
}