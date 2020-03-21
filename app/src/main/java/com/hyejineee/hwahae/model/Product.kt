package com.hyejineee.hwahae.model

data class Product(
    val id: Int = -1,
    val thumbnail_image: String = "",
    val title: String = "",
    val price: String = "",
    val oily_score: Int = -1,
    val dry_score: Int = -1,
    val sensitive_score: Int = -1
)
