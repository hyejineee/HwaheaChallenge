package com.hyejineee.hwahae.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.hyejineee.hwahae.R
import com.hyejineee.hwahae.databinding.ActivityMainBinding
import com.hyejineee.hwahae.util.SpacesItemDecoration
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel
import com.hyejineee.hwahae.viewModels.ProductViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :BaseActivity<ActivityMainBinding>() {

    private val productViewModel: ProductViewModel by viewModel()
    private val productDetailViewModel: ProductDetailViewModel by viewModel()

    override fun layoutResourceID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.productViewModel = this.productViewModel
        viewDataBinding.productDetailViewModel = this.productDetailViewModel
        viewDataBinding.activity = this
    }

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

    override fun initSubscribe() {

            productViewModel.onErrorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewDataBinding.refreshBtn.visibility = View.VISIBLE
                    viewDataBinding.itemGridView.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }.addTo(compositeDisposable)

    }

    fun clearSearchText() = viewDataBinding.searchEditTv.setText("")

    fun reRequest() {
        productViewModel.getProducts()
        viewDataBinding.refreshBtn.visibility = View.GONE
        viewDataBinding.itemGridView.visibility =  View.VISIBLE
    }


}
