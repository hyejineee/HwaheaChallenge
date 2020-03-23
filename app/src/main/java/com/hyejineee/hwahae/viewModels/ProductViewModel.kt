package com.hyejineee.hwahae.viewModels

import android.util.Log
import com.hyejineee.hwahae.Action
import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.BaseSchedulers
import com.hyejineee.hwahae.datasource.ProductDataSource
import com.hyejineee.hwahae.model.Product
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ProductViewModel(
    private val productDataSource: ProductDataSource, private val scheduler: BaseSchedulers
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
        productDataSource.getProductList(skinType, pageNum, keyword)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
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
}
