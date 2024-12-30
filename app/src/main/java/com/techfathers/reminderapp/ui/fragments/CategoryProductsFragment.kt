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
import com.techfathers.reminderapp.ui.fragments.items.ItemCategoryProduct
import com.techfathers.reminderapp.util.SwipeOptionsHelper
import com.techfathers.reminderapp.util.models.ProductModel
import com.techfathers.reminderapp.util.productsList
import com.techfathers.reminderapp.util.toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_category_products.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.*

class CategoryProductsFragment : Fragment(), View.OnClickListener {

    private lateinit var mGroupAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
        initProductsRecyclerView(productsList().toCategoryProducts())

    }


    private fun initProductsRecyclerView(items: List<ItemCategoryProduct>) {
        mGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mGroupAdapter
        }

        val swipeHelper: SwipeOptionsHelper =
            object : SwipeOptionsHelper(requireContext(), recyclerProducts) {
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
                            CategoryProductsFragmentDirections.actionCategoryProductsFragmentToAddEditProductFragment(
                                "edit"
                            )
                        )
                    })
                }
            }

        swipeHelper.attachSwipe()
    }

    private fun List<ProductModel>.toCategoryProducts(): List<ItemCategoryProduct> {
        return this.map { model ->
            ItemCategoryProduct(model, object : ItemCategoryProduct.OnClickedListener {
                override fun onItemClicked(
                    itemCategoryProduct: ItemCategoryProduct,
                    view: View,
                    position: Int
                ) {

                }
            })
        }
    }

    private fun setUpViews() {
        tvTitle.text = getString(R.string.menu_shopping_list)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener(this)
        imgAdd.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.imgAdd -> {
                findNavController().navigate(
                    CategoryProductsFragmentDirections.actionCategoryProductsFragmentToAddEditProductFragment(
                        "add"
                    )
                )
            }
        }
    }
}