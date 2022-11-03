package com.clow.animation_case.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import android.widget.SeekBar.OnSeekBarChangeListener
import com.blankj.utilcode.util.LogUtils
import com.clow.animation_case.R
import com.clow.animation_case.databinding.ActivityInterpolatorBinding
import com.clow.animation_case.weidgt.ControlPointCallback
import com.clow.animation_case.weidgt.CurveVisualizer
import com.clow.baselib.base.BaseActivity
import java.lang.reflect.Constructor

/**
 * Created by clow
 * Des: 动画插值器case
 * 插值器 ：在动画时间内，插值器会根据自身改变动画的值，使动画具有不同的模型
 *  <accelerateDecelerateInterpolator>
 *  变化率在开始和结束时缓慢，但在中间会加快。
 *  没有属性。
 *
 *  <accelerateInterpolator>
 *  变化率在开始时较为缓慢，然后会加快。
 *  属性：
 *  android:factor 浮点数。加速率（默认为 1）。
 *
 *  <anticipateInterpolator>
 *  先反向变化，然后再急速正向变化。
 *  属性：
 *  android:tension 浮点数。要应用的张力（默认为 2）。
 *
 *  <anticipateOvershootInterpolator>
 *  先反向变化，再急速正向变化并超过目标值，然后以最终值结束。
 *  属性：
 *  android:tension 浮点数。要应用的张力（默认为 2）。
 *  android:extraTension    浮点数。张力要乘以的倍数（默认值为 1.5）。
 *
 *  <bounceInterpolator>
 *  变化会在结束时退回。
 *  没有属性。
 *
 *  <cycleInterpolator>
 *  按指定的循环次数重复动画。变化率符合正弦曲线图。
 *  属性：
 *  android:cycles  整数。循环次数（默认值为 1）。
 *
 *  <decelerateInterpolator>
 *  变化率开始时很快，然后减慢。
 *  属性：
 *  android:factor  浮点数。减速率（默认值为 1）。
 *
 *  <linearInterpolator>
 *  变化率恒定不变。
 *  没有属性。
 *
 *  <overshootInterpolator>
 *  先急速正向变化，再超过最终值，然后回到最终值。
 *  属性：
 *  android:tension 浮点数。要应用的张力（默认为 2）。
 *
 *  <PathInterpolator> Android5.0引入
 *  此插值器在一个 1x1 的正方形内指定一个动作曲线，定位点位于 (0,0) 和 (1,1)，而控制点则使用构造函数参数指定
 *  可以定制出任何你想要的速度模型。定制的方式是使用一个 Path 对象来绘制出你要的动画完成度 / 时间完成度曲线
 * Date: 2022/10/11.
 */
class InterpolatorActivity: BaseActivity<ActivityInterpolatorBinding>() {

    private var mDefaultMargin = 0
    private var mDuration = 300L
    private var mAnimator: ObjectAnimator? = null
    private lateinit var mVisualizer: CurveVisualizer

    override fun layoutId() = R.layout.activity_interpolator

