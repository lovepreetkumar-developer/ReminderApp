package com.techfathers.reminderapp.ui.fragments

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
import com.techfathers.reminderapp.util.SwipeOptionsHelper
import com.techfathers.reminderapp.util.models.CategoryModel
import com.techfathers.reminderapp.util.myCategoryList
import com.techfathers.reminderapp.util.toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_main_categories.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.*

class MainCategoriesFragment : Fragment(), View.OnClickListener {

    private lateinit var mGroupAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        imgAdd.setOnClickListener(this)
        initMainCategoriesRecyclerView(myCategoryList().toMainCategoriesItems())

    }


    private fun initMainCategoriesRecyclerView(items: List<ItemMainCategory>) {
        mGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerMainCategories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mGroupAdapter
        }

        val swipeHelper: SwipeOptionsHelper =
            object : SwipeOptionsHelper(requireContext(), recyclerMainCategories) {
                override fun instantiateUnderlayButton(
                    viewHolder: RecyclerView.ViewHolder,
                    underlayButtons: MutableList<UnderlayButton>
                ) {
                    underlayButtons.add(UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FC1B1B")
                    ) {
                        requireContext().toast("yes click is working")
                    })
                    underlayButtons.add(UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#A1AAB2")
                    ) {
                        findNavController().navigate(
                            MainCategoriesFragmentDirections.actionMainCategoriesFragmentToAddEditMainCategory(
                                "edit"
                            )
                        )
                    })
                }
            }

        swipeHelper.attachSwipe()
    }

    private fun List<CategoryModel>.toMainCategoriesItems(): List<ItemMainCategory> {
        return this.map { model ->
            ItemMainCategory(model, object : ItemMainCategory.OnClickedListener {
                override fun onItemClicked(
                    itemMainCategory: ItemMainCategory,
                    view: View,
                    position: Int
                ) {
                    findNavController().navigate(
                        MainCategoriesFragmentDirections.actionMainCategoriesFragmentToCategoryProductsFragment()
                    )
                }
            })
        }
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.menu_all_category)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.imgAdd -> {
                findNavController().navigate(
                    MainCategoriesFragmentDirections.actionMainCategoriesFragmentToAddEditMainCategory(
                        "add"
                    )
                )
            }
        }
    }
}