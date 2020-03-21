package com.hyejineee.hwahae.views.bindingAdapter

import android.annotation.SuppressLint
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.hyejineee.hwahae.viewModels.ProductViewModel
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
@BindingAdapter("searchListener")
fun setSearchListener(editText: EditText, viewModel: ProductViewModel) {
    editText.textChangeEvents()
        .debounce(1000, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            viewModel.search(it.text.toString())
        }
}
