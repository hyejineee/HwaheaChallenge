package com.hyejineee.hwahae.datasource

import com.google.gson.JsonObject
import com.hyejineee.hwahae.datasource.response.Response
import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.model.ProductDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/prod/products")
    fun getProductList(
        @Query("skin_type") skin_type: String?,
        @Query("page") page_num: Int?,
        @Query("search") key_word: String?
    ): Call<Response<List<Product>>>

    @GET("/prod/products/{id}")
    fun getProductDetail(@Path("id") id: Int): Call<Response<ProductDetail>>
}
