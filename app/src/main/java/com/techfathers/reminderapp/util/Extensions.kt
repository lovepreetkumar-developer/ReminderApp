package com.techfathers.reminderapp.util

import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.techfathers.reminderapp.R
import com.techfathers.reminderapp.util.models.CategoryModel
import com.techfathers.reminderapp.util.models.ProductModel
import com.techfathers.reminderapp.util.models.ReminderModel
import com.techfathers.reminderapp.util.models.SideMenuModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun myCategoryList(): List<CategoryModel> {
    val list = mutableListOf<CategoryModel>()
    list.add(CategoryModel("Medicine", R.drawable.ic_medicine))
    list.add(
        CategoryModel(
            "Fruits",
            R.drawable.ic_fruits
        )
    )
    list.add(CategoryModel("Vegetables", R.drawable.ic_vegetabels))
    list.add(CategoryModel("Clothes", R.drawable.ic_clothes))
    list.add(CategoryModel("Cosmetics", R.drawable.ic_cosmetic))
    return list
}

fun sideMenuList(context: Context): MutableList<SideMenuModel> {

    val list = mutableListOf<SideMenuModel>()
    list.add(SideMenuModel(context.getString(R.string.menu_home), R.drawable.ic_menu_home))
    list.add(SideMenuModel(context.getString(R.string.menu_profile), R.drawable.ic_menu_profile))
    list.add(
        SideMenuModel(
            context.getString(R.string.menu_shopping_list),
            R.drawable.ic_menu_shopping_list
        )
    )
    list.add(
        SideMenuModel(
            context.getString(R.string.menu_all_category),
            R.drawable.ic_menu_all_category
        )
    )
    list.add(SideMenuModel(context.getString(R.string.menu_rate_us), R.drawable.ic_menu_rate_us))
    list.add(SideMenuModel(context.getString(R.string.menu_share), R.drawable.ic_menu_share))
    list.add(SideMenuModel(context.getString(R.string.menu_about_us), R.drawable.ic_menu_about_us))
    list.add(SideMenuModel(context.getString(R.string.menu_logout), R.drawable.ic_menu_logout))
    return list
}

fun expiringProductsList(): List<ProductModel> {
    val list = mutableListOf<ProductModel>()
    list.add(ProductModel("Fruit", R.drawable.image_person))
    list.add(
        ProductModel(
            "Beer & Cider",
            R.drawable.image_person
        )
    )
    list.add(ProductModel("Lemon", R.drawable.image_person))
    list.add(ProductModel("Cock", R.drawable.image_person))
    list.add(ProductModel("Bun", R.drawable.image_person))
    list.add(ProductModel("Books", R.drawable.image_person))
    list.add(ProductModel("Salt", R.drawable.image_person))
    return list
}

fun productsList(): List<ProductModel> {
    val list = mutableListOf<ProductModel>()
    list.add(ProductModel("Potatoes Items", R.drawable.ic_product_two))
    list.add(
        ProductModel(
            "Onions",
            R.drawable.ic_product_three
        )
    )
    list.add(ProductModel("Carrots", R.drawable.ic_product_four))
    list.add(ProductModel("Medicines", R.drawable.ic_product_one))
    list.add(ProductModel("Garlic", R.drawable.ic_product_five))

    list.addAll(list)
    return list
}

fun reminderList(): List<ReminderModel> {
    val list = mutableListOf<ReminderModel>()
    list.add(ReminderModel("Monday", "10:00 pm"))
    list.add(ReminderModel("Tuesday", "10:00 pm"))
    list.add(ReminderModel("Wednesday", "10:00 pm"))
    list.add(ReminderModel("Thursday", "10:00 pm"))
    list.add(ReminderModel("Friday", "10:00 pm"))
    list.add(ReminderModel("Saturday", "10:00 pm"))
    list.add(ReminderModel("Sunday", "10:00 pm"))
    return list
}

