package com.clow.animation_case

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.clow.animation_case.databinding.ActivityMainBinding
import com.clow.animation_case.ui.Fragment.*
import com.clow.baselib.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mNav = mutableListOf("视图动画","属性动画","揭露动画效果","曲线动画")

    private val mTabAdapter by lazy {
        TabAdapter(this,mNav)
    }

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        with(mViewBinding.viewPager2){
            adapter = mTabAdapter
            isUserInputEnabled = false //禁止滑动
        }
        //ViewPager2默认只加载当前页面，相当于官方处理了Fragment的懒加载问题
        //此时当你滑动ViewPager2时，滑动到某个Fragment页面才会加载，执行onCreateView()方法，
        //但是当你手动点击TabLayout时，此时懒加载就会失效，onCreateView()会被执行多次，
        //原因就是…此时ViewPager2默认是平滑滚动的，滚动滑过的Fragment都会被加载，
        //第二个boolean参数为smoothScroll 填false 这时点击 哪个tab 就会创建 哪个 fragment
        TabLayoutMediator(mViewBinding.tabLayout,mViewBinding.viewPager2,true,false) { tab, position ->
            tab.text = mNav[position]
        }.attach()

    }

}

class TabAdapter(activity: FragmentActivity,val titles: MutableList<String>) : FragmentStateAdapter(activity) {

    override fun getItemCount() = titles.size

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AnimationFragment()
            1 -> AnimatorFragment()
            2 -> CircularRevealFragment()
            3 -> CurveFragment()
            else -> TabFragment.newInstance(titles[position])
        }
    }

}