package ir.aryanmo.cashutilslibrary

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.StreamCorruptedException
import java.text.SimpleDateFormat
import java.util.HashMap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log

class CashStore private constructor() {
    private var cacheMap: HashMap<String, String>? = null
    private val bitmapMap: HashMap<String, Bitmap>

    init {
        cacheMap = HashMap()
        bitmapMap = HashMap()
        val fullCacheDir = File(Environment.getExternalStorageDirectory().toString(), cacheDir)
        if (!fullCacheDir.exists()) {
            Log.i("CACHE_UTILS", "Directory doesn't exist")
            cleanCacheStart()

        } else
            try {
                val ois = ObjectInputStream(BufferedInputStream(FileInputStream(File(fullCacheDir.toString(), CACHE_UTILS_FILENAME))))
                cacheMap = ois.readObject() as HashMap<String, String>
                ois.close()
            } catch (e: StreamCorruptedException) {
                Log.i("CACHE_UTILS", "Corrupted stream")
                cleanCacheStart()
            } catch (e: FileNotFoundException) {
                Log.i("CACHE_UTILS", "File not found")
                cleanCacheStart()
            } catch (e: IOException) {
                Log.i("CACHE_UTILS", "Input/Output error")
                cleanCacheStart()
            } catch (e: ClassNotFoundException) {
                Log.i("CACHE_UTILS", "Class not found")
                cleanCacheStart()
            }

    }

    private fun cleanCacheStart() {
        cacheMap = HashMap()
        val fullCacheDir = File(Environment.getExternalStorageDirectory().toString(), cacheDir)
        fullCacheDir.mkdirs()
        val noMedia = File(fullCacheDir.toString(), ".nomedia")
        try {
            noMedia.createNewFile()
            Log.i("CACHE_UTILS", "Cache created")
        } catch (e: IOException) {
            Log.i("CACHE_UTILS", "Couldn't create .nomedia file")
            e.printStackTrace()
        }

    }

    fun saveCacheFile(cacheUri: String, image: Bitmap) {
        val fullCacheDir = File(Environment.getExternalStorageDirectory().toString(), cacheDir)
        val fileLocalName = SimpleDateFormat("ddMMyyhhmmssSSS").format(java.util.Date()) + ".PNG"
        val fileUri = File(fullCacheDir.toString(), fileLocalName)
        var outStream: FileOutputStream? = null
        try {
            outStream = FileOutputStream(fileUri)
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
            cacheMap!![cacheUri] = fileLocalName
            Log.i("CACHE_UTILS", "Saved file " + cacheUri + " (which is now " + fileUri.toString() + ") correctly")
            bitmapMap[cacheUri] = image
            val os = ObjectOutputStream(BufferedOutputStream(
                    FileOutputStream(File(fullCacheDir.toString(), CACHE_UTILS_FILENAME))))
            os.writeObject(cacheMap)
            os.close()
        } catch (e: FileNotFoundException) {
            Log.i("CACHE_UTILS", "Error: File $cacheUri was not found!")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.i("CACHE_UTILS", "Error: File could not be stuffed!")
            e.printStackTrace()
        }

    }

    fun getCacheFile(cacheUri: String): Bitmap? {
        if (bitmapMap.containsKey(cacheUri)) return bitmapMap[cacheUri] as Bitmap

        if (!cacheMap!!.containsKey(cacheUri)) return null
        val fileLocalName = cacheMap!![cacheUri].toString()
        val fullCacheDir = File(Environment.getExternalStorageDirectory().toString(), cacheDir)
        val fileUri = File(fullCacheDir.toString(), fileLocalName)
        if (!fileUri.exists()) return null

        //        Log.i("CACHE_UTILS", "File " + cacheUri + " has been found in the Cache");
        val bm = BitmapFactory.decodeFile(fileUri.toString())
        bitmapMap[cacheUri] = bm
        return bm
    }

    companion object {


        private var INSTANCE: CashStore? = null
        private val cacheDir = "/Android/data/ir.aryanmo.cashUtils/cache/"
        private val CACHE_UTILS_FILENAME = ".cache"

        @Synchronized
        private fun createInstance() {
            if (INSTANCE == null) {
                INSTANCE = CashStore()
            }
        }

        val instance: CashStore?
            get() {
                if (INSTANCE == null) createInstance()
                return INSTANCE
            }
    }
}
