package com.clow.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.clow.baselib.ext.inflateBindingWithGeneric

/**
 * Created by clow
 * Des:
 * Date: 2022/8/26.
 */
abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {


    abstract fun layoutId(): Int

    lateinit var mViewBinding: VB
    lateinit var mContext: Context

    /**
     * 创建DataBinding
     */
    private fun initViewBinding(): View {
        mViewBinding = inflateBindingWithGeneric(layoutInflater)
        return mViewBinding.root

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(initViewBinding())
        initView(savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

}