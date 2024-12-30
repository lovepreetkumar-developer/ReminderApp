package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemCategoryProductBinding
import com.techfathers.reminderapp.databinding.ItemMyProductBinding
import com.techfathers.reminderapp.util.models.ProductModel
import com.xwray.groupie.databinding.BindableItem

class ItemCategoryProduct(
    val model: ProductModel,
    private val onItemClick: OnClickedListener
) : BindableItem<ItemCategoryProductBinding>() {

    override fun getLayout(): Int = R.layout.item_category_product

    override fun bind(viewBinding: ItemCategoryProductBinding, position: Int) {
        viewBinding.model = model
    }

    interface OnClickedListener {
        fun onItemClicked(
            itemCategoryProduct: ItemCategoryProduct,
            view: View,
            position:Int
        )
    }
}