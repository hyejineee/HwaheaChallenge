package com.hyejineee.hwahae.util

import com.hyejineee.hwahae.datasource.ServerMessageCode.StatusCode
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request()).let {

            val responseStr = it.body()?.string()

            val newResponse = it.newBuilder().apply {
                val toJson = JSONObject(responseStr)

                when (toJson.getInt("statusCode")) {
                    StatusCode.STATUS_SUCCESS.code ->
                        this.code(200)

                    else -> {
                        this.code(400)
                        this.message(toJson["body"].toString())
                    }
                }
            }

            newResponse.body(
                ResponseBody.create(
                    it.body()?.contentType(),
                    responseStr
                )
            )
        }.build()
}