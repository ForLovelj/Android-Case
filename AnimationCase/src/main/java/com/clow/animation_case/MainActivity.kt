package com.clow.animation_case

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.clow.animation_case.databinding.ActivityMainBinding
import com.clow.animation_case.ui.AnimationActivity
import com.clow.baselib.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mAdapter by lazy {
        MainAdapter()
    }
    private val mNav = mutableListOf("视图动画","属性动画")

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        with(mViewBinding.recyclerView) {
            layoutManager = GridLayoutManager(this@MainActivity,3)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val value = mAdapter.data[position]
            when (position) {
                0 -> {
                    startActivity(Intent(this, AnimationActivity::class.java))
                }
            }
        }
        mAdapter.setNewInstance(mNav)
        initListener()
    }

    private fun initListener() {


    }

}

class MainAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_main) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvName,item)
    }
}
