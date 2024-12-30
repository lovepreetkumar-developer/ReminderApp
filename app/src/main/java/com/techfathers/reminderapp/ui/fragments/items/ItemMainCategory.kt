package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemMainCategoryBinding
import com.techfathers.reminderapp.util.models.CategoryModel
import com.xwray.groupie.databinding.BindableItem

class ItemMainCategory(
    val model: CategoryModel,
    private val onItemClick: OnClickedListener
) : BindableItem<ItemMainCategoryBinding>() {

    override fun getLayout(): Int = R.layout.item_main_category

    override fun bind(viewBinding: ItemMainCategoryBinding, position: Int) {
        viewBinding.model = model
        viewBinding.cvItem.setOnClickListener {
            onItemClick.onItemClicked(this, it, position)
        }
    }

    interface OnClickedListener {
        fun onItemClicked(
            itemMainCategory: ItemMainCategory,
            view: View,
            position: Int
        )
    }
}