fun getIndexCounts(): MutableList<String> {

    val listOfIndex = mutableListOf<String>()
    for (i in 1..20) {
        listOfIndex.add(i.toString())
    }

    return listOfIndex
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackBar ->
        snackBar.setAction("Ok") {
            snackBar.dismiss()
        }
    }.show()
}

fun getEditTextValue(editText: AppCompatEditText): String {
    return editText.text.toString().trim()
}

fun getTextViewValue(editText: AppCompatTextView): String {
    return editText.text.toString().trim()
}

fun prepareImageWithFileDescriptor(context: Context, uri: Uri): File? {

    val parcelFileDescriptor =
        context.contentResolver.openFileDescriptor(uri, "r", null)

    var file: File?
    parcelFileDescriptor?.let {
        val inputStream =
            FileInputStream(parcelFileDescriptor.fileDescriptor)

        file = File(context.cacheDir, getFileName(context, uri)!!)
        val outputStream = FileOutputStream(file)
        //IOUtils.copy(inputStream, outputStream)
        return file
    }

    return null
}

fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }

    result?.let {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
        return result
    }

    return ""
}

fun createMultiPart(path: String, key: String): MultipartBody.Part {
    val file = File(path)
    val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
    return MultipartBody.Part.createFormData(key, file.name, requestFile)
}


fun getCurrentPageHeader(currentPage: Int): SpannableString {
    val s = "$currentPage/3"
    val ss1 = SpannableString(s)
    ss1.setSpan(RelativeSizeSpan(2f), 0, 1, 0)
    return ss1
}

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidMobile(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

@Throws(IOException::class)
fun getBytes(uri: Uri?, context: Context): ByteArray? {
    val inputStream = context.contentResolver.openInputStream(uri!!)
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)
    var len: Int
    while (inputStream!!.read(buffer).also { len = it } != -1) {
        byteBuffer.write(buffer, 0, len)
    }
    return byteBuffer.toByteArray()
}

fun getScreenWidthInPixel(context: Context): IntArray? {
    val widthHeight = IntArray(2)
    val wm = context
        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display1 = wm.defaultDisplay
    val size1 = Point()
    display1.getSize(size1)
    widthHeight[0] = size1.x
    widthHeight[1] = size1.y
    return widthHeight
}

fun getScreenWidth(context: Context): Int {
    val columnWidth: Int
    val wm = context
        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val point = Point()
    try {
        display.getSize(point)
    } catch (ignore: NoSuchMethodError) { // Older device
        point.x = display.width
        point.y = display.height
    }
    columnWidth = point.x
    return columnWidth
}

fun dipToPixels(context: Context, dipValue: Float): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
}

fun getOutputPhotoFile(context: Context?): File? {
    val root = Environment.getExternalStorageDirectory().toString()
    val myDir = File("$root/BaseAppController.PHOTO_ALBUM")

    if (!myDir.exists()) {
        if (!myDir.mkdirs()) {
            Timber.e("Failed to create storage directory.")
            return null
        }
    }
    val timeStamp = SimpleDateFormat("yyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    return File(
        myDir.path + File.separator + "IMG_"
                + timeStamp + ".jpg"
    )
}

fun streamFileType(file: File): String? {
    val string2 = file.name
    if (string2.endsWith(".jpg")) {
        return "image/jpg"
    }
    if (string2.endsWith(".JPG")) {
        return "image/jpg"
    }
    if (string2.endsWith(".png")) {
        return "image/png"
    }
    if (string2.endsWith(".PNG")) {
        return "image/png"
    }
    if (string2.endsWith(".gif")) {
        return "image/gif"
    }
    if (string2.endsWith(".GIF")) {
        return "image/gif"
    }
    if (string2.endsWith(".jpeg")) {
        return "image/jpeg"
    }
    if (string2.endsWith(".JPEG")) {
        return "image/jpeg"
    }
    if (string2.endsWith(".mp3")) {
        return "audio/mp3"
    }
    if (string2.endsWith(".MP3")) {
        return "audio/mp3"
    }
    if (string2.endsWith(".mp4")) {
        return "video/mp4"
    }
    if (string2.endsWith(".avi")) {
        return "video/.avi"
    }
    if (string2.endsWith(".ogg")) {
        return "video/ogg"
    }
    if (string2.endsWith(".3gp")) {
        return "video/3gp"
    }
    if (string2.endsWith(".pdf")) {
        return "pdf/pdf"
    }
    if (string2.endsWith(".docx")) {
        return "docs/docs"
    }
    return if (string2.endsWith(".xlsx")) {
        "xlsx/xlsx"
    } else ""
}

