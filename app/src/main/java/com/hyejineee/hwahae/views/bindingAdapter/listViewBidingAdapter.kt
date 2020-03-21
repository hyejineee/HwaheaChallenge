package com.hyejineee.hwahae.views.bindingAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hyejineee.hwahae.views.ProductAdapter
import com.hyejineee.hwahae.views.ProductDetailDialog
import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel
import com.hyejineee.hwahae.viewModels.ProductViewModel
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.scrollChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.Subject

@BindingAdapter("adapter")
fun setAdapter(recyclerView: RecyclerView, viewModel: ProductDetailViewModel) {
    recyclerView.adapter ?: run {
        recyclerView.adapter = ProductAdapter().apply {
            productDetailViewModel = viewModel
        }
    }
}

@BindingAdapter("productViewModel")
fun setProductViewModel(recyclerView: RecyclerView, productViewModel: ProductViewModel) {
    recyclerView.adapter?.apply {
        if (this is ProductAdapter) {
            this.productViewModel = productViewModel
        }
    }
}

@BindingAdapter("items")
fun setProducts(recyclerView: RecyclerView, productsSubject: Subject<List<Product>>) {
    recyclerView.adapter?.apply {
        if (this is ProductAdapter)
            productsSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    (recyclerView.adapter as ProductAdapter).stopLoadingMode()
                    this.setProducts(it)
                    (recyclerView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(recyclerView.getWindowToken(), 0)
                }
    }
}

@SuppressLint("CheckResult")
@BindingAdapter("scrollListener")
fun setScrollListener(recyclerView: RecyclerView, viewModel: ProductViewModel) {
    recyclerView.scrollChangeEvents()
        .filter { viewModel.products.isNotEmpty() }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val adapter = recyclerView.adapter as ProductAdapter

            if (layoutManager.findLastCompletelyVisibleItemPosition()
                == recyclerView.adapter?.itemCount?.minus(1)
                && !adapter.productViewModel.isMoreLoad
            ) {
                adapter?.initLoadingMode()
                recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                viewModel.increasePageNumber()
            }
        }
}

@SuppressLint("CheckResult")
@BindingAdapter("itemClick", "viewModel")
fun onclick(view: ConstraintLayout, productId: Int, viewModel: ProductDetailViewModel) {
    val dialog = ProductDetailDialog(
        view.context,
        productId,
        viewModel
    )
    view.clicks()
        .filter { !dialog.isShowing }
        .subscribe {
            dialog.show()
        }
}

@BindingAdapter("refresh")
fun setRefershListener(swipeRefreshLayout: SwipeRefreshLayout, viewModel: ProductViewModel) {
    swipeRefreshLayout.refreshes()
        .flatMap {
            viewModel.refresh()
                .onExceptionResumeNext { swipeRefreshLayout.isRefreshing = false }
        }
        .subscribe {
            viewModel.products = it
            swipeRefreshLayout.isRefreshing = false
        }
}
