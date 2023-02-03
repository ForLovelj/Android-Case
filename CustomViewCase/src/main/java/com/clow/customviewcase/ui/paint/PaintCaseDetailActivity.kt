package com.clow.customviewcase.ui.paint

import android.os.Bundle
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivityPaintDetailCaseBinding

/**
 * Created by clow
 * Des:
 * Date: 2022/12/21.
 */
class PaintCaseDetailActivity: BaseActivity<ActivityPaintDetailCaseBinding>() {
    override fun layoutId() = R.layout.activity_paint_detail_case

    override fun initView(savedInstanceState: Bundle?) {

        val type = intent.getIntExtra("type", 0)
        mViewBinding.paintCaseView.drawForType(type)
    }
}