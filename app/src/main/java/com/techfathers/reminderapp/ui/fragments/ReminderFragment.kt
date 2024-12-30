package com.techfathers.reminderapp.ui.fragments

import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.ui.fragments.items.ItemMainCategory
import com.techfathers.reminderapp.ui.fragments.items.ItemReminder
import com.techfathers.reminderapp.util.SwipeOptionsHelper
import com.techfathers.reminderapp.util.models.CategoryModel
import com.techfathers.reminderapp.util.models.ReminderModel
import com.techfathers.reminderapp.util.myCategoryList
import com.techfathers.reminderapp.util.reminderList
import com.techfathers.reminderapp.util.toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_add_edit_product.*
import kotlinx.android.synthetic.main.fragment_main_categories.*
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.imgAlarm
import java.util.*

class ReminderFragment : Fragment(), View.OnClickListener {

    private lateinit var mGroupAdapter: GroupAdapter<ViewHolder>
    private var mTimePicker: TimePickerDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        initReminderRecyclerView(reminderList().toReminderItems())
        setUpTimePicker()
    }


    private fun initReminderRecyclerView(items: List<ItemReminder>) {
        mGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerReminder.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mGroupAdapter
        }
    }

    private fun List<ReminderModel>.toReminderItems(): List<ItemReminder> {
        return this.map { model ->
            ItemReminder(model, object : ItemReminder.OnClickedListener {
                override fun onItemClicked(
                    itemReminder: ItemReminder,
                    view: View,
                    position: Int
                ) {
                    if (view.id == R.id.cvItem) {
                        mTimePicker?.show()
                    }
                }
            })
        }
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.reminder)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener(this)
        imgAlarm.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
        }
    }

    private fun setUpTimePicker() {
        val mCurrentTime: Calendar = Calendar.getInstance()
        val hour: Int = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute: Int = mCurrentTime.get(Calendar.MINUTE)
        mTimePicker =
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, min ->

                    val time = String.format("%02d:%02d", hourOfDay, min)
                    requireContext().toast("Selected Time $time")
                }, hour, minute, true
            ) //Yes 24 hour time

        mTimePicker?.setTitle("Select Time")
    }
}