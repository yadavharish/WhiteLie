package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.location.Location
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge.*
import `in`.hyiitd.whitelie.DeceiverModule
import `in`.hyiitd.whitelie.DeceiverModule.Companion.addLog

object LocationDeceiver: XC_MethodHook() {
    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        addLog("location data requested by")
        with(param) {
            if(thisObject.javaClass.name == "com.google.android.gms.location.Geofence\$Builder") {
                val uri = android.net.Uri.parse("content://in.hyiitd.deceiver.provider/deceitStrings")
                if(DeceiverModule.ctx != null) {
                    val cr: ContentResolver = `in`.hyiitd.deceiver.DeceiverModule.ctx!!.contentResolver
                    val cursor = cr.query(uri, null, null, null, null)
                    if (cursor != null) {
                        if (cursor.moveToFirst())
                            while (!cursor.isAfterLast) {
                                if(cursor.getString(cursor.getColumnIndex("name")).split(" ")[0] == "locationLatitude") args[0] = cursor.getString(cursor.getColumnIndex("name")).split(" ")[1].toFloat()
                                else if(cursor.getString(cursor.getColumnIndex("name")).split(" ")[0] == "locationLongitude") args[1] = cursor.getString(cursor.getColumnIndex("name")).split(" ")[1].toFloat()
                                else if(cursor.getString(cursor.getColumnIndex("name")).split(" ")[0] == "locationRadius") args[2] = cursor.getString(cursor.getColumnIndex("name")).split(" ")[1].toFloat()
                                cursor.moveToNext()
                            }
                        cursor.close()
                    }
                }
            }
        }
    }

    @SuppressLint("Range")
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result != null) {
            var latitude = 0.0F
            var longitude = 0.0F
            with(param) {
                val uri =
                    android.net.Uri.parse("content://in.hyiitd.deceiver.provider/deceitStrings")
                if (DeceiverModule.ctx != null) {
                    val cr: ContentResolver =
                        `in`.hyiitd.deceiver.DeceiverModule.ctx!!.contentResolver
                    val cursor = cr.query(uri, null, null, null, null)
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            while (!cursor.isAfterLast) {
                                if (cursor.getString(cursor.getColumnIndex("name"))
                                        .split(" ")[0] == "locationLatitude"
                                ) latitude = cursor.getString(cursor.getColumnIndex("name"))
                                    .split(" ")[1].toFloat()
                                else if (cursor.getString(cursor.getColumnIndex("name"))
                                        .split(" ")[0] == "locationLongitude"
                                ) longitude = cursor.getString(cursor.getColumnIndex("name"))
                                    .split(" ")[1].toFloat()
                                cursor.moveToNext()
                            }
                        }
                        cursor.close()
                        when (thisObject.javaClass.name) {
                            android.location.Location.CREATOR.javaClass.name -> {
                                (result as Location).latitude = latitude.toDouble()
                                (result as Location).longitude = longitude.toDouble()
                            }
                            "android.os.BaseBundle" -> {
                                if (!hasThrowable()) {
                                    val newLoc = Location("")
                                    newLoc.latitude = latitude.toDouble()
                                    newLoc.longitude = longitude.toDouble()
                                    result = newLoc
                                } else {
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
            addLog("location data deceived to: Lat:$latitude, Long:$longitude; when requested by")
        }
    }
}