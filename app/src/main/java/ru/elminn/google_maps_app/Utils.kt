package ru.elminn.google_maps_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val outputPath: String
        get() {
            val path = Environment.getExternalStorageDirectory().toString() //+ File.separator + APP_FOLDER + File.separator

            val folder = File(path)
            if (!folder.exists())
                folder.mkdirs()

            return path
        }
    fun addFragmentToActivity(
            fragmentManager: androidx.fragment.app.FragmentManager,
            fragment: androidx.fragment.app.Fragment, frameId: Int
    ) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    fun refreshGallery(path: String, context: Context?) {

        val file = File(path)
        try {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(file)
            mediaScanIntent.data = contentUri
            context!!.sendBroadcast(mediaScanIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getConvertedFile(folder: String, fileName: String): File {
        val f = File(folder)

        if (!f.exists())
            f.mkdirs()

        return File(f.path + File.separator + fileName)
    }

    fun calculationOfAge(str: String): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(str)
            dob.time = date
        } catch (e: IllegalArgumentException) {
            return "-"
        }

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val ageInt = age

        return ageInt.toString()
    }

    fun status(gender: String, status: String): String {
        if (gender == "male") {
            return when (status) {
                "free" -> "Свободен"
                "in_love" -> "Влюблен"
                "not_in_love" -> "Не женат"
                "marriage" -> "Женат"
                "engaged" -> "Помолвлен"
                "complicated" -> "Все сложно"
                else -> "Свободен"
            }
        } else {
            return when (status) {
                "free" -> "Свободна"
                "in_love" -> "Влюблена"
                "not_in_love" -> "Не замужем"
                "marriage" -> "Замужем"
                "engaged" -> "Помолвлена"
                "complicated" -> "Все сложно"
                else -> "Свободна"
            }
        }
    }

    fun gender(gender: String) = if (gender == "male") "Мужчина" else "Женщина"

    fun reversStatus(status: String): String {
        return when (status) {
            "Свободна", "Свободен" -> "free"
            "Влюблена", "Влюблен" -> "in_love"
            "Не замужем", "Не женат" -> "not_in_love"
            "Замужем", "Женат" -> "marriage"
            "Помолвлена", "Помолвлен" -> "engaged"
            "Все сложно" -> "complicated"
            else -> "free"
        }
    }

    fun updateStatus(gender: String, status: String): String {
        return status(gender, reversStatus(status))
    }

    fun reversGender(gender: String): String {
        return when (gender) {
            "Мужчина" -> "male"
            "Женщина" -> "female"
            else -> "None"
        }
    }

    fun reversGenderNullable(gender: String): String? {
        return when (gender) {
            "Мужчина" -> "male"
            "Женщина" -> "female"
            else -> null
        }
    }
    fun reversCityAndRegionNullable(value: String): String? {
        return when (value) {
            "Не выбрано" -> null
            else -> value
        }
    }


    fun fixDate(str: String) = str.replace('-', ' ')

    fun reversefixDate(str: String) = str.replace(' ', '-')



}

fun Fragment?.closeKeyboard() {
    val view = this?.activity!!.currentFocus
    if (view != null) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}