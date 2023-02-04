package com.clow.customviewcase.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.clow.customviewcase.R

/**
 * Created by clow
 * Des: PorterDuff 测试
 * Date: 2023/2/4.
 */
class PorterDuffLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes)  {

    private val mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.loading)
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)  //SRC_IN混合模式
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true //双线性过滤
        color = Color.parseColor("#ff7400")
    }

    private val mSrcRect = Rect(0,0,mBitmap.width,mBitmap.height)
    private val mDestRect = RectF(0f,0f,mBitmap.width.toFloat(),mBitmap.height.toFloat())
    private val mDynamicRect = RectF(0f,mBitmap.height.toFloat(),mBitmap.width.toFloat(),mBitmap.height.toFloat())
    private var mTotalWidth = 0
    private var mTotalHeight = 0
    private var mCurrentTop = mBitmap.height.toFloat() //矩形当前高度

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mBitmap.width+20,mBitmap.height+20)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTotalWidth = w
        mTotalHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //存为新图层
        val saveCount = canvas.saveLayer(0f, 0f, mTotalWidth.toFloat(), mTotalHeight.toFloat(), mPaint)
        //绘制目标图
        canvas.drawBitmap(mBitmap,mSrcRect,mDestRect,mPaint)
        //设置混合模式
        mPaint.setXfermode(mXfermode)
        //绘制源图
        canvas.drawRect(mDynamicRect,mPaint)
        //清除混合模式
        mPaint.setXfermode(null)
        //恢复图层
        canvas.restoreToCount(saveCount)

        mCurrentTop -= 1
        if (mCurrentTop <= 0) {
            //从底往上循环
            mCurrentTop = mBitmap.height.toFloat()
        }
        mDynamicRect.top = mCurrentTop
        postInvalidate()
    }
}