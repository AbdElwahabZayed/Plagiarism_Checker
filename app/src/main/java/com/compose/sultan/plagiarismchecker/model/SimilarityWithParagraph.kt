package com.compose.sultan.plagiarismchecker.model


data class SimilarityWithParagraph(val paragraph: String, val ratio: Double, val fileName: String)

data class SimilarityWithFile(val ratio: Double, val fileName: String)
