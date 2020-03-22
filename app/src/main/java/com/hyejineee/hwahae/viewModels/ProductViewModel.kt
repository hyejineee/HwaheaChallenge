package com.hyejineee.hwahae.viewModels

import com.hyejineee.hwahae.Action
import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.datasource.ProductDataSource
import com.hyejineee.hwahae.model.Product
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductViewModel(
    private val productDataSource: ProductDataSource
) : BaseViewModel() {

    var skinType: String? = null
    var keyword: String? = null
    var pageNum = 1

    var products = listOf<Product>()
        set(value) {
            onLoadingModeChange.onNext(false)
            onPagingModeChange.onNext(false)

            field = value
            onProductChange.onNext(value)
        }
    val onProductChange: Subject<List<Product>> = PublishSubject.create()

    private val actionSubject: Subject<Action> = PublishSubject.create()

    val onErrorSubject: Subject<Throwable> = PublishSubject.create()

    val onLoadingModeChange: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val onPagingModeChange : BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    val noItemSubject: Subject<Boolean> = PublishSubject.create()
    var isMoreLoad: Boolean = false

    init {
        setSubscribeActionSubject()
    }

    private fun setSubscribeActionSubject(){
        actionSubject.subscribe {
            when (it.type) {
                ActionType.FILTERING ->
                    skinType = if (it.data as String != "all") it.data else null
                ActionType.NEXT_PAGE -> pageNum += 1
                ActionType.SEARCH ->
                    keyword = if ((it.data as String).trim().isNotEmpty()) it.data else null
            }
            getProductList()
        }.addTo(compositeDisposable)
    }

    fun actionDispatch(action: ActionType, data: Any) {
        when(action){
            ActionType.NEXT_PAGE -> onPagingModeChange.onNext(true)
            else -> onLoadingModeChange.onNext(true)
        }
        actionSubject.onNext(Action(action, data))
    }

    private fun getProductList() {
        productDataSource.getProductList(skinType, pageNum, keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                products = it
            }.addTo(compositeDisposable)
    }

    //    private fun cancleLoadMode() {
//        isMoreLoad = false
//        isLoadingSubject.onNext(false)
//    }

//    @SuppressLint("CheckResult")
//    fun increasePageNumber() {
//        isMoreLoad = true
//        pageNumberIncreaseSubject.onNext(Unit)
//    }
//
//    fun selectSkinType(skinType: String) = skinTypeChangeSubject.onNext(skinType)
//
//    fun search(keyword: String) = searchChangeSubject.onNext(keyword)

//    fun refresh(): Observable<List<Product>> {
//        this.pageNum = 1
//        return getProductList()
//    }



//    @SuppressLint("CheckResult")
//    fun getProducts() {
//        isLoadingSubject.onNext(true)
//        getProductList()
//            .subscribe({
//                noItemSubject.onNext(it.isEmpty())
//                products = it
//                cancleLoadMode()
//            }, {
//                onErrorSubject.onNext(it)
//                cancleLoadMode()
//            })
//    }

//    @SuppressLint("CheckResult")
//    fun setEvents() {
//
//            merge(
//                skinTypeChangeSubject
//                    .map { skinType = it },
//                searchChangeSubject
//                    .map { keyword = it }
//            )
//                .map { pageNum = 1 }
//                .map { isLoadingSubject.onNext(true) }
//                .flatMap { getProductList() }
//                .subscribe({
//                    noItemSubject.onNext(it.isEmpty())
//                    products = it
//                    cancleLoadMode()
//                }, {
//                    onErrorSubject.onNext(it)
//                    cancleLoadMode()
//                })
//
//
//
//            pageNumberIncreaseSubject
//                .filter { products.isNotEmpty() }
//                .map { pageNum += 1 }
//                .flatMap { getProductList() }
//                .subscribe({
//                    products = products.plus(it)
//                    cancleLoadMode()
//                }, {
//                    pageNum -= 1
//                    onErrorSubject.onNext(it)
//                    cancleLoadMode()
//                })
//
//    }
//
}
