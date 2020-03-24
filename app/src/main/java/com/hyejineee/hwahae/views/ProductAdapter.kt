package com.hyejineee.hwahae.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hyejineee.hwahae.databinding.GridItemBinding
import com.hyejineee.hwahae.databinding.LoadingItemBinding
import com.hyejineee.hwahae.model.Product

class ProductAdapter(
    private val clickListener : (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var products = listOf<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val LOADING_ITEM = 0
    val PRODUCT_ITEM = 1

    inner class ViewHolder(val gridItemBinding: GridItemBinding) :
        RecyclerView.ViewHolder(gridItemBinding.root) {
        fun bind(item: Product) {
            gridItemBinding.item = item
            gridItemBinding.root.setOnClickListener { clickListener(item.id) }
            gridItemBinding.executePendingBindings()
        }
    }

    inner class LoadingViewHolder(val loadingItemBinding: LoadingItemBinding) :
        RecyclerView.ViewHolder(loadingItemBinding.root) {
        fun bind(item: Product) {
            loadingItemBinding.item = item
            loadingItemBinding.executePendingBindings()
        }
    }

    fun startLoadingMode() {
        products = products.plus(Product())
    }

    fun stopLoadingMode() {
        products = products.dropLast(1)
    }

    override fun getItemViewType(position: Int): Int =
        if (products[position].id != -1) PRODUCT_ITEM else LOADING_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == PRODUCT_ITEM)
            ViewHolder(GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else
            LoadingViewHolder(
                LoadingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

    override fun getItemCount(): Int = products?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = products[position]
        if (holder.itemViewType == PRODUCT_ITEM) {
            (holder as ViewHolder).bind(item)
        } else {
            (holder as LoadingViewHolder).bind(item)
        }
    }
}
