package com.clow.customviewcase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by clow
 * Des: 线性渐变
 * Date: 2023/2/4.
 */
class LinearGradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mTextPaint: TextPaint? = null
    private lateinit var mLinearGradient: LinearGradient
    private var deltax = 20
    private var mTranslate = 0f
    private val mMatrix = Matrix()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //拿到TextView的paint
        mTextPaint = paint
        val text = text.toString()
        //测量文本宽度
        val textWidth = mTextPaint!!.measureText(text)
        val gradientSize = textWidth/text.length * 3

        //从左边-gradientSize开始，即左边距离文字gradientSize开始渐变
        mLinearGradient = LinearGradient(-gradientSize,0f,0f,0f, intArrayOf(0x22ffffff,
            0xffffffff.toInt(), 0x22ffffff),null,Shader.TileMode.CLAMP)
        mTextPaint!!.shader = mLinearGradient
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mTranslate += deltax
        val textWidth = mTextPaint!!.measureText(text.toString())
        if (mTranslate > textWidth + 1 || mTranslate < 1) {
            //先从前往后渐变 再从后往前
            deltax = -deltax
        }
        mMatrix.reset()
        mMatrix.setTranslate(mTranslate,0f)
        mLinearGradient.setLocalMatrix(mMatrix)
        postInvalidateDelayed(50)
    }
}