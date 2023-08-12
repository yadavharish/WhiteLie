package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentResolver
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge.*
import `in`.hyiitd.whitelie.DeceiverModule

object DeceivedClipdataParcel: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        val uri = Uri.parse("content://in.hyiitd.deceiver.provider/deceitSettings")
        if(DeceiverModule.ctx != null) {
            val cr: ContentResolver = DeceiverModule.ctx!!.contentResolver
            val cursor = cr.query(uri, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst())
                    while (!cursor.isAfterLast) {
                        if(cursor.getString(cursor.getColumnIndex("name")).split(" ")[0] == "batterySaver") {
                            if(cursor.getString(cursor.getColumnIndex("name")).split(" ")[1] == "true")
                                param.result = null
                            break
                        }
                        cursor.moveToNext()
                    }
                cursor.close()
            }
        }
    }

    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return

        if(DeceiverModule.ctx != null) {
            DeceiverModule.addLog("clipboard data requested by")
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
                param.result = ClipData.newPlainText(deceitStringsMap["clipboardLabel"] ?: "deceiver", deceitStringsMap["clipboardText"] ?: "deceived")
                DeceiverModule.addLog("clipboard text deceived to ${deceitStringsMap["clipboardText"]} when requested by")
            }
        }
        else param.result = ClipData.newPlainText("deceiver", "deceived")

    }
}