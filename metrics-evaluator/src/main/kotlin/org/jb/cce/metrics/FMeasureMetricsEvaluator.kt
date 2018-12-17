package org.jb.cce.metrics

import org.jb.cce.Session

class FMeasureMetricsEvaluator {
    companion object {
        fun evaluate(sessions: List<Session>): Double {
            val precision = PrecisionMetricsEvaluator.evaluate(sessions)
            val recall = RecallMetricsEvaluator.evaluate(sessions)
            return 2 * precision * recall / (precision + recall)
        }
    }
}