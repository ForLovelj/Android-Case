package com.clow.customviewcase.ui.canvas

import android.os.Bundle
import com.clow.baselib.base.BaseActivity
import com.clow.customviewcase.R
import com.clow.customviewcase.databinding.ActivityCanvasDetailCaseBinding

/**
 * Created by clow
 * Des:
 * Date: 2022/12/21.
 */
class CanvasCaseDetailActivity: BaseActivity<ActivityCanvasDetailCaseBinding>() {
    override fun layoutId() = R.layout.activity_canvas_detail_case

    override fun initView(savedInstanceState: Bundle?) {

        val type = intent.getIntExtra("type", 0)
        mViewBinding.simpleView.drawForType(type)
    }
}