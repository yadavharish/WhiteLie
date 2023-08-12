package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule

object StringReturnTypeDeceiver2: XC_MethodHook() {
    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return

        param.result = if(DeceiverModule.ctx != null) {
            val cursor = DeceiverModule.ctx!!.contentResolver.query(Uri.parse("content://in.hyiitd.deceiver.provider/deceitStrings"), null, null, null, null)
            val deceitStringsMap = mutableMapOf<String, String>()
            if (cursor != null) {
                if (cursor.moveToFirst())
                    while (!cursor.isAfterLast) {
                        val names = cursor.getString(cursor.getColumnIndex("name")).split(" ")
                        deceitStringsMap[names[0]] = names[1]
                        cursor.moveToNext()
                    }
                cursor.close()

                when(param.thisObject.javaClass.name) {
                    "android.net.wifi.WifiInfo" -> when (param.method.name) {
                        "getBSSID" -> deceitStringsMap["wifiInfoBSSID"]
                        "getSSID" -> deceitStringsMap["wifiInfoSSID"]
                        else -> param.result
                    }
                    else -> param.result
                }
            }
            else param.result
        }
        else param.result
    }
}