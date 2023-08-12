package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule

object TrackingDeceiver: XC_MethodHook() {
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
                    "android.os.Build" -> when(param.method.name) {
                        "getRadioVersion" -> deceitStringsMap["buildRadioVersion"]
                        "getSerial" -> deceitStringsMap["serialNumber"]
                        "getString" -> when(param.args[0]) {
                            "ro.bootloader" -> deceitStringsMap["buildBootLoader"]
                            "ro.boot.hardware.sku" -> deceitStringsMap["buildSku"]
                            "ro.boot.product.hardware.sku" -> deceitStringsMap["buildOdmSku"]
                            "ro.boot.qemu" -> deceitStringsMap["buildQEMU"]
                            "ro.build.id" -> deceitStringsMap["buildId"]
                            "ro.build.display.id" -> deceitStringsMap["buildDisplay"]
                            "ro.build.host" -> deceitStringsMap["buildHost"]
                            "ro.build.tags" -> deceitStringsMap["buildTags"]
                            "ro.build.type" -> deceitStringsMap["buildType"]
                            "ro.build.user" -> deceitStringsMap["buildUser"]
                            "ro.hardware" -> deceitStringsMap["buildHardware"]
                            "ro.product.board" -> deceitStringsMap["buildBoard"]
                            "ro.product.brand" -> deceitStringsMap["buildBrand"]
                            "ro.product.device" -> deceitStringsMap["buildDevice"]
                            "ro.product.manufacturer" -> deceitStringsMap["buildManufacturer"]
                            "ro.product.model" -> deceitStringsMap["buildModel"]
                            "ro.product.name" -> deceitStringsMap["buildProduct"]
                            else -> param.result
                        }
                        else -> param.result
                    }
                    "android.os.SystemProperties" -> when(param.method.name) {
                        "get" -> {
                            val key = param.args[0] as String
                            if (key.startsWith("ro.build.")) param.result
                            else if (key.startsWith("ro.vendor.")) param.result
                            else when (key) {
                                "gsm.operator.alpha", "gsm.sim.operator.alpha" -> deceitStringsMap["operatorName"]
                                "gsm.operator.numeric", "gsm.sim.operator.numeric" -> "${deceitStringsMap["mcc"]}${deceitStringsMap["mnc"]}"
                                "gsm.operator.iso-country", "gsm.sim.operator.iso-country" -> deceitStringsMap["countryIso"]
                                "ro.serialno", "ro.boot.serialno" -> deceitStringsMap["serialNumber"]
                                else -> param.result
                            }
                        }
                        else -> param.result
                    }
                    "android.provider.Settings\$Secure" -> when(param.method.name) {
                        "getString" -> when(param.args[1]) {
                            "android_id" -> deceitStringsMap["androidId"]
                            "bluetooth_name" -> deceitStringsMap["bluetoothName"]
                            else -> param.result
                        }
                        else -> param.result
                    }
                    "android.telephony.SubscriptionInfo" -> when (param.method.name) {
                        "getCarrierName" -> deceitStringsMap["subscriptionInfoCarrierName"]
                        "getCountryIso" -> deceitStringsMap["countryIso"]
                        else -> param.result
                    }
                    "android.telephony.TelephonyManager" -> when (param.method.name) {
                        "getNetworkOperatorName", "getSimOperatorName" -> deceitStringsMap["operatorName"]
                        "getSimCountryIso", "getNetworkCountryIso" -> deceitStringsMap["countryIso"]
                        "getNetworkOperator", "getSimOperator" -> "${deceitStringsMap["mcc"]}${deceitStringsMap["mnc"]}"
                        else -> param.result
                    }
                    "com.google.android.gms.ads.identifier.AdvertisingIdClient\$Info" -> when(param.method.name) {
                        "getId" -> deceitStringsMap["adClientId"]
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