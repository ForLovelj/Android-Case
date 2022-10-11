package com.clow.animation_case.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.clow.animation_case.R

/**
 * Created by clow
 * Des:
 * Date: 2022/10/11.
 */
class NameAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvName,item)
    }
}