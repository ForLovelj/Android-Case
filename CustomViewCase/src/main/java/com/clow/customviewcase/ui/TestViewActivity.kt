package com.clow.customviewcase.ui

import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.data.TaskMapNode
import com.clow.customviewcase.databinding.ActivityTestViewBinding
import com.clow.customviewcase.widget.CircularRingMenuView


/**
 * Created by clow
 * Des:
 * Date: 2025/11/18.
 */
class TestViewActivity: BaseActivity<ActivityTestViewBinding>() {

    // 模拟数据
    val taskMapNodes = listOf(
        TaskMapNode(1, "初级课程 1", true),
        TaskMapNode(2, "初级课程 2", true),
        TaskMapNode(3, "进阶课程 1", true),
        TaskMapNode(4, "进阶课程 2", true, isCurrent = true), // 当前学习节点
        TaskMapNode(5, "高级课程 1", false),
        TaskMapNode(6, "高级课程 2", false),
        TaskMapNode(7, "终极课程 1", false),
        TaskMapNode(8, "终极课程 2", false),
        TaskMapNode(9, "终极课程 3", false),
        TaskMapNode(10, "终极课程 10", false),
        TaskMapNode(11, "终极课程 11", false),
        TaskMapNode(12, "终极课程 12", false),
        TaskMapNode(13, "终极课程 13", false),
        TaskMapNode(14, "终极课程 14", false),
        TaskMapNode(15, "终极课程 15", false),
        TaskMapNode(16, "终极课程 16", false),
        TaskMapNode(17, "终极课程 17", false),
    )

    override fun layoutId() = R.layout.activity_test_view

    override fun initView(savedInstanceState: Bundle?) {


//        initCircularRingMenuView()
        initTaskMapView()
    }

    private fun initTaskMapView() {

        mViewBinding.taskMapView.setNodes(taskMapNodes.reversed())
        mViewBinding.taskMapView.setOnNodeClickListener { node ->
            ToastUtils.showShort("点击了课程: ${node.title}, ID: ${node.id}")
            // 在这里可以实现点击节点后的逻辑，例如跳转到课程详情页
        }

        // 等待 View 渲染完成后滚动
        mViewBinding.taskMapView.post {
            val centerY = mViewBinding.taskMapView.getCurrentNodeCenterY()

            // 计算滚动的目标位置：让节点显示在屏幕中间
            // 目标位置 = 节点Y坐标 - 屏幕高度的一半
            val screenHeight = mViewBinding.scrollView.height
            val scrollTarget = (centerY - screenHeight / 2f).toInt()
//            val scrollTarget = (centerY - screenHeight / 2f + avatarHeight).toInt()

            // 执行平滑滚动 (使用 smoothScrollTo)
            // 限制最小值为0，防止滚过头
            mViewBinding.scrollView.smoothScrollTo(0, Math.max(0, scrollTarget))
        }
    }

    private fun initCircularRingMenuView() {
        // 初始化菜单项
        val menuItems = ArrayList<CircularRingMenuView.MenuItem>()
        menuItems.add(CircularRingMenuView.MenuItem("选项A", Color.parseColor("#F44336")))
        menuItems.add(CircularRingMenuView.MenuItem("选项B", Color.parseColor("#4CAF50")))
        menuItems.add(CircularRingMenuView.MenuItem("选项C", Color.parseColor("#2196F3")))
        menuItems.add(CircularRingMenuView.MenuItem("选项D", Color.parseColor("#FFC107")))
        menuItems.add(CircularRingMenuView.MenuItem("选项E", Color.parseColor("#00DBFF")))
        mViewBinding.circularRingMenuView.setMenuItems(
            listOf("选项A", "选项B", "选项C", "选项D", "选项E"),
            listOf(
                Color.parseColor("#F44336"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#FFC107"),
                Color.parseColor("#00DBFF")
            )
        )
        mViewBinding.circularRingMenuView.setOnMenuItemClickListener { position ->
            val messages = arrayOf(
                "选项 A 被点击",
                "选项 B 被点击",
                "选项 C 被点击",
                "选项 D 被点击",
                "选项 E 被点击"
            )
            ToastUtils.showShort(messages[position])
        }
    }

}