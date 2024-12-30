package com.techfathers.reminderapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.util.Coroutines
import com.techfathers.reminderapp.util.showSoftKeyboard
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.spinnerCurrencies
import kotlinx.android.synthetic.main.partial_toolbar_gray.*
import timber.log.Timber

class ProfileFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        tvCurrency.setOnClickListener(this)

        val adapterCurrencies: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_record,
            requireContext().resources.getStringArray(R.array.currencies)
        )

        spinnerCurrencies.adapter = adapterCurrencies
        spinnerCurrencies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.let {
                    tvCurrency.text = it.selectedItem.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.menu_profile)
        imgBack.visibility = View.VISIBLE
        imgEditProfile.visibility = View.VISIBLE
        imgBack.setOnClickListener(this)
        imgEditProfile.setOnClickListener(this)
        imgProfile.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.imgEditProfile -> {
                etUsername.requestFocus()
                showSoftKeyboard(requireActivity())
            }
            R.id.tvCurrency -> {
                spinnerCurrencies.performClick()
            }
            R.id.imgProfile -> {
                getMediaPermissions()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Matisse.obtainPathResult(data)
                Timber.e(Matisse.obtainOriginalState(data).toString())
                val filePath = Matisse.obtainPathResult(data)[0]
                filePath?.let {
                    Glide.with(this)
                        .load(filePath)
                        .circleCrop()
                        .into(imgProfile)
                }
            }
        }
    }

    private fun getMediaPermissions() {
        Coroutines.main {
            try {
                askPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

                getLocalImage()

            } catch (e: PermissionException) {
                if (e.hasDenied()) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Please accept our permissions to work app properly in background")
                        .setPositiveButton("yes") { _, _ ->
                            e.askAgain()
                        }
                        .setNegativeButton("no") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                    if (e.hasForeverDenied()) {
                        //the list of forever denied permissions, user has check 'never ask again'
                        //*e.foreverDenied.forEach { permission ->
                        //   binding.root.snackBar(permission)
                        //appendText(resultView, permission)
                        //you need to open setting manually if you really need it
                        e.goToSettings()
                    }
                }
            }
        }
    }

    private fun getLocalImage() {
        Matisse.from(this@ProfileFragment)
            .choose(MimeType.ofImage())
            .countable(false)
            .maxSelectable(1)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .showSingleMediaType(true)
            .originalEnable(true)
            .autoHideToolbarOnSingleTap(true)
            .forResult(1111)
    }
}