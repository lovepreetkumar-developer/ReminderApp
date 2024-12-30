package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemSideMenuBinding
import com.techfathers.reminderapp.util.models.SideMenuModel
import com.xwray.groupie.databinding.BindableItem

class ItemSideMenu(
    var model: SideMenuModel,
    private val onClick: OnClickListener
) : BindableItem<ItemSideMenuBinding>() {

    interface OnClickListener {
        fun onItemClicked(
            item: ItemSideMenu,
            view: View,
            position: Int
        )
    }

    override fun getLayout(): Int = R.layout.item_side_menu

    override fun bind(viewBinding: ItemSideMenuBinding, position: Int) {
        viewBinding.model = model
        viewBinding.rlMenu.setOnClickListener {
            onClick.onItemClicked(this, it, position)
        }
    }
}