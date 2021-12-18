package com.compose.sultan.plagiarismchecker.service

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.toLowerCase
import com.asutosh.documentreader.FilePathHelper
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.extractor.WordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.math.min

object LevenshteinDistance {
    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    fun similarity(s1: String, s2: String): Double {
        var longer = s1
        var shorter = s2
        if (s1.length < s2.length) { // longer should always have greater length
            longer = s2
            shorter = s1
        }
        val longerLength = longer.length
        return if (longerLength == 0) {
            1.0 /* both strings are zero length */
        } else (longerLength - computeEditDistance(longer, shorter)) / longerLength.toDouble()
        /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
    }
    private fun computeEditDistance(str1: String, str2: String): Int {
        var s1 = str1
        var s2 = str2
        s1 = s1.lowercase(Locale("en"))
        s2 = s2.lowercase(Locale("en"))
        val costs = IntArray(s2.length + 1)
        for (i in 0..s1.length) {
            var lastValue = i
            for (j in 0..s2.length) {
                if (i == 0) {
                    costs[j] = j
                } else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (s1[i - 1] != s2[j - 1]) {
                            newValue = min(
                                min(newValue, lastValue),
                                costs[j]
                            ) + 1
                        }
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0) {
                costs[s2.length] = lastValue
            }
        }
        return costs[s2.length]
    }
//    fun printDistance(s1: String, s2: String) {
//        var s1 = s1
//        var s2 = s2
//        var similarityOfStrings = 0.0
//        var editDistance = 0
//        if (s1.length < s2.length) { // s1 should always be bigger
//            val swap = s1
//            s1 = s2
//            s2 = swap
//        }
//        val bigLen = s1.length
//        editDistance = computeEditDistance(s1, s2)
//        similarityOfStrings = if (bigLen == 0) {
//            1.0 /* both strings are zero length */
//        } else {
//            (bigLen - editDistance) / bigLen.toDouble()
//        }
//        //////////////////////////
//        //System.out.println(s1 + "-->" + s2 + ": " +
//        //      editDistance + " (" + similarityOfStrings + ")");
//        println("$editDistance ($similarityOfStrings)")
//    }

    fun readWordDocFromUri(uri: Uri?, context: Context?): List<String> {

        val file = File(context?.let { FilePathHelper(it).getPath(uri!!) }!!)
        val fullDocumentString: StringBuilder = StringBuilder()
        val fis = FileInputStream(file.absolutePath)
        var paragraphs: Array<String> = arrayOf()
        if (file.extension == "doc") {
            val doc = HWPFDocument(fis)
            val we = WordExtractor(doc)
            paragraphs = we.paragraphText

            return paragraphs.asList()

        } else if (file.extension == "docx") {
            val document = XWPFDocument(fis)
            return document.paragraphs.map { it.text }


        }else{
            return listOf("")
        }
    }

    fun readWordDocFromUriToString(uri: Uri?, context: Context?): String {

        val file = File(context?.let { FilePathHelper(it).getPath(uri!!) }!!)
        val fullDocumentString: StringBuilder = StringBuilder()
        val fis = FileInputStream(file.absolutePath)

        if (file.extension == "doc") {
            val doc = HWPFDocument(fis)
            val we = WordExtractor(doc)
            val paragraphs: Array<String> = we.paragraphText

            for (para in paragraphs) {
                fullDocumentString.append(para).append("\n")
            }
            fis.close()

        } else if (file.extension == "docx") {
            val document = XWPFDocument(fis)
            val paragraphs: List<XWPFParagraph> = document.paragraphs

            for (para in paragraphs) {
                fullDocumentString.append(para.text).append("\n")
            }
            fis.close()
        }
        return fullDocumentString.toString()
    }
}