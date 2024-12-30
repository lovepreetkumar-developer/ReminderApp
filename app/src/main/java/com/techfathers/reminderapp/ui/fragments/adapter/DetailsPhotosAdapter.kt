package com.techfathers.reminderapp.ui.fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import com.techfathers.reminderapp.R

class DetailsPhotosAdapter(
    private val context: Context
) : PagerAdapter() {

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_welcome_slide, null)
        val imageView = view.findViewById<ImageView>(R.id.imgIcon)
        when (position) {
            0 -> imageView.setImageResource(R.drawable.ic_first_slide)
            1 -> imageView.setImageResource(R.drawable.ic_second_slide)
            2 -> imageView.setImageResource(R.drawable.ic_third_slide)
        }
        val viewPager: ViewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        val viewPager: ViewPager = container as ViewPager
        val view = `object` as View?
        viewPager.removeView(view)
    }
}