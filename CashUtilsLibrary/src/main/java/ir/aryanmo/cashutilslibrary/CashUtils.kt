package ir.aryanmo.cashutilslibrary

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log

object CashUtils {

    fun saveFile(cashUri: String, image: Bitmap): Boolean {
        return try {
//            CashStore.instance!!.saveCacheFile(cashUri, image)
            true
        } catch (e: Exception) {
            logError("saveFile", e)
            false
        }
    }

    fun getFile(cashUri: String): Bitmap? {
        return try {
            return null
//            CashStore.instance!!.getCacheFile(cashUri)
        } catch (e: Exception) {
            logError("getFile", e)
            null
        }
    }

    private fun logError(s: String, e: Exception) {
        Log.e("CashUtilsLibrary", "CashUtils::$s  Error-> ${e.message}")
    }
}
