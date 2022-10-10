package com.clow.animation_case.ui

import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.*
import androidx.core.content.ContextCompat
import com.clow.animation_case.R
import com.clow.animation_case.databinding.ActivityAnimationBinding
import com.clow.baselib.base.BaseActivity

/**
 * Created by clow
 * Des: 视图动画case
 * 使用视图动画框架可以创建两种类型的动画：
 *     - 补间动画：通过使用 Animation 对单张图片执行一系列转换来创建动画
 *     - 帧动画：通过使用 AnimationDrawable 按顺序显示一系列图片来创建动画。
 *
 *     - 文件位置：
 *          res/anim/filename.xml
 *          该文件名将用作资源 ID。
 *     - 编译后的资源数据类型：
 *          指向 Animation 的资源指针。
 *     - 资源引用：
 *          在 Java 中：R.anim.filename
 *          在 XML 中：@[package:]anim/filename
 *
 * Date: 2022/8/26.
 */
class AnimationActivity: BaseActivity<ActivityAnimationBinding>() {

    override fun layoutId() = R.layout.activity_animation

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.btAlpha.setOnClickListener {
            alpha(1)
        }

        mViewBinding.btScale.setOnClickListener {
            scale(1)
        }

        mViewBinding.btRotation.setOnClickListener {
            rotation(1)
        }

        mViewBinding.btTranslate.setOnClickListener {
            translate(1)
        }

        mViewBinding.btSet.setOnClickListener {
            set(1)
        }

        mViewBinding.btFrame.setOnClickListener {
            frame(1)
        }
    }

    private fun alpha(type: Int = 0) {
        val anim = if (type == 0) {
            //xml 实现
            AnimationUtils.loadAnimation(this,R.anim.view_anim_alpha)
        } else {
            //代码实现
            AlphaAnimation(1.0f,0f).apply {
                duration = 2000
            }
        }
        mViewBinding.ivMusic.startAnimation(anim)
    }

    private fun scale(type: Int = 0) {
        val anim = if (type == 0) {
            //xml 实现
            AnimationUtils.loadAnimation(this,R.anim.view_anim_scale)
        } else {
            //代码实现
            ScaleAnimation(
                1f, 0.5f,
                1f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
                .apply {
                    duration = 2000
                }

        }
        mViewBinding.ivMusic.startAnimation(anim)
    }

    private fun rotation(type: Int = 0) {
        val anim = if (type == 0) {
            //xml 实现
            AnimationUtils.loadAnimation(this,R.anim.view_anim_rotation)
        } else {
            //代码实现
            RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
                .apply {
                    duration = 2000
                }

        }
        mViewBinding.ivMusic.startAnimation(anim)
    }

    private fun translate(type: Int = 0) {
        val anim = if (type == 0) {
            //xml 实现
            AnimationUtils.loadAnimation(this,R.anim.view_anim_translate)
        } else {
            //代码实现
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f)
                .apply {
                    duration = 2000
                }
        }
        mViewBinding.ivMusic.startAnimation(anim)
    }

    private fun set(type: Int = 0) {
        val anim = if (type == 0) {
            //xml 实现
            AnimationUtils.loadAnimation(this,R.anim.view_anim_set)
        } else {
            //代码实现
            val alphaAnim = AlphaAnimation(1.0f,0f).apply {
                duration = 2000
            }

            val scaleAnim = ScaleAnimation(
                1f, 0.5f,
                1f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
                .apply {
                    duration = 2000
                }

            val rotateAnim = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
                .apply {
                    duration = 2000
                }

            val translateAnim = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f)
                .apply {
                    duration = 2000
                }

            AnimationSet(true).apply {
                addAnimation(alphaAnim)
                addAnimation(scaleAnim)
                addAnimation(rotateAnim)
                addAnimation(translateAnim)
            }

        }
        mViewBinding.ivMusic.startAnimation(anim)
    }

    private fun frame(type: Int = 0) {
        mViewBinding.ivMusic.setImageResource(0)
        if (type == 0) {
            //xml 实现
            mViewBinding.ivMusic.setBackgroundResource(R.drawable.music_frame_anim)
            val musicAnimation = mViewBinding.ivMusic.background
            if (musicAnimation is Animatable) {
                musicAnimation.start()
            }
        } else {
            //代码实现
            val animationDrawable = AnimationDrawable()
            for (i in 1..3) {
                val resId = resources.getIdentifier("ic_music$i", "drawable", packageName)
                val drawable = ContextCompat.getDrawable(this, resId)
                animationDrawable.addFrame(drawable!!,300)
            }
            animationDrawable.isOneShot = true
            mViewBinding.ivMusic.setImageDrawable(animationDrawable)
            animationDrawable.start()
        }

    }
}