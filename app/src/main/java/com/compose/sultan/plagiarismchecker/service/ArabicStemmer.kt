package com.compose.sultan.plagiarismchecker.service

import com.haroldadmin.lucilla.pipeline.PipelineStep
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM
import kotlin.streams.asSequence

class ArabicStemmer(
    algorithm: ALGORITHM = ALGORITHM.ARABIC,
    repeat: Int = 1
) : PipelineStep {
    private val stemmer = opennlp.tools.stemmer.snowball.SnowballStemmer(algorithm, repeat)

    override fun process(input: List<String>): List<String> {
        return input.parallelStream().map { i -> stemmer.stem(i).toString() }.asSequence().toList()
    }
}