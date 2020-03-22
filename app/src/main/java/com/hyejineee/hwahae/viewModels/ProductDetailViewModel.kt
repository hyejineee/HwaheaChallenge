package com.hyejineee.hwahae.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.hyejineee.hwahae.model.ProductDetail
import com.hyejineee.hwahae.datasource.ProductDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductDetailViewModel(private val productDataSource: ProductDataSource): BaseViewModel() {

    val productDetailSubject: Subject<ProductDetail> = PublishSubject.create()
    val onErrorSubject: Subject<Throwable> = PublishSubject.create()
    val isLoadingSubject: Subject<Boolean> = PublishSubject.create()

    @SuppressLint("CheckResult")
    fun getProductDetail(productId: Int) {
        isLoadingSubject.onNext(true)

            productDataSource.getProductDetail(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isLoadingSubject.onNext(false)
                    productDetailSubject.onNext(it)
                }, { error ->
                    isLoadingSubject.onNext(false)
                    onErrorSubject.onNext(error)
                })

    }
}
