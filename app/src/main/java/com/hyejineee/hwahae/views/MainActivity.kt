package com.hyejineee.hwahae.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.GridLayoutManager
import com.hyejineee.hwahae.R
import com.hyejineee.hwahae.databinding.ActivityMainBinding
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel
import com.hyejineee.hwahae.viewModels.ProductViewModel
import com.hyejineee.hwahae.util.SpacesItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), BaseUI<ActivityMainBinding> {

    override lateinit var viewDataBinding: ActivityMainBinding
    private val productViewModel: ProductViewModel by viewModel()
    private val productDetailViewModel: ProductDetailViewModel by viewModel()

    override fun layoutResourceID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = setContentView(this, layoutResourceID())

        initView()
        initSubscribe()

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
        productViewModel.addDisposable(
            productViewModel.onErrorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewDataBinding.refreshBtn.visibility = View.VISIBLE
                    viewDataBinding.itemGridView.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        )
    }

    fun clearSearchText() = viewDataBinding.searchEditTv.setText("")

    fun reRequest() {
        productViewModel.getProducts()
        viewDataBinding.refreshBtn.visibility = View.GONE
        viewDataBinding.itemGridView.visibility =  View.VISIBLE
    }
}
