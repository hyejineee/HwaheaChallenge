package com.hyejineee.hwahae.datasource

import android.util.Log
import com.hyejineee.hwahae.datasource.ServerMessageCode.BodyMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.model.ProductDetail
import com.hyejineee.hwahae.datasource.ServerMessageCode.StatusCode
import io.reactivex.Observable
import java.lang.reflect.Type

class ProductRemoteDataSource(val APIService: APIService) : ProductDataSource {

    override fun getProductList(
        skin_type: String?,
        page_num: Int?,
        key_word: String?
    ): Observable<List<Product>> {
        return Observable.create { observer ->
            try {
                Log.d("request", "skinType : $skin_type pageNum : $page_num keyword:$key_word" )
                val r = APIService
                    .getProductList(skin_type, page_num, key_word)
                    .execute()

                when (r.isSuccessful) {
                    true -> {
                        Log.d("response", "msg ${r.message()}")
                        when{
                            r.message().contains(BodyMessage.NO_DATA.message) ->
                                observer.onNext(emptyList())
                            else ->  observer.onNext(r.body()?.body ?: emptyList())
                        }
                        observer.onComplete()
                    }
                    else -> observer.onError(Exception(r.message()))
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
