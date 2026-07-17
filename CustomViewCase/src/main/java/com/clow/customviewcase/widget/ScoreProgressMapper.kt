package com.clow.customviewcase.widget

/**
 * 将实际分数映射为非线性的视觉进度。
 *
 * 以 150 分为基准：
 * - 0 到 40 分，每增加 10 分，高度权重增加 1.0
 * - 40 到 90 分，每增加 10 分，高度权重增加 1.2
 * - 90 到 150 分，每增加 10 分，高度权重增加 1.5
 */
object ScoreProgressMapper {

    fun toProgress(score: Float, maxScore: Float): Float {
        require(maxScore.isFinite() && maxScore > 0f) {
            "满分必须是大于 0 的有限数值"
        }
        require(score.isFinite()) {
            "分数必须是有限数值"
        }

        val clampedScore = score.coerceIn(0f, maxScore)
        val benchmarkScore = clampedScore * BENCHMARK_MAX_SCORE / maxScore
        return weightedHeight(benchmarkScore) / BENCHMARK_TOTAL_HEIGHT
    }

    private fun weightedHeight(benchmarkScore: Float): Float {
        val firstStageScore = benchmarkScore.coerceAtMost(FIRST_STAGE_END)
        val secondStageScore =
            (benchmarkScore - FIRST_STAGE_END).coerceIn(0f, SECOND_STAGE_LENGTH)
        val thirdStageScore = (benchmarkScore - SECOND_STAGE_END).coerceAtLeast(0f)

        return firstStageScore / SCORE_STEP * FIRST_STAGE_WEIGHT +
            secondStageScore / SCORE_STEP * SECOND_STAGE_WEIGHT +
            thirdStageScore / SCORE_STEP * THIRD_STAGE_WEIGHT
    }

    private const val BENCHMARK_MAX_SCORE = 150f
    private const val FIRST_STAGE_END = 40f
    private const val SECOND_STAGE_END = 90f
    private const val SECOND_STAGE_LENGTH = SECOND_STAGE_END - FIRST_STAGE_END
    private const val SCORE_STEP = 10f

    private const val FIRST_STAGE_WEIGHT = 1.0f
    private const val SECOND_STAGE_WEIGHT = 1.2f
    private const val THIRD_STAGE_WEIGHT = 1.5f

    private const val BENCHMARK_TOTAL_HEIGHT = 19f
}
