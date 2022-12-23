package com.clow.customviewcase.ui.canvas

import android.content.Intent
import android.os.Bundle
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.adapter.StringAdapter
import com.clow.customviewcase.databinding.ActivityCanvasCaseBinding
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * Created by clow
 * Des: canvas case
 * Date: 2022/12/21.
 */
class CanvasCaseActivity: BaseActivity<ActivityCanvasCaseBinding>() {

    private val mAdapter by lazy {
        StringAdapter()
    }

    private val mNav = mutableListOf(
        "绘制坐标系","平移画布","缩放画布","旋转画布","错切画布",
        "drawColor","drawPoint","drawLine","drawRect","drawRoundRect",
        "drawOval","drawCircle","drawArc","drawBitmap","drawPicture",
    )

    override fun layoutId() = R.layout.activity_canvas_case

    override fun initView(savedInstanceState: Bundle?) {
        with(mViewBinding.recyclerView) {
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener{ adapter, view, position ->
            startActivity(Intent(mContext, CanvasCaseDetailActivity::class.java).apply {
                putExtra("type",position)
            })
//            when(position){
//                0 -> {
//                    startActivity(Intent(mContext, CanvasCaseDetailActivity::class.java).apply {
//                        putExtra("type",position)
//                    })
//                }
//            }
        }

        mAdapter.setNewInstance(mNav)
    }
}