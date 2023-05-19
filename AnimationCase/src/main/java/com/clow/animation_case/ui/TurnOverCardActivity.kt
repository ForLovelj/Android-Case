package com.clow.animation_case.ui

import android.animation.*
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.clow.animation_case.R
import com.clow.animation_case.databinding.ActivityTurnOverCardBinding
import com.clow.animation_case.databinding.ItemTurnOverBinding
import com.clow.baselib.base.BaseActivity
import com.clow.baselib.ext.dp


/**
 * Created by clow
 * Des: 翻牌子动画
 * Date: 2023/5/19.
 */
class TurnOverCardActivity : BaseActivity<ActivityTurnOverCardBinding>() {

    private val mBackData = listOf(
        R.drawable.ic_aoteman_back,
        R.drawable.ic_zhizhuxia_back,
        R.drawable.ic_xiaomoxian_back,
        R.drawable.ic_aisha_back,
        R.drawable.ic_gangtiexia_back,
        R.drawable.ic_lvjuren_back,
        R.drawable.ic_tegongdui_back,
        R.drawable.ic_wangwangdui_back,
        R.drawable.ic_feixia_back,
    )

    private val mAdapter by lazy {
        TurnOverAdapter()
    }

    private val mTempView by lazy {
        View.inflate(mContext, R.layout.item_turn_over, null).apply {
            layoutParams = ViewGroup.LayoutParams(71f.dp, 109.5f.dp)
        }
    }

    private var mTempViewBind: ItemTurnOverBinding? = null

    private val mStartPoint = Point()
    private val mEndPoint = Point()
    private var mPosition = 0

    override fun layoutId() = R.layout.activity_turn_over_card

    override fun initView(savedInstanceState: Bundle?) {

        val mData = mutableListOf<TurnOverData>()
        mBackData.forEach {
            mData.add(TurnOverData().apply {
                imageBack = it
                imageFront = R.drawable.ic_aoteman_front
            })
        }

        with(mViewBinding.recyclerView) {
            layoutManager = GridLayoutManager(mContext, 3)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener{adapter, view, position ->
            mPosition = position
            val turnOverData = mAdapter.data[position]
            if (turnOverData.isTurnOver) {
                return@setOnItemClickListener
            }
            val location = IntArray(2)
            view.getLocationInWindow(location)
            start(turnOverData,location)
        }
        mAdapter.setNewInstance(mData)


    }

    fun start(turnOverData: TurnOverData, startLocation: IntArray) {
        (window.decorView as ViewGroup).let {
            mStartPoint.x = startLocation[0] + 18f.dp
            mStartPoint.y = startLocation[1]
            mEndPoint.x = it.right / 2 - 71f.dp / 2
            mEndPoint.y = it.bottom / 2 - 109.5f.dp / 2

            mTempView.x = mStartPoint.x.toFloat()
            mTempView.y = mStartPoint.y.toFloat()
            mTempViewBind = ItemTurnOverBinding.bind(mTempView)
            mTempViewBind?.image?.setImageResource(turnOverData.imageBack)
            it.removeView(mTempView)
            it.addView(mTempView)
            LogUtils.i("mStartPoint: $mStartPoint   mEndPoint: $mEndPoint")

            val translationXValuesHolder = PropertyValuesHolder.ofKeyframe(
                View.TRANSLATION_X,
                Keyframe.ofFloat(0f, mStartPoint.x.toFloat()),
                Keyframe.ofFloat(1f, mEndPoint.x.toFloat())
            )
            val translationYValuesHolder = PropertyValuesHolder.ofKeyframe(
                View.TRANSLATION_Y,
                Keyframe.ofFloat(0f, mStartPoint.y.toFloat()),
                Keyframe.ofFloat(1f, mEndPoint.y.toFloat())
            )

            ObjectAnimator.ofPropertyValuesHolder(mTempView,translationXValuesHolder,translationYValuesHolder).apply {
                duration = 500
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        scale(turnOverData,it)
                    }
                })
                start()
            }
        }
    }

    fun scale(turnOverData: TurnOverData,viewGroup: ViewGroup) {
        val sx = PropertyValuesHolder.ofKeyframe(
            View.SCALE_X,
            Keyframe.ofFloat(0f, 1.0f),
            Keyframe.ofFloat(1.0f, 1.5f)
        )
        val sy = PropertyValuesHolder.ofKeyframe(
            View.SCALE_Y,
            Keyframe.ofFloat(0f, 1.0f),
            Keyframe.ofFloat(1.0f, 1.5f)
        )

        ObjectAnimator.ofPropertyValuesHolder(mTempView,sx,sy).apply{
            duration = 500
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    rotate1(turnOverData,viewGroup)
                }
            })
            start()
        }
    }


    fun rotate1(turnOverData: TurnOverData,viewGroup: ViewGroup) {
        ObjectAnimator.ofFloat(mTempView,View.ROTATION_Y,0f,90f).apply {
            duration = 250
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mTempViewBind?.image?.setImageResource(turnOverData.imageFront)
                    rotate2(turnOverData,viewGroup)
                }
            })
            start()
        }
    }

    fun rotate2(turnOverData: TurnOverData,viewGroup: ViewGroup) {
        ObjectAnimator.ofFloat(mTempView,View.ROTATION_Y,270f,360f).apply {
            duration = 250
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    turnOverData.isTurnOver = true

                    mTempView.setOnClickListener {
                        mTempView.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(500)
                            .translationX(mStartPoint.x.toFloat())
                            .translationY(mStartPoint.y.toFloat())
                            .withEndAction {
                                viewGroup.removeView(mTempView)
                                mAdapter.notifyItemChanged(mPosition)
                            }
                    }
                }
            })
            start()
        }
    }
}

class TurnOverData {
    var imageBack: Int = 0
    var imageFront: Int = 0
    var isTurnOver = false
}

class TurnOverAdapter : BaseQuickAdapter<TurnOverData, BaseViewHolder>(R.layout.item_turn_over) {
    override fun convert(holder: BaseViewHolder, item: TurnOverData) {

        holder.setImageResource(
            R.id.image,
            if (item.isTurnOver) item.imageFront else item.imageBack
        )
    }
}