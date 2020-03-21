package com.hyejineee.hwahae.network

import com.google.gson.JsonObject
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
    ): Call<JsonObject>

    @GET("/prod/products/{id}")
    fun getProductDetail(@Path("id") id: Int): Call<JsonObject>
}
