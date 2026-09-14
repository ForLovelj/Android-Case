package com.clow.customviewcase.ui

import android.os.Bundle
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivitySaturationImageViewBinding
import java.util.Locale

/**
 * SaturationImageView 交互演示页面。
 */
class SaturationImageViewActivity : BaseActivity<ActivitySaturationImageViewBinding>() {

    override fun layoutId() = R.layout.activity_saturation_image_view

    override fun initView(savedInstanceState: Bundle?) {
        updateSaturation(mViewBinding.saturationSeekBar.progress)
        Glide.with(this)
            .load(SATURATION_IMAGE_URL)
            .into(mViewBinding.saturationImage)

        mViewBinding.saturationSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    updateSaturation(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            }
        )
    }

    private fun updateSaturation(progress: Int) {
        val saturation = progress / SATURATION_SCALE
        mViewBinding.saturationImage.saturation = saturation
        mViewBinding.saturationValue.text = getString(
            R.string.saturation_value,
            String.format(Locale.getDefault(), "%.2f", saturation)
        )
    }

    private companion object {
        const val SATURATION_SCALE = 100.0f
        const val SATURATION_IMAGE_URL =
            "https://filecdn.ailecheng.com/20260615/07bcf16d8e4ae07d621999828a9837f3.png"
    }
}
