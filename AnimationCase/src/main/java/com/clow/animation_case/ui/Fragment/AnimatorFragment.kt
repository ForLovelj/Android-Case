package com.clow.animation_case.ui.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.clow.animation_case.R
import com.clow.animation_case.databinding.FragmentAnimatorBinding
import com.clow.animation_case.ui.AnimatorActivity
import com.clow.animation_case.ui.InterpolatorActivity
import com.clow.animation_case.ui.adapter.NameAdapter
import com.clow.baselib.base.BaseFragment

/**
 * Created by clow
 * Des: 属性动画case
 *
 * Date: 2022/10/11.
 */
class AnimatorFragment: BaseFragment<FragmentAnimatorBinding>() {

    private val mAdapter by lazy {
        NameAdapter()
    }

    private val mNames = mutableListOf("基本使用","插值器")

    override fun layoutId() = R.layout.fragment_animator

    override fun initView(view: View, savedInstanceState: Bundle?) {
        with(mViewBinding.recyclerView) {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val value = mAdapter.data[position]
            when (position) {
                0 -> {
                    startActivity(Intent(requireContext(), AnimatorActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(requireContext(), InterpolatorActivity::class.java))
                }
            }
        }
        mAdapter.setNewInstance(mNames)
    }
}