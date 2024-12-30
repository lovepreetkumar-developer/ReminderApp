package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemMyProductBinding
import com.techfathers.reminderapp.util.models.ProductModel
import com.xwray.groupie.databinding.BindableItem

class ItemMyProduct(
    val model: ProductModel,
    private val onItemClick: OnClickedListener
) : BindableItem<ItemMyProductBinding>() {

    override fun getLayout(): Int = R.layout.item_my_product

    override fun bind(viewBinding: ItemMyProductBinding, position: Int) {
        viewBinding.model = model
    }

    interface OnClickedListener {
        fun onItemClicked(
            itemMyProduct: ItemMyProduct,
            view: View
        )
    }
}