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

//
//@BindingAdapter("items")
//fun setProducts(recyclerView: RecyclerView, productsSubject: Subject<List<Product>>) {
//    recyclerView.adapter?.apply {
//        if (this is ProductAdapter)
//            productsSubject
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    (recyclerView.adapter as ProductAdapter).stopLoadingMode()
//                    this.setProducts(it)
//                    (recyclerView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
//                        .hideSoftInputFromWindow(recyclerView.getWindowToken(), 0)
//                }
//    }
//}


//
//@BindingAdapter("refresh")
//fun setRefreshListener(swipeRefreshLayout: SwipeRefreshLayout, viewModel: ProductViewModel) {
//    swipeRefreshLayout.refreshes()
//        .flatMap {
//            viewModel.refresh()
//                .onExceptionResumeNext { swipeRefreshLayout.isRefreshing = false }
//        }
//        .subscribe {
//            viewModel.products = it
//            swipeRefreshLayout.isRefreshing = false
//        }
//}