    override fun initView(savedInstanceState: Bundle?) {

        val metrics = resources.displayMetrics
        mDefaultMargin = (8 * metrics.density).toInt()

        mAnimator = ObjectAnimator.ofFloat(this, "fraction", 0f, 1f)
        mVisualizer = CurveVisualizer(this)
        mViewBinding.gridParent.addView(mVisualizer)

        mViewBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                populateParametersUI(parent.getItemAtPosition(position).toString(),
                    mViewBinding.paramatersParent)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        mViewBinding.durationSeeker.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mViewBinding.durationLabel.text = "Duration ${progress}ms"
                mDuration = progress.toLong()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    /**
     * Called when the "Run" button is clicked. It cancels any running animation
     * and starts a new one with the values specified in the UI.
     */
    fun runAnimation(view: View) {
        mAnimator!!.cancel()
        mAnimator!!.interpolator = mVisualizer.interpolator
        mAnimator!!.duration = mDuration
        mAnimator!!.start()
    }

    /**
     * This method is called to populate the UI according to which interpolator was
     * selected.
     */
    private fun populateParametersUI(interpolatorName: String, parent: LinearLayout) {
        parent.removeAllViews()
        try {
            when (interpolatorName) {
                "Quadratic Path" -> createQuadraticPathInterpolator(parent)
                "Cubic Path" -> createCubicPathInterpolator(parent)
                "AccelerateDecelerate" -> mVisualizer.interpolator =
                    AccelerateDecelerateInterpolator()
                "Linear" -> mVisualizer.interpolator = LinearInterpolator()
                "Bounce" -> mVisualizer.interpolator = BounceInterpolator()
                "Accelerate" -> {
                    val decelConstructor: Constructor<AccelerateInterpolator> =
                        AccelerateInterpolator::class.java.getConstructor(Float::class.javaPrimitiveType)
                    createParamaterizedInterpolator(parent, decelConstructor, "Factor", 1f, 5f, 1f)
                }
                "Decelerate" -> {
                    val accelConstructor: Constructor<DecelerateInterpolator> =
                        DecelerateInterpolator::class.java.getConstructor(
                            Float::class.javaPrimitiveType
                        )
                    createParamaterizedInterpolator(parent, accelConstructor, "Factor", 1f, 5f, 1f)
                }
                "Overshoot" -> {
                    val overshootConstructor: Constructor<OvershootInterpolator> =
                        OvershootInterpolator::class.java.getConstructor(Float::class.javaPrimitiveType)
                    createParamaterizedInterpolator(
                        parent,
                        overshootConstructor,
                        "Tension",
                        1f,
                        5f,
                        1f
                    )
                }
                "Anticipate" -> {
                    val anticipateConstructor: Constructor<AnticipateInterpolator> =
                        AnticipateInterpolator::class.java.getConstructor(Float::class.javaPrimitiveType)
                    createParamaterizedInterpolator(
                        parent,
                        anticipateConstructor,
                        "Tension",
                        1f,
                        5f,
                        1f
                    )
                }
            }
        } catch (e: NoSuchMethodException) {
            LogUtils.eTag("InterpolatorPlayground", "Error constructing interpolator: $e")
        }
    }

    /**
     * Creates an Interpolator that takes a single parameter in its constructor.
     * The min/max/default parameters determine how the interpolator is initially
     * set up as well as the values used in the SeekBar for changing this value.
     */
    private fun <T: Interpolator> createParamaterizedInterpolator(
        parent: LinearLayout,
        constructor: Constructor<T>, name: String,
        min: Float, max: Float, defaultValue: Float
    ) {
        val inputContainer = LinearLayout(this)
        inputContainer.orientation = LinearLayout.HORIZONTAL
        var params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin)
        inputContainer.layoutParams = params
        val label = TextView(this)
        params = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.weight = .5f
        label.layoutParams = params
        val formattedValue = String.format(" %.2f", defaultValue)
        label.text = name + formattedValue
        val seek = SeekBar(this)
        params = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.weight = .5f
        seek.layoutParams = params
        seek.max = 100
        seek.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val percentage = i.toFloat() / 100
                val value = min + percentage * (max - min)
                val formattedValue = String.format(" %.2f", value)
                try {
                    mVisualizer.interpolator = constructor.newInstance(value)
                } catch (error: Throwable) {
                    LogUtils.eTag("interpolatorPlayground", error.message)
                }
                label.text = name + formattedValue
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        inputContainer.addView(label)
        inputContainer.addView(seek)
        parent.addView(inputContainer)
        try {
            mVisualizer.interpolator = constructor.newInstance(defaultValue)
        } catch (error: Throwable) {
            LogUtils.eTag("interpolatorPlayground", error.message)
        }
    }

