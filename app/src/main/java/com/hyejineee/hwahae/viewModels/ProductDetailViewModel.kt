package com.hyejineee.hwahae.viewModels

import android.annotation.SuppressLint
import com.hyejineee.hwahae.model.ProductDetail
import com.hyejineee.hwahae.datasource.ProductDataSource
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductDetailViewModel(val productDataSource: ProductDataSource) {

    val productDeailSubject: Subject<ProductDetail> = PublishSubject.create()
    val onErrorSubject: Subject<Throwable> = PublishSubject.create()
    val isLoadingSubject: Subject<Boolean> = PublishSubject.create()

    @SuppressLint("CheckResult")
    fun getProductDetail(productId: Int) {
        isLoadingSubject.onNext(true)
        addDisposable(
            productDataSource.getProductDetail(productId)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({
                    isLoadingSubject.onNext(false)
                    productDeailSubject.onNext(it)
                }, { error ->
                    isLoadingSubject.onNext(false)
                    onErrorSubject.onNext(error)
                })
        )
    }
}
