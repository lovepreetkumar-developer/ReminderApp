package com.techfathers.reminderapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.util.Coroutines
import com.techfathers.reminderapp.util.toast
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_add_edit_product.*
import kotlinx.android.synthetic.main.partial_toolbar_gray.*
import timber.log.Timber
import java.util.*


class AddEditProductFragment : Fragment(), View.OnClickListener {

    private var mTimePicker: TimePickerDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
    }

    private fun setUpViews() {

        val adapterCurrencies: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_record,
            requireContext().resources.getStringArray(R.array.categories)
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
                    tvCategory.text = it.selectedItem.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        tvTitle.text = getString(R.string.add_product)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener(this)
        llUpload.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        imgPlus.setOnClickListener(this)
        imgMinus.setOnClickListener(this)
        tvCategory.setOnClickListener(this)
        tvTimeValue.setOnClickListener(this)

        val arguments = arguments?.let { AddEditProductFragmentArgs.fromBundle(it) }
        val type = arguments?.type

        if (type == "edit") {
            tvTitle.text = getString(R.string.edit_product)
        }

        setUpTimePicker()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgBack -> requireActivity().onBackPressed()
            R.id.tvCategory -> {
                spinnerCurrencies.performClick()
            }
            R.id.imgPlus -> {
                var count = tvProductQty.text.toString().toInt()
                tvProductQty.text = (++count).toString()
            }
            R.id.imgMinus -> {
                var count = tvProductQty.text.toString().toInt()
                if (count > 1) {
                    tvProductQty.text = (--count).toString()
                }
            }
            R.id.llUpload -> {
                getMediaPermissions()
            }
            R.id.btnNext -> findNavController().navigate(
                AddEditProductFragmentDirections.actionAddEditProductFragmentToReminderFragment()
            )
            R.id.tvTimeValue -> mTimePicker?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == RESULT_OK) {
            if (data != null) {
                Matisse.obtainPathResult(data)
                Timber.e(Matisse.obtainOriginalState(data).toString())
                val filePath = Matisse.obtainPathResult(data)[0]
                filePath?.let {
                    Glide.with(this)
                        .load(filePath)
                        .into(ivProduct)
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
        Matisse.from(this@AddEditProductFragment)
            .choose(MimeType.ofImage())
            .countable(false)
            .maxSelectable(1)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .showSingleMediaType(true)
            .originalEnable(true)
            .autoHideToolbarOnSingleTap(true)
            .forResult(1111)
    }

    private fun setUpTimePicker() {
        val mCurrentTime: Calendar = Calendar.getInstance()
        val hour: Int = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute: Int = mCurrentTime.get(Calendar.MINUTE)
        mTimePicker =
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, min ->

                    val time = String.format("%02d:%02d", hourOfDay, min)
                    tvTimeValue.text = time
                }, hour, minute, true
            ) //Yes 24 hour time

        mTimePicker?.setTitle("Select Time")
    }
}