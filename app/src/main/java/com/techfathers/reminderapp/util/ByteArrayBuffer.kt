package com.techfathers.reminderapp.util

class ByteArrayBuffer(capacity: Int) {

    private var buffer: ByteArray
    private var len = 0

    init {
        require(capacity >= 0) { "Buffer capacity may not be negative" }
        buffer = ByteArray(capacity)
    }

    private fun expand(newlen: Int) {
        val newbuffer = ByteArray(Math.max(buffer.size shl 1, newlen))
        System.arraycopy(buffer, 0, newbuffer, 0, len)
        buffer = newbuffer
    }

    fun append(b: ByteArray?, off: Int, len: Int) {
        if (b == null) {
            return
        }
        if (off < 0 || off > b.size || len < 0 ||
                off + len < 0 || off + len > b.size) {
            throw IndexOutOfBoundsException()
        }
        if (len == 0) {
            return
        }
        val newlen = this.len + len
        if (newlen > buffer.size) {
            expand(newlen)
        }
        System.arraycopy(b, off, buffer, this.len, len)
        this.len = newlen
    }

    fun append(b: Int) {
        val newlen = len + 1
        if (newlen > buffer.size) {
            expand(newlen)
        }
        buffer[len] = b.toByte()
        len = newlen
    }

    fun append(b: CharArray?, off: Int, len: Int) {
        if (b == null) {
            return
        }
        if (off < 0 || off > b.size || len < 0 ||
                off + len < 0 || off + len > b.size) {
            throw IndexOutOfBoundsException()
        }
        if (len == 0) {
            return
        }
        val oldlen = this.len
        val newlen = oldlen + len
        if (newlen > buffer.size) {
            expand(newlen)
        }
        var i1 = off
        var i2 = oldlen
        while (i2 < newlen) {
            buffer[i2] = b[i1].toByte()
            i1++
            i2++
        }
        this.len = newlen
    }


    fun clear() {
        len = 0
    }

    fun toByteArray(): ByteArray? {
        val b = ByteArray(len)
        if (len > 0) {
            System.arraycopy(buffer, 0, b, 0, len)
        }
        return b
    }

    fun byteAt(i: Int): Int {
        return buffer[i].toInt()
    }

    fun capacity(): Int {
        return buffer.size
    }

    fun length(): Int {
        return len
    }

    fun buffer(): ByteArray? {
        return buffer
    }

    fun setLength(len: Int) {
        if (len < 0 || len > buffer.size) {
            throw IndexOutOfBoundsException()
        }
        this.len = len
    }

    fun isEmpty(): Boolean {
        return len == 0
    }

    fun isFull(): Boolean {
        return len == buffer.size
    }
}