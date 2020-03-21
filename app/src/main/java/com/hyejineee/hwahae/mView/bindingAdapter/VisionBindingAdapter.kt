package com.hyejineee.hwahae.mView.bindingAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.hyejineee.hwahae.R
import io.reactivex.subjects.Subject

@SuppressLint("CheckResult")
@BindingAdapter("loadingVisible")
fun setVisible(view: View, isVisible: Subject<Boolean>) {
    isVisible.subscribe {
        when (view) {
            is FrameLayout -> {
                view.isVisible = it
                (view.parent as ViewGroup).findViewById<LinearLayout>(R.id.refresh_btn)?.isVisible =
                    false
            }
            else -> {
                view.isVisible = !it
                view.isClickable = false
            }
        }
    }
}

@SuppressLint("CheckResult")
@BindingAdapter("noItemNoticeVisible")
fun setNoItemNoticeViewVisible(view: ViewGroup, isVisible: Subject<Boolean>) {
    isVisible.subscribe {
        view.isVisible = it
        (view.parent as ViewGroup).findViewById<LinearLayout>(R.id.refresh_btn)?.isVisible = false
        (view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}