    /**
     * Creates a quadratic PathInterpolator, whose control point values can be changed
     * by the user dragging that handle around in the UI.
     */
    private fun createQuadraticPathInterpolator(parent: LinearLayout) {
        val controlX = 0.5f
        val controlY = 0.2f
        val inputContainer = LinearLayout(this)
        inputContainer.orientation = LinearLayout.VERTICAL
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin)
        inputContainer.layoutParams = params
        val cx1Label = TextView(this)
        cx1Label.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        cx1Label.text = "ControlX: $controlX"
        val cy1Label = TextView(this)
        cy1Label.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        cy1Label.text = "ControlY: $controlY"
        inputContainer.addView(cx1Label)
        inputContainer.addView(cy1Label)
        parent.addView(inputContainer)
        val callback = object : ControlPointCallback {
            override fun onControlPoint1Moved(cx1: Float, cy1: Float) {
                cx1Label.text = "ControlX: " + String.format("%.2f", cx1)
                cy1Label.text = "ControlY: " + String.format("%.2f", cy1)
            }

            override fun onControlPoint2Moved(cx2: Float, cy2: Float) {

            }
        }
        mVisualizer.setQuadraticInterpolator(controlX, controlY, callback)
    }

    /**
     * Creates a cubic PathInterpolator, whose control points values can be changed
     * by the user dragging the handles around in the UI.
     */
    private fun createCubicPathInterpolator(parent: LinearLayout) {
        val cx1 = 0.5f
        val cy1 = .2f
        val cx2 = 0.9f
        val cy2 = .7f
        val inputContainer = LinearLayout(this)
        inputContainer.orientation = LinearLayout.VERTICAL
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin)
        inputContainer.layoutParams = params
        val cx1Label = createControlPointLabel("ControlX1", cx1)
        val cy1Label = createControlPointLabel("ControlY1", cy1)
        val cx2Label = createControlPointLabel("ControlX2", cx2)
        val cy2Label = createControlPointLabel("ControlY2", cy2)
        inputContainer.addView(cx1Label)
        inputContainer.addView(cy1Label)
        inputContainer.addView(cx2Label)
        inputContainer.addView(cy2Label)
        parent.addView(inputContainer)
        val callback = object : ControlPointCallback {
            override fun onControlPoint1Moved(cx: Float, cy: Float) {
                cx1Label.text = "ControlX1: " + String.format("%.2f", cx)
                cy1Label.text = "ControlY1: " + String.format("%.2f", cy)
            }

            override fun onControlPoint2Moved(cx: Float, cy: Float) {
                cx2Label.text = "ControlX2: " + String.format("%.2f", cx)
                cy2Label.text = "ControlY2: " + String.format("%.2f", cy)
            }
        }

        // Buttons to set control points from standard Material Design interpolators
        val buttonContainer = LinearLayout(this)
        buttonContainer.orientation = LinearLayout.HORIZONTAL
        buttonContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        buttonContainer.addView(
            createMaterialMotionButton(
                "L out S in",
                0f,
                0f,
                .2f,
                1f,
                .33f,
                callback
            )
        )
        buttonContainer.addView(
            createMaterialMotionButton(
                "F out S in",
                .4f, 0f, .2f, 1f, .33f, callback
            )
        )
        buttonContainer.addView(
            createMaterialMotionButton(
                "F out L in",
                .4f, 0f, 1f, 1f, .33f, callback
            )
        )
        inputContainer.addView(buttonContainer)
        mVisualizer.setCubicInterpolator(cx1, cy1, cx2, cy2, callback)
    }

    private fun createControlPointLabel(label: String, value: Float): TextView {
        val cx1Label = TextView(this)
        cx1Label.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        cx1Label.text = "$label: $value"
        return cx1Label
    }

    private fun createMaterialMotionButton(
        label: String,
        cx1: Float, cy1: Float, cx2: Float, cy2: Float,
        weight: Float, callback: ControlPointCallback
    ): Button {
        val button = Button(this)
        val params = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.weight = weight
        button.setLayoutParams(params)
        button.setText("F out L in")
        button.setOnClickListener(View.OnClickListener { // Animate the control points to their new values
            val oldCx1 = mVisualizer.cx1
            val oldCy1 = mVisualizer.cy1
            val oldCx2 = mVisualizer.cx2
            val oldCy2 = mVisualizer.cy2
            val anim = ValueAnimator.ofFloat(0f, 1f).setDuration(100)
            anim.addUpdateListener { valueAnimator ->
                val t = valueAnimator.animatedFraction
                mVisualizer.setCubicInterpolator(
                    oldCx1 + t * (cx1 - oldCx1),
                    oldCy1 + t * (cy1 - oldCy1),
                    oldCx2 + t * (cx2 - oldCx2),
                    oldCy2 + t * (cy2 - oldCy2), callback
                )
            }
            anim.start()
        })
        return button
    }

    /**
     * This method is called by the animation to update the position of the animated
     * objects in the curve view as well as the view at the bottom showing sample animations.
     */
    fun setFraction(fraction: Float) {
        mViewBinding.timingVisualizer.setFraction(fraction)
        mVisualizer.setFraction(fraction)
    }
}