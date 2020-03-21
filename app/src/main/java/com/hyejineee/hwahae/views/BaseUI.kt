package com.hyejineee.hwahae.views

import androidx.databinding.ViewDataBinding

interface BaseUI<T:ViewDataBinding>{

    var viewDataBinding:T
    abstract fun layoutResourceID(): Int

    abstract fun initView()
    abstract fun initSubscribe()
}
