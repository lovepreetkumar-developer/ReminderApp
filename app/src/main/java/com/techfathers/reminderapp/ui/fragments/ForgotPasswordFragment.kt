package com.techfathers.reminderapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.techfathers.reminderapp.R
import kotlinx.android.synthetic.main.partial_toolbar.*

class ForgotPasswordFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.btnLogin -> findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToValidateOtpFragment())
        }
    }
}