package com.clow.customviewcase.widget

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by clow
 * Des: 设置图片饱和度
 * Date: 2026/7/17.
 */
open class SaturationImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val colorMatrix = ColorMatrix()

    /**
     * 图片饱和度属性
     * 0.0f = 黑白, 1.0f = 正常, >1.0f = 艳丽
     */
    var saturation: Float = 1.0f
        set(value) {
            field = value
            colorMatrix.setSaturation(value)
            // 直接为 ImageView 设置色彩滤镜
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }

    /**
     * 清除滤镜，恢复原图
     */
    fun clearSaturation() {
        saturation = 1.0f
        clearColorFilter()
    }
}
