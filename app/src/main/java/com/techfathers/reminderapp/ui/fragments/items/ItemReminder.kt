package com.techfathers.reminderapp.ui.fragments.items

import android.view.View
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.databinding.ItemMainCategoryBinding
import com.techfathers.reminderapp.databinding.ItemReminderBinding
import com.techfathers.reminderapp.util.models.CategoryModel
import com.techfathers.reminderapp.util.models.ReminderModel
import com.xwray.groupie.databinding.BindableItem

class ItemReminder(
    val model: ReminderModel,
    private val onItemClick: OnClickedListener
) : BindableItem<ItemReminderBinding>() {

    override fun getLayout(): Int = R.layout.item_reminder

    override fun bind(viewBinding: ItemReminderBinding, position: Int) {
        viewBinding.model = model
        viewBinding.cvItem.setOnClickListener {
            onItemClick.onItemClicked(this, it, position)
        }
    }

    interface OnClickedListener {
        fun onItemClicked(
            itemReminder: ItemReminder,
            view: View,
            position: Int
        )
    }
}