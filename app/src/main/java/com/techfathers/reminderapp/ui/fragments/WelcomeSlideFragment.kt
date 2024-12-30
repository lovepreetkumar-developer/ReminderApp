package com.techfathers.reminderapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.techfathers.reminderapp.MainActivity
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.ui.fragments.adapter.DetailsPhotosAdapter
import com.techfathers.reminderapp.util.CubeOutRotationTransformation
import com.techfathers.reminderapp.util.toast
import kotlinx.android.synthetic.main.fragment_welcome_slider.*

class WelcomeSlideFragment : Fragment(), View.OnClickListener {

    private lateinit var mDetailsPhotosAdapter: DetailsPhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        mDetailsPhotosAdapter = DetailsPhotosAdapter(
            requireContext()
        )
        vpSlides.setPageTransformer(true, CubeOutRotationTransformation())
        vpSlides.adapter = mDetailsPhotosAdapter
        tvSkip.setOnClickListener(this)
        tvNext.setOnClickListener(this)

        tabLayoutDots.setupWithViewPager(vpSlides)

        for (i in 0 until tabLayoutDots.tabCount) {
            val tab = (tabLayoutDots.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.tvSkip -> findNavController().navigate(WelcomeSlideFragmentDirections.actionWelcomeSliderFragmentToLoginFragment())
            R.id.tvNext -> {
                if (vpSlides.currentItem == 2) {
                    findNavController().navigate(WelcomeSlideFragmentDirections.actionWelcomeSliderFragmentToLoginFragment())
                } else {
                    vpSlides.currentItem = vpSlides.currentItem + 1
                }
            }
        }
    }
}