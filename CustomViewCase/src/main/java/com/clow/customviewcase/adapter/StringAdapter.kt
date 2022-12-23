package com.clow.customviewcase.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.clow.customviewcase.R

/**
 * Created by clow
 * Des:
 * Date: 2022/12/21.
 */
class StringAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main){
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvName,item)
    }

}