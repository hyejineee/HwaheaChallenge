package com.hyejineee.hwahae.datasource

import com.hyejineee.hwahae.datasource.ServerMessageCode.BodyMessage
import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.model.ProductDetail
import io.reactivex.Observable

class ProductRemoteDataSource(val APIService: APIService) : ProductDataSource {

    override fun getProductList(
        skin_type: String?,
        page_num: Int?,
        key_word: String?
    ): Observable<List<Product>> {
        return Observable.create { observer ->
            try {
                val r = APIService
                    .getProductList(skin_type, page_num, key_word)
                    .execute()

                when (r.isSuccessful) {
                    false -> observer.onError(Exception(r.message()))
                    else -> {
                        when {
                            r.message().contains(BodyMessage.NO_DATA.message) ->
                                observer.onNext(emptyList())
                            else -> observer.onNext(r.body()?.body ?: emptyList())
                        }
                        observer.onComplete()
                    }
                }
            } catch (err: Throwable) {
                if (!observer.isDisposed) {
                    observer.onError(err)
                }
            }
        }
    }

    override fun getProductDetail(product_id: Int): Observable<ProductDetail> {
        return Observable.create { observer ->
            try {
                val r = APIService
                    .getProductDetail(product_id)
                    .execute()

                when (r.isSuccessful) {
                    true -> {
                        observer.onNext(r.body()?.body ?: ProductDetail())
                        observer.onComplete()
                    }
                    else ->
                        observer.onError(Exception(r.message()))
                }
            } catch (err: Throwable) {
                observer.onError(err)
            }
        }
    }
}
