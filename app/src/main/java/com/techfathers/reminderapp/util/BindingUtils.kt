package com.techfathers.reminderapp.util

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.io.File


class BindingUtils {

    companion object {

        @BindingAdapter(value = ["simpleResource"], requireAll = false)
        @JvmStatic
        fun simpleResource(imageView: AppCompatImageView, simpleResource: Int) {
            if (simpleResource != -1) {
                imageView.setImageResource(simpleResource)
            }
        }

        @BindingAdapter(value = ["simpleCircleResource"], requireAll = false)
        @JvmStatic
        fun simpleCircleResource(imageView: AppCompatImageView, simpleResource: Int) {
            if (simpleResource != -1) {
                Glide.with(imageView.context)
                    .load(simpleResource)
                    .circleCrop()
                    .into(imageView)
            }
        }

        @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
        @JvmStatic
        fun setImageUrl(imageView: ImageView?, imageUrl: String?, placeholder: Drawable?) {

            imageUrl?.let {
                if (it.isNotEmpty()) {
                    if (placeholder == null) {
                        Picasso.get().load(imageUrl).into(imageView)
                    } else {
                        Picasso.get().load(imageUrl).placeholder(placeholder).into(imageView)
                    }
                }
            }
        }

        @BindingAdapter(value = ["image_path"], requireAll = false)
        @JvmStatic
        fun setBitmapOnImage(imageView: ImageView?, imagePath: String) {
            Picasso.get().load(File(imagePath))
                .centerCrop()
                .resize(100, 100)
                .into(imageView)
        }

        @BindingAdapter(value = ["circle_image", "placeholder"], requireAll = false)
        @JvmStatic
        fun setCircleImage(imageView: ImageView, imagePath: String?, placeholder: Drawable?) {

            try {
                imagePath?.let {
                    if (!imagePath.contains(" ")) {
                        Glide.with(imageView.context)
                            .load(imagePath)
                            .circleCrop()
                            .placeholder(placeholder)
                            .into(imageView)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @BindingAdapter(value = ["round_pic", "placeholder"], requireAll = false)
        @JvmStatic
        fun round(imageView: ImageView, imageUrl: String?, placeholder: Drawable?) {

            Glide.with(imageView.context)
                .load(imageUrl)
                .circleCrop()
                .placeholder(placeholder)
                .into(imageView)
        }

        @BindingAdapter(value = ["setPaintFlag"], requireAll = false)
        @JvmStatic
        fun setPaintFlag(textView: TextView?, string: String) {

            textView?.let {
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }
}