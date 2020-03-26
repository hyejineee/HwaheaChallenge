package com.hyejineee.hwahae.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<T:ViewDataBinding>:AppCompatActivity(){

    protected lateinit var viewDataBinding:T
    val compositeDisposable = CompositeDisposable()
    abstract fun layoutResourceID(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = setContentView(this, layoutResourceID())
        initView()
        initSubscribe()
    }

    abstract fun initView()
    abstract fun initSubscribe()
}
