package com.compose.sultan.plagiarismchecker.utils

import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream

fun readFromFile(file: File): List<String> {
    val list = mutableListOf("")
    return try {
        (FileInputStream(file)).use { inputStream ->
            val document = XWPFDocument(inputStream)
            return list.apply {
                addAll(document.paragraphs.map { it.text })
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        list
        // Handle the exception
    }
}