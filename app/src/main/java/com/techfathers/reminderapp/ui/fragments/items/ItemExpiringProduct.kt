package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemExpiringProductBinding
import com.techfathers.reminderapp.util.models.ProductModel
import com.xwray.groupie.databinding.BindableItem

class ItemExpiringProduct(
    val model: ProductModel,
    private val onItemClick: OnClickedListener
) : BindableItem<ItemExpiringProductBinding>() {

    override fun getLayout(): Int = R.layout.item_expiring_product

    override fun bind(viewBinding: ItemExpiringProductBinding, position: Int) {
        viewBinding.model = model
        viewBinding.cvItem.setOnClickListener {
            onItemClick.onItemClicked(this, it,position)
        }
    }

    interface OnClickedListener {
        fun onItemClicked(
            itemExpiringProduct: ItemExpiringProduct,
            view: View,
            position: Int
        )
    }
}