package org.jb.cce.completion

import org.jb.cce.Session
import org.jb.cce.actions.Action
import org.jb.cce.actions.PrintText
import java.util.stream.Collectors

class CompletionEvaluator(private val actions: List<Action>, private val completions: List<Session>) {

    fun getPrecisionMetric() : Double {
        // top3
        val listOfCompletions = completions.stream()
                                    .flatMap { compl -> compl.lookups.stream() }
                                    .collect(Collectors.toList())
        val recommendationsMadeCount = listOfCompletions.stream()
                                    .filter { l -> !l.isEmpty() }
                                    .count()

        var relevantRecommendationsCount = 0
        var completionIndex = 0
        for (action in actions) {
            if (action is PrintText) {
                val indexOfNecessaryCompletion = listOfCompletions[completionIndex++].indexOf(action.text)
                if (indexOfNecessaryCompletion in 0..3) {
                    relevantRecommendationsCount++
                }
            }
        }
        return relevantRecommendationsCount.toDouble() / recommendationsMadeCount
    }
    fun getRecallMetric() : Double {
        val listOfCompletions = completions.stream()
                .flatMap { compl -> compl.lookups.stream() }
                .collect(Collectors.toList())
        val recommendationsMadeCount = listOfCompletions.stream()
                .filter { l -> !l.isEmpty() }
                .count()
        return recommendationsMadeCount.toDouble() / listOfCompletions.size
    }
    fun getFMeasureMetric() : Double {
        val precision = getPrecisionMetric()
        val recall = getRecallMetric()
        return 2 * precision * recall / (precision + recall)
    }
}