package com.techfathers.reminderapp.ui.fragments

import android.os.Bundle
import android.text.*
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
import kotlinx.android.synthetic.main.fragment_validate_otp.*
import kotlinx.android.synthetic.main.partial_toolbar.*

class ValidateOtpFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_validate_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgBack.setOnClickListener(this)
        tvTitle.text = getString(R.string.otp_verification)
        setHighLightedText(tvOtpSentTo, "Enter OTP sent to +91 9876543210")
        otpWork()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.btnProceed -> findNavController().navigate(ValidateOtpFragmentDirections.actionValidateOtpFragmentToResetPasswordFragment())
        }
    }

    private fun setHighLightedText(tv: TextView, text: String) {
        val wordToSpan: Spannable = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                requireContext().toast("yes click is working")
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
        wordToSpan.setSpan(clickableSpan, 19, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = wordToSpan
        tv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun otpWork() {
        etFirstBlock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 1) {
                    etSecondBlock.requestFocus()
                }
            }
        })

        etSecondBlock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 1) {
                    etThirdBlock.requestFocus()
                }
                if (p0?.length == 0) {
                    etFirstBlock.text?.let {
                        etFirstBlock.requestFocus(it.length)
                    }
                }
            }
        })

        etThirdBlock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 1) {
                    etFourthBlock.requestFocus()
                }
                if (p0?.length == 0) {
                    etSecondBlock.text?.let {
                        etSecondBlock.requestFocus(it.length)
                    }
                }
            }
        })

        etFourthBlock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    etThirdBlock.text?.let {
                        etThirdBlock.requestFocus(it.length)
                    }
                }
            }
        })
    }
}