package com.clow.customviewcase.base

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


/**
 * Created by clow
 * Des:
 * Date: 2022/12/19.
 */
abstract class BaseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    // 坐标画笔
    private var mCoordinatePaint: Paint? = null

    // 网格画笔
    private var mGridPaint: Paint? = null

    // 写字画笔
    private var mTextPaint: Paint? = null

    // 坐标颜色
    private var mCoordinateColor = 0
    private var mGridColor = 0

    // 网格宽度 50px
    private val mGridWidth = 50f

    // 坐标线宽度
    private val mCoordinateLineWidth = 2.5f

    // 网格宽度
    private val mGridLineWidth = 1f

    // 字体大小
    private var mTextSize = 0f

    // 标柱的高度
    private val mCoordinateFlagHeight = 8f

    private var mWidth = 0f
    private var mHeight = 0f

    protected abstract fun init(context: Context)

    private fun initCoordinate(context: Context) {
        mCoordinateColor = Color.BLACK
        mGridColor = Color.LTGRAY
        mTextSize = spToPx(10f).toFloat()
        mCoordinatePaint = Paint().apply {
            setAntiAlias(true)
            setColor(mCoordinateColor)
            setStrokeWidth(mCoordinateLineWidth)
        }
        mGridPaint = Paint().apply {
            setAntiAlias(true)
            setColor(mGridColor)
            setStrokeWidth(mGridLineWidth)
        }
        mTextPaint = Paint().apply {
            setAntiAlias(true)
            setColor(mCoordinateColor)
            setTextAlign(Paint.Align.CENTER)
            setTextSize(mTextSize)
        }
    }

    init {
        initCoordinate(context)
        init(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
    }




    /**
     * 画坐标和网格，以画布中心点为原点
     *
     * @param canvas 画布
     */
    protected open fun drawCoordinate(canvas: Canvas) {
        val halfWidth = mWidth / 2
        val halfHeight = mHeight / 2

        // 画网格
        canvas.save()
        canvas.translate(halfWidth, halfHeight)
        var curWidth = mGridWidth
        // 画横线
        while (curWidth < halfWidth + mGridWidth) {

            // 向右画
            canvas.drawLine(curWidth, -halfHeight, curWidth, halfHeight, mGridPaint!!)
            // 向左画
            canvas.drawLine(-curWidth, -halfHeight, -curWidth, halfHeight, mGridPaint!!)

            // 画标柱
            canvas.drawLine(curWidth, 0f, curWidth, -mCoordinateFlagHeight, mCoordinatePaint!!)
            canvas.drawLine(-curWidth, 0f, -curWidth, -mCoordinateFlagHeight, mCoordinatePaint!!)

            // 标柱宽度（每两个画一个）
            if (curWidth.toInt() % (mGridWidth * 2).toInt() == 0) {
                canvas.drawText("$curWidth", curWidth, mTextSize * 1.5f, mTextPaint!!)
                canvas.drawText("${-curWidth}", -curWidth, mTextSize * 1.5f, mTextPaint!!)
            }
            curWidth += mGridWidth
        }
        var curHeight = mGridWidth
        // 画竖线
        while (curHeight < halfHeight + mGridWidth) {

            // 向右画
            canvas.drawLine(-halfWidth, curHeight, halfWidth, curHeight, mGridPaint!!)
            // 向左画
            canvas.drawLine(-halfWidth, -curHeight, halfWidth, -curHeight, mGridPaint!!)

            // 画标柱
            canvas.drawLine(0f, curHeight, mCoordinateFlagHeight, curHeight, mCoordinatePaint!!)
            canvas.drawLine(0f, -curHeight, mCoordinateFlagHeight, -curHeight, mCoordinatePaint!!)

            // 标柱宽度（每两个画一个）
            if (curHeight.toInt() % (mGridWidth * 2).toInt() == 0) {
                canvas.drawText(
                    "$curHeight",
                    -mTextSize * 2,
                    curHeight + mTextSize / 2,
                    mTextPaint!!
                )
                canvas.drawText(
                    "${-curHeight}",
                    -mTextSize * 2,
                    -curHeight + mTextSize / 2,
                    mTextPaint!!
                )
            }
            curHeight += mGridWidth
        }
        canvas.restore()

        // 画 x，y 轴
        canvas.drawLine(halfWidth, 0f, halfWidth, mHeight, mCoordinatePaint!!)
        canvas.drawLine(0f, halfHeight, mWidth, halfHeight, mCoordinatePaint!!)
    }

    /**
     * 转换 sp 至 px
     *
     * @param spValue sp值
     * @return px值
     */
    protected open fun spToPx(spValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected open fun dpToPx(dpValue: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (dpValue * metrics.density + 0.5f).toInt()
    }
}