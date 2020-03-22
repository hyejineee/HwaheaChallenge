package com.hyejineee.hwahae.datasource.response

data class Response<T>(
    val statusCode:Int,
    val body:T)