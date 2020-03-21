package com.hyejineee.hwahae.viewModels

import android.annotation.SuppressLint
import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.datasource.ProductDataSource
import io.reactivex.Observable
import io.reactivex.Observable.merge
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductViewModel(
    private val productDataSource: ProductDataSource
)  {

    var skinType: String? = null
    var keyword: String? = null
    var pageNum = 1
    var products = listOf<Product>()
        set(value) {
            field = value
            productsSubject.onNext(value)
        }

    val productsSubject: Subject<List<Product>> = PublishSubject.create()
    private val skinTypeChangeSubject: Subject<String> = PublishSubject.create()
    private val pageNumberIncreaseSubject: Subject<Unit> = PublishSubject.create()
    private val searchChangeSubject: Subject<String> = PublishSubject.create()

    val onErrorSubject: Subject<Throwable> = PublishSubject.create()
    val isLoadingSubject: Subject<Boolean> = PublishSubject.create()
    val noItemSubject: Subject<Boolean> = PublishSubject.create()
    var isMoreLoad: Boolean = false

    init {
        setEvents()
    }

    private fun cancleLoadMode() {
        isMoreLoad = false
        isLoadingSubject.onNext(false)
    }

    @SuppressLint("CheckResult")
    fun increasePageNumber() {
        isMoreLoad = true
        pageNumberIncreaseSubject.onNext(Unit)
    }

    @SuppressLint("CheckResult")
    fun selectSkinType(skinType: String) = skinTypeChangeSubject.onNext(skinType)

    @SuppressLint("CheckResult")
    fun search(keyword: String) = searchChangeSubject.onNext(keyword)

    fun refresh(): Observable<List<Product>> {
        this.pageNum = 1
        return getProductList()
    }

    private fun getProductList(): Observable<List<Product>> {
        val skinType = if (this.skinType === "all") null else this.skinType
        val keyword = if (this.keyword == "") null else this.keyword

        return productDataSource.getProductList(skinType, pageNum, keyword)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnError { throwable -> onErrorSubject.onNext(throwable) }
    }

    @SuppressLint("CheckResult")
    fun getProducts() {
        isLoadingSubject.onNext(true)
        getProductList()
            .subscribe({
                noItemSubject.onNext(it.isEmpty())
                products = it
                cancleLoadMode()
            }, {
                onErrorSubject.onNext(it)
                cancleLoadMode()
            })
    }

    @SuppressLint("CheckResult")
    fun setEvents() {
        addDisposable(
            merge(
                skinTypeChangeSubject
                    .map { skinType = it },
                searchChangeSubject
                    .map { keyword = it }
            )
                .map { pageNum = 1 }
                .map { isLoadingSubject.onNext(true) }
                .flatMap { getProductList() }
                .subscribe({
                    noItemSubject.onNext(it.isEmpty())
                    products = it
                    cancleLoadMode()
                }, {
                    onErrorSubject.onNext(it)
                    cancleLoadMode()
                })
        )

        addDisposable(
            pageNumberIncreaseSubject
                .filter { products.isNotEmpty() }
                .map { pageNum += 1 }
                .flatMap { getProductList() }
                .subscribe({
                    products = products.plus(it)
                    cancleLoadMode()
                }, {
                    pageNum -= 1
                    onErrorSubject.onNext(it)
                    cancleLoadMode()
                })
        )
    }

}
