package org.jb.cce.metrics

import org.jb.cce.Session
import java.util.stream.Collectors

class PrecisionMetricsEvaluator {
    companion object {
        fun evaluate(sessions: List<Session>): Double {
            // top3
            val listOfCompletions = sessions.stream()
                    .flatMap { compl -> compl.lookups.stream() }
                    .collect(Collectors.toList())
            val recommendationsMadeCount = listOfCompletions.stream()
                    .filter { l -> !l.isEmpty() }
                    .count()

            val listOfRealCompletions = sessions.stream()
                    .flatMap { compl -> compl.completions.stream() }
                    .collect(Collectors.toList())

            assert(listOfCompletions.size == listOfRealCompletions.size)
            var relevantRecommendationsCount = 0
            var completionIndex = 0
            for (realCompletion in listOfRealCompletions) {
                val indexOfNecessaryCompletion = listOfCompletions[completionIndex++].indexOf(realCompletion)
                if (indexOfNecessaryCompletion in 0..3) {
                    relevantRecommendationsCount++
                }
            }
            return relevantRecommendationsCount.toDouble() / recommendationsMadeCount
        }
    }
}