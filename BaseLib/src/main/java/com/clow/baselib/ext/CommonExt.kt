package com.clow.baselib.ext

import android.content.Context
import android.util.TypedValue
import android.view.View
import com.blankj.utilcode.util.Utils

/**
 * Created by clow
 * Des:
 * Date: 2022/10/14.
 */

var Float.dp: Int
    set(value) {

    }
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Utils.getApp().resources.displayMetrics
    ).toInt()

var Float.dpf: Float
    set(value) {

    }
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Utils.getApp().resources.displayMetrics
    )

/**
 * dp值转换为px
 */
fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 */
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * dp值转换为px
 */
fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 */
fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}