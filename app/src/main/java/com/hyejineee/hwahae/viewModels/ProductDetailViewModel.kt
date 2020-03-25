package com.hyejineee.hwahae.viewModels

import com.hyejineee.hwahae.Action
import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.util.BaseSchedulers
import com.hyejineee.hwahae.datasource.ProductDataSource
import com.hyejineee.hwahae.model.ProductDetail
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductDetailViewModel(
    private val productDataSource: ProductDataSource,
    private val schedulers: BaseSchedulers
) : BaseViewModel() {

    var productDetail = ProductDetail()
        set(value) {
            onLoadingModeChange.onNext(false)
            field = value
            onProductDetailChange.onNext(field)
        }
    val onProductDetailChange: Subject<ProductDetail> = PublishSubject.create()

    private val onActionSubject: Subject<Action> = PublishSubject.create()
    val onErrorSubject: Subject<Throwable> = PublishSubject.create()
    val onLoadingModeChange: Subject<Boolean> = BehaviorSubject.createDefault(false)

    init {
        setSubscribeAction()
    }

    fun actionDispatch(action: ActionType, data: Any) {
        onLoadingModeChange.onNext(true)
        onActionSubject.onNext(Action(action, data))
    }

    private fun setSubscribeAction() {
        onActionSubject.subscribe {
            getProductDetail(it.data as Int)
        }.addTo(compositeDisposable)
    }

    private fun getProductDetail(productId: Int) {
        productDataSource.getProductDetail(productId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                productDetail = it
            }, { error ->
                onLoadingModeChange.onNext(false)
                onErrorSubject.onNext(error)
            }).addTo(compositeDisposable)
    }
}
