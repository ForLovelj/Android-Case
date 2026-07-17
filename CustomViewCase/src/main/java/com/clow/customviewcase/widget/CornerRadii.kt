package com.clow.customviewcase.widget

/**
 * 四个角的圆角半径，单位为像素 px。
 */
data class CornerRadii(
    val topLeft: Float = 0f,
    val topRight: Float = 0f,
    val bottomRight: Float = 0f,
    val bottomLeft: Float = 0f
) {

    internal val hasRoundedCorners: Boolean
        get() = topLeft > 0f || topRight > 0f || bottomRight > 0f || bottomLeft > 0f

    internal val uniformRadius: Float?
        get() = if (
            topRight == topLeft &&
            bottomRight == topLeft &&
            bottomLeft == topLeft
        ) {
            topLeft
        } else {
            null
        }

    internal fun copyInto(target: FloatArray) {
        target[0] = topLeft
        target[1] = topLeft
        target[2] = topRight
        target[3] = topRight
        target[4] = bottomRight
        target[5] = bottomRight
        target[6] = bottomLeft
        target[7] = bottomLeft
    }

    companion object {
        val ZERO = CornerRadii()

        fun all(radius: Float) = CornerRadii(
            topLeft = radius,
            topRight = radius,
            bottomRight = radius,
            bottomLeft = radius
        )
    }
}

private fun Float.isValidRadius() = isFinite() && this >= 0f
