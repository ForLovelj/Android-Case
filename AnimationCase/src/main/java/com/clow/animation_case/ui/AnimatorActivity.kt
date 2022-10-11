package com.clow.animation_case.ui

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.clow.animation_case.R
import com.clow.animation_case.databinding.ActivityAnimatorBinding
import com.clow.baselib.base.BaseActivity


/**
 * Created by clow
 * Des: 属性动画case
 *  - 通过使用 Animator 在设定的时间段内修改对象的属性值来创建动画。
 *  - 借助属性动画系统，您可以定义动画的以下特性：
 *       时长：您可以指定动画的时长。默认时长为 300 毫秒。
 *       时间插值：您可以指定如何根据动画的当前已播放时长来计算属性的值。
 *       重复计数和行为：您可以指定是否在某个时长结束后重复播放动画以及重复播放动画多少次。您还可以指定是否要反向播放动画。
 *                    如果将其设置为反向播放，则会先播放动画，然后反向播放动画，直到达到重复次数。
 *       Animator 集：您可以将动画分成多个逻辑集，它们可以一起播放、按顺序播放或者在指定的延迟时间后播放。
 *       帧刷新延迟：您可以指定动画帧的刷新频率。默认设置为每 10 毫秒刷新一次，但应用刷新帧的速度最终取决于整个系统的繁忙
 *                 程度以及系统为底层计时器提供服务的速度。
 *
 *  - 文件位置：
 *      res/animator/filename.xml 该文件名将用作资源 ID。
 *  - 编译后的资源数据类型：
 *      指向 ValueAnimator、ObjectAnimator 或 AnimatorSet 的资源指针。
 *  - 资源引用：
 *      在 Java 或 Kotlin 代码中：R.animator.filename
 *      在 XML 中：@[package:]animator/filename
 * Date: 2022/10/10.
 */
class AnimatorActivity : BaseActivity<ActivityAnimatorBinding>() {
    override fun layoutId() = R.layout.activity_animator

    override fun initView(savedInstanceState: Bundle?) {

        mViewBinding.btValueAnim.setOnClickListener {
            valueOfFloat(0f, 1f)
//            valueOfArgb(Color.BLACK, Color.WHITE)
        }

        mViewBinding.btObjectAnim.setOnClickListener {
            set()
        }

        mViewBinding.btCustom.setOnClickListener {
            animCustom()
        }
    }

    private fun valueOfFloat(vararg values: Float) {
        ValueAnimator.ofFloat(*values).apply {
            duration = 3000
            addUpdateListener {
                // 动画更新过程中的动画值，可以根据动画值的变化来关联对象的属性，实现属性动画
                val value = it.animatedValue as Float
                mViewBinding.ivMusic.alpha = value
                LogUtils.i("ValueAnimator 动画值: $value")
            }
            start()
        }
    }

    private fun valueOfArgb(vararg values: Int) {
        ValueAnimator.ofArgb(*values).apply {
            duration = 3000
            addUpdateListener {
                // 动画更新过程中的动画值，可以根据动画值的变化来关联对象的属性，实现属性动画
                val value = it.animatedValue as Int
                mViewBinding.tvArgb.setBackgroundColor(value)
                LogUtils.i("ValueAnimator 动画值: $value")
            }
            start()
        }
    }


    private fun set(type: Int = 0) {
        val set = if (type == 0) {
            //xml方式实现
            (AnimatorInflater.loadAnimator(
                this,
                R.animator.property_animator_set
            ) as AnimatorSet).apply {
                setTarget(mViewBinding.ivMusic)
            }
        } else {
            //代码方式实现
            val scaleXAnimator =
                ObjectAnimator.ofFloat(mViewBinding.ivMusic, "scaleX", 1f, 0.5f).apply {
                    duration = 2000
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = 1
                }
            val scaleYAnimator =
                ObjectAnimator.ofFloat(mViewBinding.ivMusic, "scaleY", 1f, 0.5f).apply {
                    duration = 2000
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = 1
                }
            val alphaAnimator =
                ObjectAnimator.ofFloat(mViewBinding.ivMusic, "alpha", 1f, 0.5f).apply {
                    duration = 2000
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = 1
                }
            val rotationYAnimator =
                ObjectAnimator.ofFloat(mViewBinding.ivMusic, "rotationY", 0f, 360f).apply {
                    duration = 2000
                }
            AnimatorSet().apply {
                play(scaleXAnimator)
                    .with(scaleYAnimator)
                    .with(alphaAnimator)
//                    .after(rotationYAnimator)
            }
        }

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                LogUtils.i("onAnimationStart...")
            }

            override fun onAnimationEnd(animation: Animator) {
                LogUtils.i("onAnimationEnd...")
            }

            override fun onAnimationCancel(animation: Animator) {
                LogUtils.i("onAnimationCancel...")
            }

            override fun onAnimationRepeat(animation: Animator) {
                LogUtils.i("onAnimationRepeat...")
            }

        })
//        set.playSequentially()
//        set.playTogether()
        set.start()
    }

    /**
     *  要添加动画效果的对象属性必须具有 set<PropertyName>() 形式的 setter 函数（采用驼峰式大小写形式）。
     *  由于 ObjectAnimator 会在动画过程中自动更新属性，它必须能够使用此 setter 方法访问该属性。
     *  例如，如果属性名称为 progress，则需要使用 setProgress() 方法。如果此 setter 方法不存在，您有三个选择：
     *  如果您有权限，可将 setter 方法添加到类中。
     *  使用您有权更改的封装容器类，让该封装容器使用有效的 setter 方法接收值并将其转发给原始对象。
     *  改用 ValueAnimator。
     */
    private fun animCustom(){
        ObjectAnimator.ofFloat(mViewBinding.circleProgressView,"progress",0f,360f).apply {
            duration = 3000
            start()
        }
    }

}