package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.util.PreferencesUtil.sharedPrefs

object IntegerReturnTypeDeceiver: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return
        param.result = when (param.thisObject.javaClass.name) {
            "android.hardware.Camera" -> if (param.method.name == "getNumberOfCameras") sharedPrefs.getInt("deceitsNoOfCams", 0) else { }
            "android.telephony.SubscriptionInfo" -> when(param.method.name) {
                "getMnc", "getMcc" -> "deceits${param.method.name.subSequence(3..param.method.name.length)}"
                else -> return
            }
            else -> return
        }
    }
}