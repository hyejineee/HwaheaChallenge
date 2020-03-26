package com.hyejineee.hwahae.views

import android.content.res.Configuration
import android.util.Log
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
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val productViewModel: ProductViewModel by viewModel()
    private val productDetailViewModel: ProductDetailViewModel by viewModel()

    override fun layoutResourceID(): Int = R.layout.activity_main

    private val productAdapter = ProductAdapter { productId ->
        val dialog = ProductDetailDialog(
            this,
            productId,
            productDetailViewModel
        )

        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
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
                        && productAdapter.itemCount >= 20
                    ) {
                        productAdapter.startLoadingMode()
                        this.smoothScrollToPosition(productAdapter.itemCount - 1)
                        productViewModel.actionDispatch(ActionType.NEXT_PAGE, 0)
                    }
                }.addTo(compositeDisposable)
        }

        val skinTypeMap = mutableMapOf<String, String>().apply {
            put("모든 피부 타입", "all")
            put("지성", "oily")
            put("건성", "dry")
            put("민감성", "sensitive")
        }

        viewDataBinding.skinTypeSp.selectionEvents()
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val selectedItem = it.view.selectedItem.toString()
                val skinType = skinTypeMap[selectedItem] ?: "all"
                productViewModel.actionDispatch(ActionType.FILTERING, skinType)
            }.addTo(compositeDisposable)

        viewDataBinding.searchEditTv.textChangeEvents()
            .skipInitialValue()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                productViewModel.actionDispatch(ActionType.SEARCH, it.text.toString())
            }.addTo(compositeDisposable)

    }

    override fun initSubscribe() {

        productViewModel.onProductChange
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isEmpty()) {
                    Toast.makeText(this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                }
                productAdapter.products = it
            }
            .addTo(compositeDisposable)

        productViewModel.onPagingModeChange
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!it) productAdapter.stopLoadingMode()
            }.addTo(compositeDisposable)

        productViewModel.onLoadingModeChange
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewDataBinding.loadingNoticeView.visibility = if (it) View.VISIBLE else View.GONE
                viewDataBinding.itemGridView.visibility = if (it) View.GONE else View.VISIBLE
            }
            .addTo(compositeDisposable)

        productViewModel.onErrorSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewDataBinding.refreshBtn.visibility = View.VISIBLE
                viewDataBinding.itemGridView.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }.addTo(compositeDisposable)
    }

    fun clearSearchText(view: View) = viewDataBinding.searchEditTv.setText("")

    fun reRequest(view: View) {
        productViewModel.actionDispatch(ActionType.DEFAULT_PRODUCTS, 0)
        viewDataBinding.refreshBtn.visibility = View.GONE
        viewDataBinding.itemGridView.visibility = View.VISIBLE
    }
}
