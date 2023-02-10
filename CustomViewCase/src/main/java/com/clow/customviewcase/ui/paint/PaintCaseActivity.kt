package com.clow.customviewcase.ui.paint

import android.content.Intent
import android.os.Bundle
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.adapter.StringAdapter
import com.clow.customviewcase.databinding.ActivityPaintCaseBinding
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * Created by clow
 * Des: paint case
 * Date: 2022/12/21.
 */
class PaintCaseActivity: BaseActivity<ActivityPaintCaseBinding>() {

    private val mAdapter by lazy {
        StringAdapter()
    }

    private val mNav = mutableListOf(
        "setStyle","setStrokeWidth","setStrokeCap","setStrokeJoin","setStrokeMiter",
        "setXfermode","setColor","setARGB","setShader","setColorFilter",
        "setMaskFilter","setShadowLayer","setPathEffect","setTextSize","setTypeface",
        "setFakeBoldText","setTextAlign","setUnderlineText","setStrikeThruText","setTextScaleX",
        "setTextSkewX","setLetterSpacing","drawText","getFontSpacing","getTextBounds",
        "breakText"
    )

    override fun layoutId() = R.layout.activity_paint_case

    override fun initView(savedInstanceState: Bundle?) {
        with(mViewBinding.recyclerView) {
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener{ adapter, view, position ->
            startActivity(Intent(mContext, PaintCaseDetailActivity::class.java).apply {
                putExtra("type",position)
            })
        }

        mAdapter.setNewInstance(mNav)
    }
}