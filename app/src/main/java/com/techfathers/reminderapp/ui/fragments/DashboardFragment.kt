package com.techfathers.reminderapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.techfathers.reminderapp.MainActivity
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.ui.fragments.items.ItemExpiringProduct
import com.techfathers.reminderapp.ui.fragments.items.ItemMyCategory
import com.techfathers.reminderapp.ui.fragments.items.ItemSideMenu
import com.techfathers.reminderapp.util.expiringProductsList
import com.techfathers.reminderapp.util.models.CategoryModel
import com.techfathers.reminderapp.util.models.ProductModel
import com.techfathers.reminderapp.util.models.SideMenuModel
import com.techfathers.reminderapp.util.myCategoryList
import com.techfathers.reminderapp.util.rateApplication
import com.techfathers.reminderapp.util.sideMenuList
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.partial_app_bar_dashboard.*
import kotlinx.android.synthetic.main.partial_side_menu.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.*
import java.util.*


class DashboardFragment : Fragment(), View.OnClickListener {

    private lateinit var mDrawerGroupAdapter: GroupAdapter<ViewHolder>
    private lateinit var mCategoryGroupAdapter: GroupAdapter<ViewHolder>
    private lateinit var mExpiringGroupAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        initCategoriesRecyclerView(myCategoryList().toCategoriesItems())
        initExpiringRecyclerView(expiringProductsList().toExpiringItems())
        initSideMenuRecyclerView(sideMenuList(requireContext()).toSideMenuItems())

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                try {
                    val moveFactor: Float = drawerLayout.width * slideOffset
                    appBarMain.translationX = moveFactor
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerOpened(drawerView: View) {
            }
        })
    }

    private fun setToolbar() {
        tvTitle.text = getString(R.string.home)
        imgMenu.visibility = View.VISIBLE
        imgSearch.visibility = View.VISIBLE
        imgMenu.setOnClickListener(this)
        imgSearch.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgMenu -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.imgSearch -> findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToSearchProductFragment())
        }
    }

    private fun initCategoriesRecyclerView(items: List<ItemMyCategory>) {

        mCategoryGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerCategories.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = mCategoryGroupAdapter
        }
    }

    private fun List<CategoryModel>.toCategoriesItems(): List<ItemMyCategory> {
        return this.map { model ->
            ItemMyCategory(model, object : ItemMyCategory.OnClickedListener {
                override fun onItemClicked(
                    itemMyCategory: ItemMyCategory,
                    view: View,
                    position: Int
                ) {
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToCategoryProductsFragment())
                }
            })
        }
    }

    private fun initExpiringRecyclerView(items: List<ItemExpiringProduct>) {

        mExpiringGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerExpiring.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = mExpiringGroupAdapter
        }
    }

    private fun initSideMenuRecyclerView(items: List<ItemSideMenu>) {

        mDrawerGroupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerMenus.apply {
            layoutManager = LinearLayoutManager(
                requireContext()
            )
            setHasFixedSize(true)
            adapter = mDrawerGroupAdapter
        }
    }

    private fun List<ProductModel>.toExpiringItems(): List<ItemExpiringProduct> {
        return this.map { model ->
            ItemExpiringProduct(model, object : ItemExpiringProduct.OnClickedListener {
                override fun onItemClicked(
                    itemExpiringProduct: ItemExpiringProduct,
                    view: View,
                    position: Int
                ) {
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToProductDetailsFragment())
                }
            })
        }
    }

    private fun List<SideMenuModel>.toSideMenuItems(): List<ItemSideMenu> {
        return this.map {
            ItemSideMenu(it, object : ItemSideMenu.OnClickListener {
                override fun onItemClicked(item: ItemSideMenu, view: View, position: Int) {
                    if (mDrawerGroupAdapter.itemCount > 0) {
                        for (i in 0..mDrawerGroupAdapter.itemCount) {
                            try {
                                val itemSideMenu = mDrawerGroupAdapter.getItem(i) as ItemSideMenu
                                itemSideMenu.model.selected = i == position
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                break
                            }
                        }
                    }

                    mDrawerGroupAdapter.notifyDataSetChanged()

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            drawerLayout?.closeDrawer(GravityCompat.START)
                        }
                    }, 300)

                    when (position) {
                        1 -> findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToProfileFragment())
                        4 -> rateApplication(requireContext())
                        3 -> findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToMainCategoriesFragment())
                        6 -> findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAboutUsFragment())
                        7 -> {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            })
        }
    }
}