package com.wilinz.globalization.translator.network

import com.intellij.openapi.util.SystemInfo

object HttpData {

    private const val GOOGLE_REFERER = "https://translate.google.com/"

    private const val CHROME_VERSION = "98.0.4758.102"
    private const val EDGE_VERSION = "98.0.1108.62"

    @Suppress("SpellCheckingInspection")
    val userAgent: String
        get() {
            val arch = System.getProperty("os.arch")
            val is64Bit = arch != null && "64" in arch
            val systemInformation =
                when {
                    SystemInfo.isWindows -> {
                        "Windows NT ${SystemInfo.OS_VERSION}${if (is64Bit) "; Win64; x64" else ""}"
                    }
                    SystemInfo.isMac -> {
                        val parts = SystemInfo.OS_VERSION.split('.').toMutableList()
                        if (parts.size < 3) {
                            parts.add("0")
                        }
                        "Macintosh; Intel Mac OS X ${parts.joinToString("_")}"
                    }
                    else -> "X11; Linux x86${if (is64Bit) "_64" else ""}"
                }

            return "Mozilla/5.0 ($systemInformation) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$CHROME_VERSION Safari/537.36 Edg/$EDGE_VERSION"
        }

}