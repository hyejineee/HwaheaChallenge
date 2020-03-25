package com.hyejineee.hwahae.util

import com.hyejineee.hwahae.datasource.ServerMessageCode.BodyMessage
import com.hyejineee.hwahae.datasource.ServerMessageCode.StatusCode
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request()).let {

            val responseStr = it.body()?.string()
            val newResponse = it.newBuilder()
            val toJson = JSONObject(responseStr)

            when(toJson.get("statusCode") ) {
                StatusCode.STATUS_SUCCESS.code -> {
                    newResponse.code(200)
                    newResponse.body(
                        ResponseBody.create(
                            it.body()?.contentType(),
                            responseStr
                        )
                    )
                }

                else -> {
                    when {
                        toJson.get("body").toString()
                            .contains(BodyMessage.NO_DATA.message) ->{
                            newResponse.code(200)
                            newResponse.body(
                                ResponseBody.create(
                                it.body()?.contentType(),
                                    "{\n" +
                                            "  \"statusCode\": 200,\n" +
                                            "  \"body\": []\n" +
                                            "}"
                            ))
                        }

                        else -> newResponse.code(400)
                    }
                    newResponse.message(toJson.get("body").toString())
                }
            }
        }.build()
}