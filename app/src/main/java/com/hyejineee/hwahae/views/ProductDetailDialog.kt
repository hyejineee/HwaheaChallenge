package com.hyejineee.hwahae.views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil.inflate
import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.R
import com.hyejineee.hwahae.databinding.ProductDetailDialogBinding
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ProductDetailDialog(
    context: Context,
    private val product_id: Int,
    private val viewModel: ProductDetailViewModel
) : Dialog(context) {

    private lateinit var viewDataBinding: ProductDetailDialogBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = inflate(
            LayoutInflater.from(context), R.layout.product_detail_dialog, null, false
        )
        setContentView(viewDataBinding.root)

        initView()
        initSubscribe()

        viewDataBinding.dialog = this

        viewModel.actionDispatch(ActionType.GET_DETAIL, product_id)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun initView() {
        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val param = WindowManager.LayoutParams()
        param.height = (displayMetrics.heightPixels * 0.9f).toInt()
        param.gravity = Gravity.BOTTOM
        param.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        param.dimAmount = 0.8f
        param.windowAnimations = R.style.AnimationPopupStyle
        window?.attributes = param
        setCanceledOnTouchOutside(true)

        viewDataBinding.buyButton
            .startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_up))

    }

    private fun initSubscribe() {
        viewModel.onProductDetailChange
            .subscribe {
                viewDataBinding.productDetailLayout.visibility = View.VISIBLE
                viewDataBinding.product = it
            }.addTo(compositeDisposable)

        viewModel.onLoadingModeChange
            .subscribe {
                viewDataBinding.detailBox.visibility = if (it) View.GONE else View.VISIBLE
                viewDataBinding.loadingNoticeView.visibility = if (it) View.VISIBLE else View.GONE
                viewDataBinding.buyButton.visibility = if (it) View.GONE else View.VISIBLE
            }.addTo(compositeDisposable)

        viewModel.onErrorSubject.subscribe {
            viewDataBinding.refreshBtn.visibility = View.VISIBLE
            viewDataBinding.productDetailLayout.visibility = View.GONE
            viewDataBinding.buyButton.visibility = View.GONE
            Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
        }.addTo(compositeDisposable)
    }

    fun reRequest() {
        viewModel.actionDispatch(ActionType.GET_DETAIL, product_id)
        viewDataBinding.refreshBtn.visibility = View.GONE
    }

    fun closeDialog() = this.dismiss()
}
