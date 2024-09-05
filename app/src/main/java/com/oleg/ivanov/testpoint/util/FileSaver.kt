package com.oleg.ivanov.testpoint.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.oleg.ivanov.testpoint.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileSaver {

    suspend fun saveBitmapToPictures(context: Context, bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            var saveSuccess = false
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "${AppSettings.FILE_IMAGE}$timeStamp.jpg"

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                } else {
                    val picturesDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                    put(MediaStore.Images.Media.DATA, "$picturesDir/$fileName")
                }
            }

            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )?.let {
                saveSuccess = try {
                    val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
                    outputStream?.use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    }
                    true
                } catch (e: IOException) {
                    e.printStackTrace()
                    false
                }
            }
            saveSuccess
        }
    }
}
