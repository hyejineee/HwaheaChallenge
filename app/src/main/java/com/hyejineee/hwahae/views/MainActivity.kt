package com.hyejineee.hwahae.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.R
import com.hyejineee.hwahae.databinding.ActivityMainBinding
import com.hyejineee.hwahae.util.SpacesItemDecoration
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel
import com.hyejineee.hwahae.viewModels.ProductViewModel
import com.jakewharton.rxbinding3.view.scrollChangeEvents
import com.jakewharton.rxbinding3.widget.selectionEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val productViewModel: ProductViewModel by viewModel()
    private val productDetailViewModel: ProductDetailViewModel by viewModel()

    override fun layoutResourceID(): Int = R.layout.activity_main

    private val productAdapter = ProductAdapter { productId ->
        val dialog = ProductDetailDialog(
            this,
            productId
        )

        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewDataBinding.productViewModel = this.productViewModel
//        viewDataBinding.productDetailViewModel = this.productDetailViewModel
//        viewDataBinding.activity = this
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
        viewDataBinding.itemGridView.apply {
            this.layoutManager = layoutManager
            addItemDecoration(SpacesItemDecoration(7))
            adapter = productAdapter
            this.scrollChangeEvents()
                .filter { productViewModel.products.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (layoutManager.findLastCompletelyVisibleItemPosition()
                        == productAdapter.itemCount.minus(1)
                        && !productViewModel.onPagingModeChange.blockingFirst()
                    ) {
                        this.smoothScrollToPosition(productAdapter.itemCount - 1)
                        productViewModel.actionDispatch(ActionType.NEXT_PAGE,0)
                    }
                }.addTo(compositeDisposable)
        }

        val skinType = mutableMapOf<String, String>().apply {
            put("모든 피부 타입", "all")
            put("지성", "oily")
            put("건성", "dry")
            put("민감성", "sensitive")
        }

        viewDataBinding.skinTypeSp.selectionEvents()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                val selectedItem = it.view.selectedItem.toString()
                val skinType = skinType[selectedItem] ?: "all"
                productViewModel.actionDispatch(ActionType.FILTERING, skinType)
            }.addTo(compositeDisposable)
    }

    override fun initSubscribe() {
        productViewModel.onProductChange
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                productAdapter.products = it
            }.addTo(compositeDisposable)

        productViewModel.onPagingModeChange
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(!it) productAdapter.stopLoadingMode()
            }.addTo(compositeDisposable)

        productViewModel.onErrorSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewDataBinding.refreshBtn.visibility = View.VISIBLE
                viewDataBinding.itemGridView.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }.addTo(compositeDisposable)

    }

    fun clearSearchText(view: View) = viewDataBinding.searchEditTv.setText("")

    fun reRequest(view:View) {
        productViewModel.actionDispatch(ActionType.REFRESH, 0)
        viewDataBinding.refreshBtn.visibility = View.GONE
        viewDataBinding.itemGridView.visibility = View.VISIBLE
    }
}
