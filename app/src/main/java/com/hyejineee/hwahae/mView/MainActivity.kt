package com.hyejineee.hwahae.mView

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hyejineee.hwahae.R
import com.hyejineee.hwahae.databinding.ActivityMainBinding
import com.hyejineee.hwahae.mViewModel.ProductDetailViewModel
import com.hyejineee.hwahae.mViewModel.ProductViewModel
import com.hyejineee.hwahae.util.SpacesItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override lateinit var viewDataBinding: ActivityMainBinding
    private val productViewModel: ProductViewModel by viewModel()
    private val productDetailViewModel: ProductDetailViewModel by viewModel()

    override fun layoutResourceID(): Int = R.layout.activity_main

    override fun initView() {
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = viewDataBinding.itemGridView.adapter as ProductAdapter
                when (adapter.getItemViewType(position)) {
                    adapter.PRODUCT_ITEM -> return 1
                    adapter.LOADING_ITEM -> return 2
                }
                return -1
            }
        }
        viewDataBinding.itemGridView.layoutManager = layoutManager
        viewDataBinding.itemGridView.addItemDecoration(SpacesItemDecoration(7))
    }

    override fun initDataBinding() {
        viewDataBinding.productViewModel = this.productViewModel
        viewDataBinding.productDetailViewModel = this.productDetailViewModel
        viewDataBinding.activity = this
    }

    override fun initSubscribe() {
        productViewModel.addDisposable(
            productViewModel.onErrorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewDataBinding.refreshBtn.isVisible = true
                    viewDataBinding.itemGridView.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        )
    }

    fun clearSerchText() = viewDataBinding.searchEditTv.setText("")

    fun reRequest() {
        productViewModel.getProducts()
        viewDataBinding.refreshBtn.isVisible = false
        viewDataBinding.itemGridView.isVisible = true
    }
}