fun fadeIn(view: View) {
    val anim = AlphaAnimation(0.1f, 1f)
    anim.duration = 1500
    view.startAnimation(anim)
}

fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || "google_sdk" == Build.PRODUCT)
}

fun fadeOut(view: View) {
    val anim = AlphaAnimation(1f, 0.1f)
    anim.duration = 1500
    view.startAnimation(anim)
}

/*----------------------------Dates functions--------------------------*/

var commanDateTimeFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

fun getOnlyDateFromTimeStamp(timeStamp: String): String? {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp.toLong() * 1000
        return commanDateTimeFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun isDateInCurrentWeek(epoch: Long): Boolean {
    val currentCalendar = Calendar.getInstance()
    val week = currentCalendar[Calendar.WEEK_OF_YEAR]
    val year = currentCalendar[Calendar.YEAR]
    val targetCalendar = Calendar.getInstance()
    targetCalendar.timeInMillis = epoch * 1000
    val targetWeek = targetCalendar[Calendar.WEEK_OF_YEAR]
    val targetYear = targetCalendar[Calendar.YEAR]
    return week == targetWeek && year == targetYear
}


fun getTime(createdon: String): String? {
    //data and time coming from response ......"createdon": "2018-10-30 06:57:50"
    val split1 = createdon.split(" ".toRegex()).toTypedArray()
    val value_split1 = split1[1]
    val split2 = value_split1.split(":".toRegex()).toTypedArray()
    var time = split2[0] + ":" + split2[1]
    time += if (split2[0].toInt() < 12) " AM" else " PM"
    return time
}


fun getCalDate(createdon: String?): String? {
    // coming createdon date looks like this  -----  2018-11-01 04:49:16
    //and output should look like TUES, AUG 11
    var newDate: Date? = null
    var spf = SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault())
    try {
        newDate = spf.parse(createdon)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    spf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val final_date = spf.format(newDate)
    return final_date.toUpperCase()
}

fun getDateWithDots(startDate: String?): String? {
    val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val format2 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var date: Date? = null
    try {
        date = format1.parse(startDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return format2.format(date)
}

@Throws(ParseException::class)
fun getNextDate(string: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = format.parse(string)
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return format.format(calendar.time)
}

fun getDateFromString(string: String): Date? {
    /*2017-11-28 18:18:04*/
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    var date: Date? = null
    try {
        date = simpleDateFormat.parse(string)
    } catch (e: ParseException) {
        e.printStackTrace()
    } finally {
        return date
    }
}

fun getCurrentDate(): String {
    /*2017-11-28 18:18:04*/
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Calendar.getInstance().time
    return simpleDateFormat.format(date)
}

fun getCurrentDate(calendar: Calendar): String? {
    /*2017-11-28 18:18:04*/
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = calendar.time
    return simpleDateFormat.format(date)
}

fun getMonthForInt(num: Int): String? {
    var month = "wrong"
    val dfs = DateFormatSymbols()
    val months = dfs.shortMonths
    if (num >= 0 && num <= 11) {
        month = months[num]
    }
    return month
}

// getting only month from full date
fun getOnlyMonth(date: Int): String? {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date * 1000.toLong()
    return SimpleDateFormat("MMM").format(calendar.time)
}

fun getOnlyMonth(date: Calendar): String? {
    return SimpleDateFormat("MMM").format(date.time)
}

// getting only date from full date
fun getOnlyDate(date: Int): String? {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date * 1000.toLong()
    return calendar[Calendar.DAY_OF_MONTH].toString()
}

fun getDateFromStringDate(date: String?): String? {
    val dateFormatOne = SimpleDateFormat("dd-MMM-yyyy")
    val dateFormatTwo = SimpleDateFormat("yyyy-MM-dd")
    var date1: Date? = null
    try {
        date1 = dateFormatOne.parse(date)
    } catch (ex: ParseException) {
        ex.printStackTrace()
    }
    return dateFormatTwo.format(date1)
}


/*----------------------------String functions--------------------------*/


/*----------------------------String functions--------------------------*/
fun decodeString(stringToDecode: String?): String? {
    var decodedString: String? = null
    try {
        decodedString = URLDecoder.decode(stringToDecode, "UTF-8")
    } catch (e: java.lang.Exception) {
    }
    if (decodedString == null) {
        return stringToDecode
    }
    Log.e("after decoding", decodedString)
    return decodedString
}

fun getbitmap(arr: ByteArray?): Bitmap? {
    return BitmapFactory.decodeStream(ByteArrayInputStream(arr))
}

fun checkText(text: String?): Boolean {
    return text != null && !text.equals("", ignoreCase = true)
}

/*----------------------------Bitmap functions--------------------------*/

/*----------------------------Bitmap functions--------------------------*/
fun getCircleBitmap(bit: Bitmap?): Bitmap? {
    val bitmap1 = Bitmap.createScaledBitmap(bit!!, 100, 100, true)
    val output = Bitmap.createBitmap(
        bitmap1.width,
        bitmap1.height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(output)
    val color = Color.RED
    val paint = Paint()
    val rect = Rect(0, 0, bitmap1.width, bitmap1.height)
    val rectF = RectF(rect)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawOval(rectF, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap1, rect, rect, paint)
    bitmap1.recycle()
    return output
}

fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
            matrix.setRotate(180f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.setRotate(90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.setRotate(-90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
        else -> return bitmap
    }
    return try {
        val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        bmRotated
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        null
    }
}

fun getCompressedBitmap(bitmap: Bitmap): Bitmap? {
    try {
        return Bitmap.createScaledBitmap(bitmap, bitmap.width / 3, bitmap.height / 3, true)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
    return null
}

fun compressImage(bitmapImage: Bitmap): ByteArray? {
    val byteImage: ByteArray
    val stream = ByteArrayOutputStream()
    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
    byteImage = stream.toByteArray()
    return byteImage
}

fun dpToPx(dp: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return dp * scale + 0.5f
}

/*//  validation to check phonenumber
fun validatePhoneNumber(phoneNo: String): Boolean {
    //validate phone numbers of format "1234567890"
    return if (phoneNo.matches("\\d{10}")) true else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) true else  //return false if nothing matches the input
        if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) true else phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")
}*/

// hide keyboard
private var inputMethodManager: InputMethodManager? = null

fun hideSoftKeyboard(activity: Activity) {
    if (inputMethodManager == null) inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (activity.currentFocus != null) inputMethodManager!!.hideSoftInputFromWindow(
        activity.currentFocus!!.windowToken,
        0
    )
}

fun showSoftKeyboard(activity: Activity) {
    if (inputMethodManager == null) inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (activity.currentFocus != null) inputMethodManager!!.showSoftInput(activity.currentFocus, 0)
}

// Make a phone call
fun dialPhoneNumber(context: Context, phoneNumber: String?) {
    if (phoneNumber != null && !TextUtils.isEmpty(phoneNumber)) {
        val number: String = phoneNumber
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}

// Send a SMS
fun composeSmsMessage(context: Context, message: String?, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phoneNumber"))
    intent.putExtra("sms_body", message)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

fun gettimecounting(sobertime: String): String? {
    val stringBuilder = StringBuilder()
    try {
        val seconds = Calendar.getInstance().timeInMillis / 1000 - sobertime.toLong()
        val year = seconds / (3600 * 24 * 365)
        val month = seconds % (3600 * 24 * 365) / (30 * 24 * 3600)
        val days = seconds % (3600 * 24 * 365) % (30 * 24 * 3600) / (3600 * 24)
        if (year > 0) {
            stringBuilder.append(year.toString() + " year" + if (year > 1) "s " else " ")
        } else if (month > 0) {
            stringBuilder.append(month.toString() + " month" + if (month > 1) "s " else " ")
        }
        if (days > 0) {
            stringBuilder.append(days.toString() + " day" + if (days > 1) "s " else " ")
        }
    } catch (e: java.lang.Exception) {
    } finally {
        return stringBuilder.toString()
    }
}

fun gettcoincounting(sobertime: String): Array<String?>? {
    val s = arrayOfNulls<String>(2)
    try {
        val seconds = Calendar.getInstance().timeInMillis / 1000 - sobertime.toLong()
        val year = seconds / (3600 * 24 * 365)
        if (year > 0) {
            s[0] = year.toString() + ""
            s[1] = "year" + if (year > 1) "s" else ""
        } else {
            val sd = seconds % (3600 * 24 * 365) / (24 * 3600)
            s[0] = sd.toString() + ""
            s[1] = "day" + if (sd > 1) "s" else ""
        }
    } catch (e: java.lang.Exception) {
    } finally {
        return s
    }
}

fun getTimeZone(): String? {
    return Calendar.getInstance().timeZone.id
}

fun getTimeStamp(): String? {
    return (Calendar.getInstance().timeInMillis / 1000).toString()
}

fun getUtcTimeStamp(): Long {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    return calendar.timeInMillis / 1000
}

fun getCalenderObject(epoch: Long): Calendar? {
    val c = Calendar.getInstance()
    c.timeInMillis = epoch * 1000
    return c
}

fun getCalenderObject(epoch: String): Calendar? {
    return getCalenderObject(epoch.toLong())
}

/* public static Calendar getCalenderInChat(long epoch) {
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
         cal.setTimeInMillis(epoch);
         cal.setTimeZone(TimeZone.getDefault());
        return cal;

     }*/
fun getTimeStamp(calendar: Calendar): String? {
    return (calendar.timeInMillis / 1000).toString()
}

private val dateFormanew = SimpleDateFormat("dd MMM yyyy")
private val timeformat = SimpleDateFormat("hh:mm a")

fun getMessageId(): String? {
    return System.currentTimeMillis().toString()
}

fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}


/* public static String getAgoTime(long time) {
        try {
            long m = System.currentTimeMillis() - (time * 1000);
            m = m / 60000;
            if (m == 0) {
                return "just now";
            } else if (m > 0 && m < 11) {
                if (m > 2)
                    return m + " mins ago";
                else
                    return m + " min ago";

            } else {
                return ChatAdapter.dateFormat.format(UtilMethods.getCalenderObject(time).getTime());
            }
        } catch (NumberFormatException e) {
            return "error";
        }
    }*/

/*  public static String getAgoWithdate(long s) {
        Calendar now = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(s * 1000);
        StringBuilder builder = new StringBuilder();
        if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
            builder.append("Today");
        } else if (now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1) {
            builder.append("Yesterday");
        } else
            builder.append(DateFormat.format("dd MMMM yyyy", time.getTimeInMillis()));

        builder.append(" " + getAgoTime(s));
        return builder.toString();
    }*/

/* public static String getAgoTime(long time) {
        try {
            long m = System.currentTimeMillis() - (time * 1000);
            m = m / 60000;
            if (m == 0) {
                return "just now";
            } else if (m > 0 && m < 11) {
                if (m > 2)
                    return m + " mins ago";
                else
                    return m + " min ago";

            } else {
                return ChatAdapter.dateFormat.format(UtilMethods.getCalenderObject(time).getTime());
            }
        } catch (NumberFormatException e) {
            return "error";
        }
    }*/
/*  public static String getAgoWithdate(long s) {
        Calendar now = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(s * 1000);
        StringBuilder builder = new StringBuilder();
        if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
            builder.append("Today");
        } else if (now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1) {
            builder.append("Yesterday");
        } else
            builder.append(DateFormat.format("dd MMMM yyyy", time.getTimeInMillis()));

        builder.append(" " + getAgoTime(s));
        return builder.toString();
    }*/
fun convertDpToPixel(dp: Float): Int {
    val metrics = Resources.getSystem().displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return Math.round(px)
}

fun getRealPathFromUriNew(context: Context, uri: Uri): String {
    var filePath = ""
    val p = Pattern.compile("(\\d+)$")
    val m = p.matcher(uri.toString())
    if (!m.find()) {
        Timber.e("ID for requested image not found: $uri")
        return filePath
    }
    val imgId = m.group()
    val column = arrayOf(MediaStore.Images.Media.DATA)
    val sel = MediaStore.Images.Media._ID + "=?"
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        column, sel, arrayOf(imgId), null
    )
    val columnIndex = cursor!!.getColumnIndex(column[0])
    if (cursor.moveToFirst()) {
        filePath = cursor.getString(columnIndex)
    }
    cursor.close()
    return filePath
}


fun decodeBase64(data: String): String? {
    return String(Base64.decode(data.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT))
}

fun encodeBase64(data: String): String? {
    return String(Base64.encode(data.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT))
}

/*public String encodeMessage(String message) {
     *//* message = message.replaceAll("&", ":and:");
        message = message.replaceAll("\\+", ":plus:");*//*
        return StringEscapeUtils.escapeJava(message);
    }

    public String decodeMessage(String message) {
       *//* message = message.replaceAll(":and:", "&");
        message = message.replaceAll(":plus:", "+");*//*
        return StringEscapeUtils.unescapeJava(message);
    }*/

/*public String encodeMessage(String message) {
     */
/* message = message.replaceAll("&", ":and:");
        message = message.replaceAll("\\+", ":plus:");*/
/*
        return StringEscapeUtils.escapeJava(message);
    }

    public String decodeMessage(String message) {
       */
/* message = message.replaceAll(":and:", "&");
        message = message.replaceAll(":plus:", "+");*/
/*
        return StringEscapeUtils.unescapeJava(message);
    }*/
fun formatNumberWithSuffix(count: Long): String? {
    if (count < 1000) return "" + count
    val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        count / Math.pow(1000.0, exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}

fun currentDate(): Date? {
    val calendar = Calendar.getInstance()
    return calendar.time
}

fun getDateTimeFromTimestamp(value: Long?): Date? {
    val timestamp = Timestamp(TimeUnit.SECONDS.toMillis(value!!))
    return Date(timestamp.time)
}

fun getDateFormate(value: Long?): String? {
    val timestamp = Timestamp(TimeUnit.SECONDS.toMillis(value!!))
    val date = Date(timestamp.time)
    val simpleDateFormat = SimpleDateFormat("MMM dd yyyy")
    return simpleDateFormat.format(date)
}


fun dateFormat(date: String): String? {
    var new_date = ""
    val date_array = date.split("-".toRegex()).toTypedArray()
    if (date_array.size >= 2) {
        if (date_array[1].equals("01", ignoreCase = true)) {
            new_date = "Jan" + " " + date_array[2]
        } else if (date_array[1].equals("02", ignoreCase = true)) {
            new_date = "Feb" + " " + date_array[2]
        } else if (date_array[1].equals("02", ignoreCase = true)) {
            new_date = "Feb" + " " + date_array[2]
        } else if (date_array[1].equals("03", ignoreCase = true)) {
            new_date = "Mar" + " " + date_array[2]
        } else if (date_array[1].equals("04", ignoreCase = true)) {
            new_date = "Apr" + " " + date_array[2]
        } else if (date_array[1].equals("05", ignoreCase = true)) {
            new_date = "May" + " " + date_array[2]
        } else if (date_array[1].equals("06", ignoreCase = true)) {
            new_date = "Jun" + " " + date_array[2]
        } else if (date_array[1].equals("07", ignoreCase = true)) {
            new_date = "Jul" + " " + date_array[2]
        } else if (date_array[1].equals("08", ignoreCase = true)) {
            new_date = "Aug" + " " + date_array[2]
        } else if (date_array[1].equals("09", ignoreCase = true)) {
            new_date = "Sep" + " " + date_array[2]
        } else if (date_array[1].equals("10", ignoreCase = true)) {
            new_date = "Oct" + " " + date_array[2]
        } else if (date_array[1].equals("11", ignoreCase = true)) {
            new_date = "Nov" + " " + date_array[2]
        } else if (date_array[1].equals("12", ignoreCase = true)) {
            new_date = "Dec" + " " + date_array[2]
        }
    }
    return new_date
}

fun getThumbnil(file: File?): ByteArray? {
    var imageData: ByteArray? = null
    try {
        val THUMBNAIL_SIZE = 300
        val fis = FileInputStream(file)
        var imageBitmap = BitmapFactory.decodeStream(fis)
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false)
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        imageData = baos.toByteArray()
    } catch (ex: java.lang.Exception) {
    } finally {
        return imageData
    }
}

fun trimCache(context: Context) {
    try {
        val dir = context.cacheDir
        if (dir != null && dir.isDirectory) {
            deleteDir(dir)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

@Throws(java.lang.Exception::class)
fun deleteDir(dir: File?): Boolean {
    if (dir != null && dir.isDirectory) {
        val children = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
    }
    return dir!!.delete()
}


fun dateAfterSevenDay(date: String?): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val c = Calendar.getInstance()
    try {
        c.time = sdf.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    c.add(Calendar.DAY_OF_MONTH, 7)
    return sdf.format(c.time)
}

fun monthFromDate(date: String?): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("MMM")
    var datenew: Date? = null
    try {
        datenew = inputFormat.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return outputFormat.format(datenew)
}

fun createDirectoryAndSaveFile(imageToSave: Bitmap, fileName: String?) {
    val direct = File(Environment.getExternalStorageDirectory().toString() + "/BoardGame2Go/")
    if (!direct.exists()) {
        direct.mkdirs()
    }
    val file = File(direct, fileName)
    try {
        val out = FileOutputStream(file)
        imageToSave.compress(Bitmap.CompressFormat.PNG, 10, out)
        out.flush()
        out.close()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun createDirectory(imageToSave: ByteArrayBuffer, fileName: String?) {
    val blob = ByteArrayOutputStream()
    val direct = File(Environment.getExternalStorageDirectory().toString() + "/FolderName/")
    if (!direct.exists()) {
        direct.mkdirs()
    }
    val file = File(direct, fileName)
    try {
        val out = FileOutputStream(file)
        out.write(imageToSave.toByteArray())
        out.flush()
        out.close()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}


fun getSaveImages(): List<String>? {
    val list: MutableList<String> = ArrayList()
    val path = Environment.getExternalStorageDirectory().toString() + "/FolderName/"
    val directory = File(path)
    val files = directory.listFiles()
    if (files != null) {
        for (file in files) list.add(file.absolutePath)
    }
    return list
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss")
    val calendar = Calendar.getInstance()
    return dateFormat.format(calendar.time)
}


fun dateAfterTwoDay(date: String?): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val c = Calendar.getInstance()
    try {
        c.time = sdf.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    c.add(Calendar.DAY_OF_MONTH, 2)
    return sdf.format(c.time)
}

fun getdayName(dateis: String?): String? {
    val inFormat = SimpleDateFormat("yyyy-MM-dd")
    var date: Date? = null
    try {
        date = inFormat.parse(dateis)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val outFormat = SimpleDateFormat("EEEE")
    return outFormat.format(date)
}

fun getDayNameHalf(dateis: String?): String? {
    val inFormat = SimpleDateFormat("yyyy-MM-dd")
    var date: Date? = null
    try {
        date = inFormat.parse(dateis)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val outFormat = SimpleDateFormat("EEE")
    return outFormat.format(date)
}

fun compareDates(date1: Date, date2: Date): Boolean {
    var istrue = false
    if (date1.after(date2)) istrue = false
    if (date1.before(date2)) istrue = true
    if (date1 == date2) istrue = true
    return istrue
}

fun convertStringToDate(Date: String?): Date? {
    var date: Date? = null
    val format1 = SimpleDateFormat("yyyy-MM-dd")
    try {
        date = format1.parse(Date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date
}

fun setRotrationEffectiveBackground(
    context: Activity,
    configuration: Configuration,
    protimage: Int,
    landimage: Int
) {
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> context.window.setBackgroundDrawableResource(protimage)
        Configuration.ORIENTATION_LANDSCAPE -> context.window.setBackgroundDrawableResource(
            landimage
        )
    }
}


@Throws(IOException::class)
fun streamToString(`is`: InputStream?): String? {
    var str = ""
    if (`is` != null) {
        val sb = StringBuilder()
        var line: String?
        try {
            val reader = BufferedReader(
                InputStreamReader(`is`)
            )
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            reader.close()
        } finally {
            `is`.close()
        }
        str = sb.toString()
    }
    return str
}


/* load json from assets folder*/
@Throws(java.lang.Exception::class)
fun loadFileFromAsset(context: Context, fileName: String?): String? {
    val json: String
    val `is` = context.assets.open(fileName!!)
    val size = `is`.available()
    val buffer = ByteArray(size)
    `is`.read(buffer)
    `is`.close()
    json = String(buffer, StandardCharsets.UTF_8)
    return json
}

/*fun distance(lat1: String, lon1: String): String? {
    val theta = getDouble(lon1) - getDouble(PrefUtils.getInstance().longtitude)
    var dist = (Math.sin(deg2rad(getDouble(lat1)))
            * Math.sin(deg2rad(getDouble(PrefUtils.getInstance().latitude)))
            + Math.cos(deg2rad(getDouble(lat1))
            * Math.cos(deg2rad(getDouble(PrefUtils.getInstance().latitude)))
            * Math.cos(deg2rad(theta))))
    dist = Math.acos(dist)
    dist = rad2deg(dist)
    dist = dist * 60 * 1.1515
    return dist.toString()
}*/

fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}

fun getDouble(value: String): Double {
    return value.toDouble()
}

fun getAge(date: String): String? {
    val splitDate = date.split("-".toRegex()).toTypedArray()
    val year = splitDate[0].toInt()
    val month = splitDate[1].toInt()
    val day = splitDate[2].toInt()
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob[year, month] = day
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    return "$age years old"
}

/*public void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.techwin.nla",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT)); }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }*/

/*public void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.techwin.nla",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT)); }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }*/
fun rateApplication(context: Context) {
    val uri = Uri.parse("market://details?id=" + context.packageName)
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    // To count with Play market backstack, After pressing back button,
    // to taken back to our application, we need to add following flags to intent.
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        context.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
            )
        )
    }
}

/*
fun shareApplication(context: Context) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, """
     
     ${R.string.sharing_app_content}
     """.trimIndent()
        )
        context.startActivity(Intent.createChooser(shareIntent, "choose one"))
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun shareAppOnWhatsApp(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT, "\n" + context.getString(R.string.sharing_app_content))
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
*/

fun getRandomNumber(min: Int, max: Int): Int {
    return Random().nextInt(max - min + 1) + min
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

