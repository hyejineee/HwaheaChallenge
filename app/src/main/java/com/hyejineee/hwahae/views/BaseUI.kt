package com.hyejineee.hwahae.views

import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable

interface BaseUI<T:ViewDataBinding>{

    abstract fun layoutResourceID(): Int
    abstract fun initView()
    abstract fun initSubscribe()
}
