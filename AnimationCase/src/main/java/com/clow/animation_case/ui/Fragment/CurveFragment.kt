package com.clow.animation_case.ui.Fragment

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.view.animation.PathInterpolator
import com.clow.animation_case.R
import com.clow.animation_case.databinding.FragmentCurveBinding
import com.clow.baselib.base.BaseFragment

/**
 * Created by clow
 * Des: 通过Path 实现曲线动画
 * Date: 2022/11/2.
 */
class CurveFragment: BaseFragment<FragmentCurveBinding>() {

    override fun layoutId() = R.layout.fragment_curve

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mViewBinding.bt1.setOnClickListener {
            showPathInterpolator()
        }

        mViewBinding.bt2.setOnClickListener {
            showPath()
        }

        mViewBinding.bt3.setOnClickListener {
            showBezier2()
        }

        mViewBinding.bt4.setOnClickListener {
            showBezier3()
        }

    }

    /**
     * 通过path 运行曲线动画
     */
    private fun showPath() {
        //运行曲线为矩形的内切椭圆
        val path = Path().apply {
            arcTo(0f,
                mViewBinding.ivBall.top.toFloat(),
                mViewBinding.ivBall.left * 2f,
                1000f,
                270f, -180f, true)
        }

        ObjectAnimator.ofFloat(mViewBinding.ivBall,View.X,View.Y,path).apply {
            duration = 2000
            start()
        }
    }

    /**
     * 二阶贝塞尔曲线
     */
    private fun showBezier2() {
        val path = Path().apply {
            moveTo(mViewBinding.ivBall.left.toFloat(),//480,300
                mViewBinding.ivBall.top.toFloat())
            //(x1,y1) 控制点坐标 (x2,y2) 结束点坐标
            quadTo(
                100f,
                400f,
                mViewBinding.ivBall.left.toFloat(),
                1000f
            )
        }

        ObjectAnimator.ofFloat(mViewBinding.ivBall,View.X,View.Y,path).apply {
            duration = 2000
            start()
        }
    }

    /**
     * 三阶贝塞尔曲线
     */
    private fun showBezier3() {
        val path = Path().apply {
            moveTo(mViewBinding.ivBall.left.toFloat(),//480,300
                mViewBinding.ivBall.top.toFloat())
            cubicTo(
                100f,
                400f,
                760f,
                800f,
                mViewBinding.ivBall.left.toFloat(),
                1000f,
            )
        }

        ObjectAnimator.ofFloat(mViewBinding.ivBall,View.X,View.Y,path).apply {
            duration = 2000
            start()
        }
    }

    /**
     * 插值器器PathInterpolator 自定义动画完成度 / 时间完成度曲线
     *
     * 这条 Path 描述的其实是一个 y = f(x) (0 ≤ x ≤ 1) （y 为动画完成度，x 为时间完成度）的曲线，
     * 所以同一段时间完成度上不能有两段不同的动画完成度，而且每一个时间完成度的点上都必须要有对应的动画完成度
     *
     */
    private fun showPathInterpolator() {
        val path = Path().apply {
            // 先以「时间完成度 : 动画完成度 = 1 : 1」的速度匀速运行 25%
            lineTo(0.25f, 0.25f)
            // 然后瞬间跳跃到 50% 的动画完成度
            moveTo(0.25f,0.5f)
            // 再匀速返回到目标点
            lineTo(1f,1f)
        }

        val path1 = Path().apply {
            //先减速再加速
            // 先以0.8的时间完成0.2的动画完成度
            lineTo(0.8f, 0.2f)
            // 然后以0.2的时间完成剩下的0.8动画完成度
            lineTo(1f,1f)
        }

        val path2 = Path().apply {
            arcTo(0f,
                mViewBinding.ivBall.top.toFloat(),
                800f,
                800f,
                270f, -180f, true)
        }
        val pathInterpolator = PathInterpolator(path1)
        ObjectAnimator.ofFloat(mViewBinding.ivBall,View.X,View.Y,path2).apply {
            interpolator = pathInterpolator
            duration = 2000
            start()
        }
    }
}