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
import com.hyejineee.hwahae.R
import com.hyejineee.hwahae.databinding.ProductDetailDialogBinding
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel

class ProductDetailDialog(
    context: Context,
    val product_id: Int,
    val viewModel: ProductDetailViewModel
) : Dialog(context),BaseUI<ProductDetailDialogBinding> {

    override lateinit var viewDataBinding: ProductDetailDialogBinding
    override fun layoutResourceID(): Int = R.layout.product_detail_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = inflate(
                LayoutInflater.from(context), layoutResourceID(), null, false)

        setContentView(viewDataBinding.root)

        initView()
        initSubscribe()

        viewDataBinding.dialog = this
        viewDataBinding.productDetailViewModel = viewModel
    }

    fun closeDialog() {
        this.dismiss()
    }

    override fun initView() {
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

    override fun initSubscribe() {
        viewModel.getProductDetail(product_id)
        viewModel.addDisposable(viewModel.productDeailSubject.subscribe {
            viewDataBinding.productDetailLayout.visibility = View.VISIBLE
            viewDataBinding.product = it
        })
        viewModel.addDisposable(viewModel.onErrorSubject.subscribe {
            viewDataBinding.refreshBtn.visibility = View.VISIBLE
            viewDataBinding.productDetailLayout.visibility = View.GONE
            viewDataBinding.buyButton.visibility = View.GONE
            Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
        })

    }

    fun reRequest() {
        viewModel.getProductDetail(product_id)
        viewDataBinding.refreshBtn.visibility = View.GONE
    }
}
