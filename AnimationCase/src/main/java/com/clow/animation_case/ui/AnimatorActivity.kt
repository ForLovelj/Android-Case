package com.clow.animation_case.ui

import android.os.Bundle
import com.clow.animation_case.R
import com.clow.animation_case.databinding.ActivityAnimatorBinding
import com.clow.baselib.base.BaseActivity

/**
 * Created by clow
 * Des: 属性动画case
 *  - 通过使用 Animator 在设定的时间段内修改对象的属性值来创建动画。
 * Date: 2022/10/10.
 */
class AnimatorActivity: BaseActivity<ActivityAnimatorBinding>() {
    override fun layoutId() = R.layout.activity_animator

    override fun initView(savedInstanceState: Bundle?) {

    }
}