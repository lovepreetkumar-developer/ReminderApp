package com.techfathers.reminderapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.techfathers.reminderapp.R
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.*

class ProductDetailsFragment : Fragment(), View.OnClickListener {

    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        imgCall.setOnClickListener(this)
        (childFragmentManager.findFragmentById(R.id.mapFrag) as SupportMapFragment?)?.let {
            it.getMapAsync { map: GoogleMap? ->
                mMap = map
            }
        }
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.shopping)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.imgCall -> {
                /*val intent = Intent(Intent.ACTION_CALL);
                intent.data = Uri.parse("tel:+91 9876543210")
                requireContext().startActivity(intent)*/
            }
        }
    }
}