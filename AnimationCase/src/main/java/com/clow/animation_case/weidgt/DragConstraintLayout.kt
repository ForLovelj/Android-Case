package com.clow.animation_case.weidgt

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.contains
import com.blankj.utilcode.util.LogUtils

/**
 * Created by clow
 * Des: 从手指点击的地方生成一个view 用于拖拽
 * Date: 2023/11/15.
 */
class DragConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes){

    private var mView: View? = null
    private var isDrag = false

    fun setDragView(view: View) {
        mView = view
        view.visibility = View.INVISIBLE
        view.parent?.let {
            (it as ViewGroup).removeView(view)
        }
        if(!contains(view)){
            addView(view)
        }
    }

    private fun removeDragView() {
        mView?.let {
            it.visibility = View.INVISIBLE
            removeView(it)
        }
    }

    fun setDrag(isDrag: Boolean) {
        this.isDrag = isDrag
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        LogUtils.i("isDrop: $isDrag   action: ${ev.action}  x: ${ev.x}  y: ${ev.y}")
        if (isDrag) {
            when (ev.action) {
                MotionEvent.ACTION_MOVE -> {
                    mView?.let {
                        it.translationX = ev.x - it.width/2
                        it.translationY = ev.y - it.height/2
                        it.visibility = View.VISIBLE
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isDrag = false
                    removeDragView()
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}