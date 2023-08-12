package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule

object TelephonyDeceiver: XC_MethodHook() {
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
                    "android.telephony.SubscriptionInfo" -> when(param.method.name) {
                        "getIccId" -> deceitStringsMap["iccId"]
                        "getMcc" -> deceitStringsMap["mcc"]
                        "getMnc" -> deceitStringsMap["mnc"]
                        "getNumber" -> deceitStringsMap["phoneNumber"]
                        else -> param.result
                    }
                    "android.telephony.TelephonyManager" -> when (param.method.name) {
                        "getImei" -> deceitStringsMap["telephonyImei"]
                        "getMeid" -> deceitStringsMap["telephonyMeid"]
                        "getLine1Number", "getVoiceMailNumber" -> deceitStringsMap["phoneNumber"]
                        "getSimSerialNumber" -> deceitStringsMap["simSerialNumber"]
                        "getSubscriberId" -> deceitStringsMap["subscriberId"]
                        "getVoiceMailAlphaTag" -> deceitStringsMap["voiceMailAlphaTag"]
                        "getDeviceId" -> when(param.thisObject.javaClass.getMethod("getPhoneType").invoke(null)) {
                            1 -> deceitStringsMap["telephonyImei"]
                            2 -> deceitStringsMap["telephonyMeid"]
                            else -> param.result
                        }
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