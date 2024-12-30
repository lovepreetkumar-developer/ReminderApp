package com.techfathers.reminderapp.ui.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.util.toast
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvForgotPass.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        setHighLightedText(tvSignUp, getString(R.string.don_t_have_account))
    }

    private fun setHighLightedText(tv: TextView, text: String) {
        val wordToSpan: Spannable = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_forgot_password
                )
            }
        }
        wordToSpan.setSpan(clickableSpan, 23, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = wordToSpan
        tv.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvForgotPass -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            R.id.btnLogin -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
        }
    }
}