package com.clow.customviewcase

import android.content.Intent
import android.os.Bundle
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.adapter.StringAdapter
import com.clow.customviewcase.databinding.ActivityMainBinding
import com.clow.customviewcase.ui.MarqueeTextViewActivity
import com.clow.customviewcase.ui.TestViewActivity
import com.clow.customviewcase.ui.canvas.CanvasCaseActivity
import com.clow.customviewcase.ui.paint.PaintCaseActivity
import com.google.android.flexbox.FlexboxLayoutManager

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mAdapter by lazy {
        StringAdapter()
    }

    private val mNav = mutableListOf("Canvas","Paint","MarqueeTextViewActivity","TestViewActivity")

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        with(mViewBinding.recyclerView) {
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener{ adapter, view, position ->
            val name = mAdapter.data[position]
            when(name){
                "Canvas" -> {
                    startActivity(Intent(mContext, CanvasCaseActivity::class.java))
                }
                "Paint" -> {
                    startActivity(Intent(mContext, PaintCaseActivity::class.java))
                }
                "MarqueeTextViewActivity" -> {
                    startActivity(Intent(mContext, MarqueeTextViewActivity::class.java))
                }
                "TestViewActivity" -> {
                    startActivity(Intent(mContext, TestViewActivity::class.java))
                }
            }
        }
        mAdapter.setNewInstance(mNav)

    }
}
