package com.dvir.portwarden.service

import java.io.File

import javax.swing.filechooser.FileSystemView

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import java.io.ByteArrayOutputStream

import java.util.Base64
import javax.swing.ImageIcon


class IconExtractor {

    fun getIcon(path: String): String? {

        val file = File(path)
        val icon = FileSystemView.getFileSystemView().getSystemIcon(file, 32, 32) as? ImageIcon ?: return null
        val image = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()
        icon.paintIcon(null, g, 0 , 0)
        g.dispose()
        var hasVisiblePixel = false

        for (x in 0 until image.width){
            for (y in 0 until image.height){

                if (x < 16){

                    continue

                }

                val pixel = image.getRGB(x, y)

                if (pixel shr 24 != 0){

                    hasVisiblePixel = true

                }

            }

        }

        if (!hasVisiblePixel){

            return null

        }
        val stream = ByteArrayOutputStream()
        ImageIO.write(image, "png", stream)
        val bytes = stream.toByteArray()
        val encodedBytes = Base64.getEncoder().encodeToString(bytes)

        return "data:image/png;base64,$encodedBytes"

    }

}