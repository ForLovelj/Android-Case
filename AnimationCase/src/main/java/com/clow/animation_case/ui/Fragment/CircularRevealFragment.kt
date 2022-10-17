package com.clow.animation_case.ui.Fragment

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import com.blankj.utilcode.util.ScreenUtils
import com.clow.animation_case.R
import com.clow.animation_case.databinding.FragmentCircularRevealBinding
import com.clow.baselib.base.BaseFragment

/**
 * Created by clow
 * Des: 圆形揭露动画
 * Date: 2022/10/17.
 */
class CircularRevealFragment: BaseFragment<FragmentCircularRevealBinding>() {

    override fun layoutId() = R.layout.fragment_circular_reveal

    override fun initView(view: View, savedInstanceState: Bundle?) {

        mViewBinding.bt1.setOnClickListener {
            show01()
        }

        mViewBinding.bt2.setOnClickListener {
            show02()
        }

        mViewBinding.bt3.setOnClickListener {
            show03()
        }

        mViewBinding.bt4.setOnClickListener {
            show04()
        }
    }

    /**
     * 从点击按钮出开始揭露动画
     */
    private fun show01() {
        //获取裁剪圆的中心
        val cx = mViewBinding.bt1.width / 2
        val cy = mViewBinding.bt1.height / 2

        //获取裁剪圆最终半径
        val radius = Math.hypot(ScreenUtils.getScreenWidth().toDouble(), ScreenUtils.getScreenHeight().toDouble()).toFloat()

        //为这个视图创建动画(起始半径为0)
        //view：代表的是要操作的view
        //centerX：圆的x方向的中点
        //centerY：圆的y方向的中点
        //startRadius：这个圆开始时候的半径
        //endRadius：结束时候的半径
        val anim = ViewAnimationUtils.createCircularReveal(mViewBinding.ivBg, cx, cy, 0f, radius).apply {
            duration = 2000
        }

        mViewBinding.ivBg.visibility = View.VISIBLE
        anim.start()
    }

    /**
     * 斜线展示
     */
    private fun show02() {
        //获取裁剪圆的中心
        val cx = mViewBinding.ivBg.width / 2
        val cy = mViewBinding.ivBg.height / 2

        //获取裁剪圆最终半径
        val radius = Math.hypot(mViewBinding.ivBg.width.toDouble(), mViewBinding.ivBg.height.toDouble()).toFloat()

        //为这个视图创建动画(起始半径为0)
        //view：代表的是要操作的view
        //centerX：圆的x方向的中点
        //centerY：圆的y方向的中点
        //startRadius：这个圆开始时候的半径
        //endRadius：结束时候的半径
        val anim = ViewAnimationUtils.createCircularReveal(mViewBinding.ivBg, 0, 0, 0f, radius).apply {
            duration = 2000
        }

        mViewBinding.ivBg.visibility = View.VISIBLE
        anim.start()
    }

    /**
     * 由内到外
     */
    private fun show03() {
        //获取裁剪圆的中心
        val cx = mViewBinding.ivBg.width / 2
        val cy = mViewBinding.ivBg.height / 2

        //获取裁剪圆最终半径
        val radius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        //为这个视图创建动画(起始半径为0)
        //view：代表的是要操作的view
        //centerX：圆的x方向的中点
        //centerY：圆的y方向的中点
        //startRadius：这个圆开始时候的半径
        //endRadius：结束时候的半径
        val anim = ViewAnimationUtils.createCircularReveal(mViewBinding.ivBg, cx, cy, 0f, radius).apply {
            duration = 2000
        }

        mViewBinding.ivBg.visibility = View.VISIBLE
        anim.start()
    }

    /**
     * 由外到内
     */
    private fun show04() {
        //获取裁剪圆的中心
        val cx = mViewBinding.ivBg.width / 2
        val cy = mViewBinding.ivBg.height / 2

        //获取裁剪圆最终半径
        val radius = mViewBinding.ivBg.height.toFloat()

        //为这个视图创建动画(起始半径为0)
        //view：代表的是要操作的view
        //centerX：圆的x方向的中点
        //centerY：圆的y方向的中点
        //startRadius：这个圆开始时候的半径
        //endRadius：结束时候的半径
        val anim = ViewAnimationUtils.createCircularReveal(mViewBinding.ivBg, cx, cy, radius, 0f).apply {
            duration = 2000
        }

        val listener = object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mViewBinding.ivBg.visibility = View.GONE
                anim.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        }

        anim.addListener(listener)
        anim.start()
    }

}
