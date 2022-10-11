package com.clow.animation_case.weidgt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Created by clow
 * Des: 用于测试属性动画
 * Date: 2022/10/11.
 */
class CircleProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val mStrokeWidth = 20f
    private var mProgress  = 0f
    private val arcRectF = RectF()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = mStrokeWidth
    }

    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = mStrokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = Math.min(measuredWidth,measuredHeight)
        setMeasuredDimension(width,width)
    }

    /**
     *  提供progress的set方法 用于ObjectAnimator动画执行时调用
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        val radius  = width / 2f - mStrokeWidth
        canvas.drawCircle(centerX,centerY,radius ,mPaint)
        arcRectF.set(centerX - radius,centerY - radius,centerX + radius,centerY+radius)
        canvas.drawArc(arcRectF,0f,mProgress,false,mProgressPaint)
    }
}