package com.hyejineee.hwahae.network

import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.model.ProductDetail
import io.reactivex.Observable

interface ProductRepo {
    fun getProductList(
        skin_type: String?,
        page_num: Int?,
        key_word: String?
    ): Observable<List<Product>>

    fun getProductDetail(product_Id: Int): Observable<ProductDetail>
}
