package com.clow.animation_case.ui.Fragment

import android.os.Bundle
import android.view.View
import com.clow.animation_case.R
import com.clow.animation_case.databinding.FragmentTabBinding
import com.clow.baselib.base.BaseFragment

/**
 * Created by clow
 * Des:
 * Date: 2022/10/11.
 */
class TabFragment: BaseFragment<FragmentTabBinding>() {

    companion object{
        fun newInstance(title: String): TabFragment {
            val fragment = TabFragment()
            fragment.arguments = Bundle().apply {
                putString("title",title)
            }
            return fragment
        }
    }

    override fun layoutId() = R.layout.fragment_tab

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mViewBinding.tvTitle.text = arguments?.getString("title","--")
    }
}