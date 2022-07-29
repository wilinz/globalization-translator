package com.wilinz.i18ntranslator.translator.engine

/**
 * 计算tk值.
 */
fun getGoogleToken(text: String): String {
    val a = text.toByteArray().map { it.toUByte().toShort() }

    val d = 406644L
    val e = 3293161072L
    var f = 406644L
    for (h in a) {
        f += h
        f = calculate(f, "+-a^+6")
    }
    f = calculate(f, "+-3^+b+-f")
    f = f xor e
    if (0 > f) {
        f = (f and Int.MAX_VALUE.toLong()) + Int.MAX_VALUE.toLong() + 1
    }
    f = (f % 1E6).toLong()

    return "$f.${f xor d}"
}

private fun calculate(a: Long, b: String): Long {
    var g = a
    for (c in 0..b.length - 2 step 3) {
        val d = b[c + 2]
        val e = if ('a' <= d) (d - 87).toInt() else d.toString().toInt()
        val f = if ('+' == b[c + 1]) g.ushr(e) else g shl e
        g = if ('+' == b[c]) g + f and (Int.MAX_VALUE.toLong() * 2 + 1) else g xor f
    }

    return g
}