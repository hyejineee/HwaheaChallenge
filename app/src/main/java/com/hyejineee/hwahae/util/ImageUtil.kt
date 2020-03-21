package com.hyejineee.hwahae.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(this.getContext())
        .load(imageUrl)
        .centerCrop()
        .into(this)
}

@BindingAdapter("thumnailImage")
fun ImageView.loadThumnailImage(imageUrl: String) {
    Glide.with(this.getContext())
        .load(imageUrl)
        .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(30, 0)))
        .into(this)
}
