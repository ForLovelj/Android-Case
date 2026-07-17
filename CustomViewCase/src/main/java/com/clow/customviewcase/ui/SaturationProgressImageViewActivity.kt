package com.clow.customviewcase.ui

import android.os.Bundle
import android.widget.SeekBar
import com.clow.baselib.base.BaseActivity
import com.clow.baselib.ext.dpf
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivitySaturationProgressImageViewBinding
import com.clow.customviewcase.widget.CornerRadii
import com.clow.customviewcase.widget.SaturationProgressImageView
import java.util.Locale

/**
 * SaturationProgressImageView 交互演示页面。
 */
class SaturationProgressImageViewActivity :
    BaseActivity<ActivitySaturationProgressImageViewBinding>() {

    override fun layoutId() = R.layout.activity_saturation_progress_image_view

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.progressImage.direction =
            SaturationProgressImageView.ProgressDirection.BOTTOM_TO_TOP
        mViewBinding.progressImage.cornerRadii = CornerRadii(
            topLeft = 24f.dpf,
            topRight = 24f.dpf,
            bottomRight = 0f.dpf,
            bottomLeft = 0f.dpf
        )

        updateSaturation(mViewBinding.saturationSeekBar.progress)
        updateProgress(mViewBinding.progressSeekBar.progress)

        mViewBinding.saturationSeekBar.setOnSeekBarChangeListener(
            seekBarChangeListener(::updateSaturation)
        )
        mViewBinding.progressSeekBar.setOnSeekBarChangeListener(
            seekBarChangeListener(::updateProgress)
        )
    }

    private fun seekBarChangeListener(onProgressChanged: (Int) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        }

    private fun updateSaturation(progress: Int) {
        val saturation = progress / SATURATION_SCALE
        mViewBinding.progressImage.saturation = saturation
        mViewBinding.saturationValue.text = getString(
            R.string.progress_image_saturation_value,
            String.format(Locale.getDefault(), "%.2f", saturation)
        )
    }

    private fun updateProgress(progress: Int) {
        mViewBinding.progressImage.progress = progress / PROGRESS_SCALE
        mViewBinding.progressValue.text = getString(R.string.progress_image_progress_value, progress)
    }

    private companion object {
        const val SATURATION_SCALE = 100.0f
        const val PROGRESS_SCALE = 100.0f
    }
}
