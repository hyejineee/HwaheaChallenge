package com.hyejineee.hwahae.mViewModel

import android.annotation.SuppressLint
import com.hyejineee.hwahae.model.ProductDetail
import com.hyejineee.hwahae.network.ProductRepo
import com.hyejineee.hwahae.util.BaseSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductDetailViewModel(val productRepo: ProductRepo, val scheduler: BaseSchedulers) :
    BaseViewModel() {

    val productDeailSubject: Subject<ProductDetail> = PublishSubject.create()
    val onErrorSubject: Subject<Throwable> = PublishSubject.create()
    val isLoadingSubject: Subject<Boolean> = PublishSubject.create()

    @SuppressLint("CheckResult")
    fun getProductDetail(productId: Int) {
        isLoadingSubject.onNext(true)
        addDisposable(
            productRepo.getProductDetail(productId)
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
