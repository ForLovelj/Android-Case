package com.clow.customviewcase.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivityMarqueeTextViewBinding
import com.clow.customviewcase.widget.MarqueeTextView

/**
 * Created by clow
 * Des:
 * Date: 2023/3/21.
 */
class MarqueeTextViewActivity: BaseActivity<ActivityMarqueeTextViewBinding>() {

    private val mAdapter by lazy {
        MarqueeAdapter()
    }
    private val mData = mutableListOf<String>()
    override fun layoutId() = R.layout.activity_marquee_text_view

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.btn.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                mViewBinding.btn.text = "停止"
            } else {
                mViewBinding.btn.text = "开始"
            }
            mViewBinding.marqueeTextView.isMarquee = it.isSelected
        }


        with(mViewBinding.recyclerView) {
            layoutManager = GridLayoutManager(mContext,3)
            adapter = mAdapter
        }

        mData.add("qqqqqqqqqqqqqq")
        mData.add("wwwwwww")
        mData.add("rr")
        for (i in 0..50) {
            mData.add("$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i$i")
        }

        mAdapter.setNewInstance(mData)
    }
}

class MarqueeAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_marquee_text) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val marqueeTextView = holder.getView<MarqueeTextView>(R.id.marqueeTextView)
        marqueeTextView.text = item
        marqueeTextView.post {


            marqueeTextView.isMarquee = true
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        val marqueeTextView = holder.getView<MarqueeTextView>(R.id.marqueeTextView)
        marqueeTextView.isMarquee = false
    }
}