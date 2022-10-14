package com.clow.animation_case.weidgt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.clow.animation_case.ui.data.ProgressData
import com.clow.baselib.ext.dp
import com.clow.baselib.ext.dpf

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
    private var mProgressData: ProgressData? = null
    private val arcRectF = RectF()
    private var mType = 0 //0: mProgress 1: mProgressData

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

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = (mStrokeWidth/2).dpf
        color = Color.BLUE
        textAlign = Paint.Align.CENTER
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
        mType = 0
        invalidate()
    }

    /**
     * 用于自定义TypeEvaluator测试
     */
    fun setProgressData(progressData: ProgressData) {
        mProgressData = progressData
        mType = 1
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        val radius  = width / 2f - mStrokeWidth
        //画底部圆环
        canvas.drawCircle(centerX,centerY,radius ,mPaint)

        //画进度圆环
        arcRectF.set(centerX - radius,centerY - radius,centerX + radius,centerY+radius)
        if (mType == 1) {
            mProgressPaint.color = mProgressData!!.color
            mTextPaint.color = mProgressData!!.color

            canvas.drawArc(arcRectF,0f,mProgressData!!.progress*3.6f,false,mProgressPaint)

            canvas.drawText( "${mProgressData!!.progress.toInt()}%", centerX,
                centerY - (mTextPaint.ascent() + mTextPaint.descent()) / 2, mTextPaint)
        } else {
            mProgressPaint.color = Color.BLUE
            mTextPaint.color = Color.BLUE

            canvas.drawArc(arcRectF,0f,mProgress*3.6f,false,mProgressPaint)

            canvas.drawText( "${mProgress.toInt()}%", centerX,
                centerY - (mTextPaint.ascent() + mTextPaint.descent()) / 2, mTextPaint)
        }


    }
}