package com.clow.customviewcase.ui

import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivityTestViewBinding
import com.clow.customviewcase.widget.CircularRingMenuView


/**
 * Created by clow
 * Des:
 * Date: 2025/11/18.
 */
class TestViewActivity: BaseActivity<ActivityTestViewBinding>() {

    override fun layoutId() = R.layout.activity_test_view

    override fun initView(savedInstanceState: Bundle?) {


        // 初始化菜单项
        val menuItems = ArrayList<CircularRingMenuView.MenuItem>()
        menuItems.add(CircularRingMenuView.MenuItem("选项A", Color.parseColor("#F44336")))
        menuItems.add(CircularRingMenuView.MenuItem("选项B", Color.parseColor("#4CAF50")))
        menuItems.add(CircularRingMenuView.MenuItem("选项C", Color.parseColor("#2196F3")))
        menuItems.add(CircularRingMenuView.MenuItem("选项D", Color.parseColor("#FFC107")))
        menuItems.add(CircularRingMenuView.MenuItem("选项E", Color.parseColor("#00DBFF")))
        mViewBinding.circularRingMenuView.setMenuItems(
            listOf("选项A","选项B","选项C","选项D","选项E"),
            listOf(
                Color.parseColor("#F44336"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#FFC107"),
                Color.parseColor("#00DBFF")
            )
        )
        mViewBinding.circularRingMenuView.setOnMenuItemClickListener { position->
            val messages = arrayOf("选项 A 被点击", "选项 B 被点击", "选项 C 被点击", "选项 D 被点击", "选项 E 被点击")
            ToastUtils.showShort(messages[position])
        }
    }
}