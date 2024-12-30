package com.techfathers.reminderapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.ui.fragments.items.ItemMyProduct
import com.techfathers.reminderapp.util.expiringProductsList
import com.techfathers.reminderapp.util.models.ProductModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_search_product.*
import java.util.*

class SearchProductFragment : Fragment() {

    private lateinit var mGroupAdapter: GroupAdapter<ViewHolder>
    private var adapterList = mutableListOf<ItemMyProduct>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    filterList(it.toString())
                }
            }
        })

        initExpiringRecyclerView(expiringProductsList().toExpiringItems())

    }


    private fun initExpiringRecyclerView(items: List<ItemMyProduct>) {
        adapterList.addAll(items)
        mGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerProducts.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 2
            )
            setHasFixedSize(true)
            adapter = mGroupAdapter
        }
    }

    private fun List<ProductModel>.toExpiringItems(): List<ItemMyProduct> {
        return this.map { model ->
            ItemMyProduct(model, object : ItemMyProduct.OnClickedListener {
                override fun onItemClicked(itemMyProduct: ItemMyProduct, view: View) {

                }
            })
        }
    }

    private fun filterList(text: String) {

        val list = adapterList.filter {
            it.model.name.toLowerCase(
                Locale.getDefault()
            ).contains(
                text.toLowerCase(
                    Locale.getDefault()
                )
            )
        }
        mGroupAdapter.update(list)
    }
}