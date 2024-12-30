package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemMyCategoryBinding
import com.techfathers.reminderapp.util.models.CategoryModel
import com.xwray.groupie.databinding.BindableItem

class ItemMyCategory(
    val model: CategoryModel,
    private val onItemClick: OnClickedListener
) : BindableItem<ItemMyCategoryBinding>() {

    override fun getLayout(): Int = R.layout.item_my_category

    override fun bind(viewBinding: ItemMyCategoryBinding, position: Int) {
        viewBinding.model = model
        viewBinding.cvItem.setOnClickListener{
            onItemClick.onItemClicked(this,it,position)
        }
    }

    interface OnClickedListener {
        fun onItemClicked(
            itemMyCategory: ItemMyCategory,
            view: View,
            position: Int
        )
    }
}