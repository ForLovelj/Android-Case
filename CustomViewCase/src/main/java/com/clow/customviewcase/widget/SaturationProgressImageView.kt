package com.clow.customviewcase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Created by clow
 * Des: 进度裁剪
 * Date: 2026/7/17.
 */
class SaturationProgressImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SaturationImageView(context, attrs, defStyleAttr) {

    enum class ProgressDirection {
        LEFT_TO_RIGHT, // 从左到右
        RIGHT_TO_LEFT, // 从右到左
        TOP_TO_BOTTOM, // 从上到下
        BOTTOM_TO_TOP  // 从下到上
    }

    // 整体圆角裁剪路径
    private val clipPath = Path()
    private val viewRect = RectF()
    private val progressRect = RectF()
    private val cornerRadiiArray = FloatArray(CORNER_RADII_SIZE)
    private var hasRoundedCorners = false
    private val grayscalePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        colorFilter = ColorMatrixColorFilter(
            ColorMatrix().apply { setSaturation(0.0f) }
        )
    }

    /**
     * 当前进度：0.0f 到 1.0f
     */
    var progress: Float = 0.0f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            invalidate()
        }

    /**
     * 进度方向，默认为从左到右
     */
    var direction: ProgressDirection = ProgressDirection.LEFT_TO_RIGHT
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 是否绘制完整的黑白底图，默认不绘制。
     */
    var showGrayscaleBackground: Boolean = false
        set(value) {
            if (field == value) return

            field = value
            invalidate()
        }

    /**
     * 四个角使用相同的圆角半径（单位：像素 px）。
     * 设置该属性会覆盖 [cornerRadii]。
     */
    var cornerRadius: Float
        get() = cornerRadii.uniformRadius ?: 0f
        set(value) {
            val radius = value.coerceAtLeast(0f)
            cornerRadii = CornerRadii.all(radius)
        }

    /**
     * 四个角各自的圆角半径。
     */
    var cornerRadii: CornerRadii = CornerRadii.ZERO
        set(value) {
            field = value
            value.copyInto(cornerRadiiArray)
            hasRoundedCorners = value.hasRoundedCorners
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) return

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        viewRect.set(0f, 0f, viewWidth, viewHeight)

        if (showGrayscaleBackground) {
            drawGrayscaleBackground(canvas)
        }

        if (progress <= 0f) return

        updateProgressRect(viewWidth, viewHeight)
        val progressSave = canvas.save()
        clipRoundedBounds(canvas)
        clipProgressBounds(canvas)
        super.onDraw(canvas)
        canvas.restoreToCount(progressSave)
    }

    private fun drawGrayscaleBackground(canvas: Canvas) {
        val bgSave = canvas.save()
        clipRoundedBounds(canvas)
        val grayscaleLayer = canvas.saveLayer(viewRect, grayscalePaint)
        super.onDraw(canvas)
        canvas.restoreToCount(grayscaleLayer)
        canvas.restoreToCount(bgSave)
    }

    private fun updateProgressRect(viewWidth: Float, viewHeight: Float) {
        when (direction) {
            ProgressDirection.LEFT_TO_RIGHT -> {
                progressRect.set(0f, 0f, viewWidth * progress, viewHeight)
            }
            ProgressDirection.RIGHT_TO_LEFT -> {
                progressRect.set(viewWidth * (1f - progress), 0f, viewWidth, viewHeight)
            }
            ProgressDirection.TOP_TO_BOTTOM -> {
                progressRect.set(0f, 0f, viewWidth, viewHeight * progress)
            }
            ProgressDirection.BOTTOM_TO_TOP -> {
                progressRect.set(0f, viewHeight * (1f - progress), viewWidth, viewHeight)
            }
        }
    }

    private fun clipRoundedBounds(canvas: Canvas) {
        if (!hasRoundedCorners) return

        clipRoundedRect(canvas, viewRect)
    }

    private fun clipProgressBounds(canvas: Canvas) {
        if (hasRoundedCorners) {
            clipRoundedRect(canvas, progressRect)
        } else {
            canvas.clipRect(progressRect)
        }
    }

    private fun clipRoundedRect(canvas: Canvas, rect: RectF) {
        clipPath.reset()
        clipPath.addRoundRect(rect, cornerRadiiArray, Path.Direction.CW)
        canvas.clipPath(clipPath)
    }

    private companion object {
        const val CORNER_RADII_SIZE = 8
    }
}
