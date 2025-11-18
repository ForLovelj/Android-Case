package com.clow.customviewcase.ui

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivityTestViewBinding


/**
 * Created by clow
 * Des:
 * Date: 2025/11/18.
 */
class TestViewActivity: BaseActivity<ActivityTestViewBinding>() {

    override fun layoutId() = R.layout.activity_test_view

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.circularRingMenuView.setOnMenuItemClickListener { position->
            val messages = arrayOf("选项 A 被点击", "选项 B 被点击", "选项 C 被点击", "选项 D 被点击", "选项 E 被点击")
            ToastUtils.showShort(messages[position])
        }
    }
}