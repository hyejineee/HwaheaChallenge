package com.hyejineee.hwahae.network

import com.hyejineee.hwahae.network.ServerMessageCode.BodyMessage
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.model.ProductDetail
import com.hyejineee.hwahae.network.ServerMessageCode.StatusCode
import io.reactivex.Observable
import java.lang.reflect.Type

class ProductRepoImpl(val APIService: APIService) : ProductRepo {

    override fun getProductList(
        skin_type: String?,
        page_num: Int?,
        key_word: String?
    ): Observable<List<Product>> {
        return Observable.create { observer ->
            try {
                val response = APIService
                    .getProductList(skin_type, page_num, key_word)
                    .execute()
                    .body()

                val statusCode = response?.get("statusCode")?.asInt
                val body = response?.get("body")
                when (statusCode) {
                    StatusCode.STATUS_ERROR.code -> {
                        when {
                            body.toString().contains(BodyMessage.PAGE_ERROR.message) ||
                                    body.toString().equals(BodyMessage.SKIN_TYPE_ERROR.toString()) -> {
                                observer.onError(Exception(body.toString()))
                            }
                            body.toString().contains(BodyMessage.NO_DATA.message) -> {
                                observer.onNext(emptyList())
                                observer.onComplete()
                            }
                            else -> observer.onError(Exception(body.toString()))
                        }

                    }
                    StatusCode.STATUS_SUCCESS.code -> {
                        val listType: Type = object :
                            TypeToken<List<Product?>?>() {}.getType()
                        val products = Gson().fromJson<List<Product>>(
                            body,
                            listType
                        )
                        observer.onNext(products)
                        observer.onComplete()
                    }
                    else ->
                        observer.onError(Exception(body.toString()))
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
                val response = APIService
                    .getProductDetail(product_id)
                    .execute()
                    .body()

                val statusCode = response?.get("statusCode")?.asInt
                val body = response?.get("body")
                when (statusCode) {
                    StatusCode.STATUS_ERROR.code -> observer.onError(Exception(body.toString()))
                    StatusCode.STATUS_SUCCESS.code -> {
                        val productDetail = Gson().fromJson<ProductDetail>(
                            body,
                            ProductDetail::class.java
                        )

                        observer.onNext(productDetail)
                        observer.onComplete()
                    }
                    else ->
                        observer.onError(Exception(body.toString()))
                }
            } catch (err: Throwable) {
                observer.onError(err)
            }

        }
    }
}
