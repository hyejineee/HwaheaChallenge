package com.hyejineee.hwahae.mView.bindingAdapter

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.hyejineee.hwahae.mViewModel.ProductViewModel
import com.jakewharton.rxbinding3.widget.selectionEvents
import io.reactivex.android.schedulers.AndroidSchedulers

@BindingAdapter("spinnerListener")
fun setSpinnerListener(spinner: Spinner, viewModel: ProductViewModel) {
    val skin_type = mutableMapOf<String, String>().apply {
        put("모든 피부 타입", "all")
        put("지성", "oily")
        put("건성", "dry")
        put("민감성", "sensitive")
    }
    spinner.selectionEvents()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            val selectedItem = it.view.selectedItem.toString()
            val skinType = skin_type[selectedItem] ?: "all"
            viewModel.selectSkinType(skinType)
        })
}
