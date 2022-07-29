package com.wilinz.i18ntranslator.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import java.io.InputStream

@OptIn(ExperimentalComposeUiApi::class)
object JavaResourceLoader : ResourceLoader {
    override fun load(resourcePath: String): InputStream {
        return this.javaClass.classLoader.getResourceAsStream(resourcePath)!!
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun javaPainterResource(resourcePath: String): Painter {
    return painterResource(resourcePath, JavaResourceLoader)
}