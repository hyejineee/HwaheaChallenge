package com.hyejineee.hwahae.model

data class ProductDetail(
    val id: Int = -1,
    val full_size_image: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val oily_score: Int = -1,
    val dry_score: Int = -1,
    val sensitive_score: Int = -1
)
