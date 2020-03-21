package com.hyejineee.hwahae.mView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T:ViewDataBinding> : AppCompatActivity(), BaseInit {
    protected abstract var viewDataBinding:T

    abstract fun layoutResourceID(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceID())
        viewDataBinding.lifecycleOwner = this

        initView()
        initDataBinding()
        initSubscribe()
    }
}
