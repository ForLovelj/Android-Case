package com.clow.customviewcase

import android.content.Intent
import android.os.Bundle
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.adapter.StringAdapter
import com.clow.customviewcase.databinding.ActivityMainBinding
import com.clow.customviewcase.ui.canvas.CanvasCaseActivity
import com.google.android.flexbox.FlexboxLayoutManager

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mAdapter by lazy {
        StringAdapter()
    }

    private val mNav = mutableListOf("Canvas","Paint")

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
            }
        }
        mAdapter.setNewInstance(mNav)

    }
}
