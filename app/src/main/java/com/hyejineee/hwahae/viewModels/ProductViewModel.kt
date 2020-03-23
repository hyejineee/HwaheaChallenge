package com.hyejineee.hwahae.viewModels

import android.util.Log
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

    private val actionSubject: Subject<Action> = PublishSubject.create()

    var skinType: String? = null
    var keyword: String? = null
    var pageNum = 1

    var products = mutableListOf<Product>()
        set(value) {
            onLoadingModeChange.onNext(false)
            onPagingModeChange.onNext(false)

            field = value
            onProductChange.onNext(field)
        }
    val onProductChange: Subject<List<Product>> = PublishSubject.create()

    val onErrorSubject: Subject<Throwable> = PublishSubject.create()
    val onLoadingModeChange: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val onPagingModeChange: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    val noItemSubject: Subject<Boolean> = PublishSubject.create()
    var isMoreLoad: Boolean = false

    init {
        setSubscribeActionSubject()
    }

    fun actionDispatch(action: ActionType, data: Any) {
        when (action) {
            ActionType.NEXT_PAGE -> onPagingModeChange.onNext(true)
            else -> onLoadingModeChange.onNext(true)
        }
        actionSubject.onNext(Action(action, data))
    }

    private fun setSubscribeActionSubject() {
        actionSubject.subscribe { action ->
            when (action.type) {
                ActionType.FILTERING -> {
                    skinType = if (action.data as String != "all") action.data else null
                    resetPage()
                }
                ActionType.SEARCH -> {
                    keyword = if ((action.data as String).trim().isNotEmpty()) action.data else null
                    resetPage()
                }
                ActionType.NEXT_PAGE -> pageNum += 1
                ActionType.REFRESH -> resetPage()
            }
            getProductList(currentAction(action.type))
        }.addTo(compositeDisposable)
    }

    private fun resetPage(){
        pageNum = 1
        products.clear()
    }

    private fun getProductList(appendProduct: (List<Product>) -> Unit) {
        Log.d("getProductList", "getProductList is called")
        productDataSource.getProductList(skinType, pageNum, keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { appendProduct(it) },
                {
                    onLoadingModeChange.onNext(false)
                    onPagingModeChange.onNext(false)
                    onErrorSubject.onNext(it)
                }
            ).addTo(compositeDisposable)
    }

    private val currentAction: (ActionType) -> (List<Product>) -> Unit = { actionType ->
        { it ->
            when (actionType) {
                ActionType.NEXT_PAGE -> products = products.plus(it).toMutableList()
                else ->{
                    products = it.toMutableList()
                }
            }
        }
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
