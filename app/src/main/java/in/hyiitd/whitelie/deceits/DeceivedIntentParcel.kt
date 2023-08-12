package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import de.robv.android.xposed.XC_MethodHook
import android.net.Uri
import android.os.Build

object DeceivedIntentParcel: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        val intent = param.result as Intent? ?: return
        when (intent.action ?: return) {
            "android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_DATA_CLEARED", "android.intent.action.PACKAGE_FIRST_LAUNCH", "android.intent.action.PACKAGE_FULLY_REMOVED", "android.intent.action.PACKAGE_INSTALL", "android.intent.action.PACKAGE_NEEDS_VERIFICATION", "android.intent.action.PACKAGE_REMOVED", "android.intent.action.PACKAGE_REPLACED", "android.intent.action.PACKAGE_RESTARTED", "android.intent.action.PACKAGE_VERIFIED" -> intent.data = Uri.parse("package:${getProcessName()}")
            "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE", "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE", "android.intent.action.PACKAGES_SUSPENDED", "android.intent.action.PACKAGES_UNSUSPENDED" -> intent.putExtra("android.intent.extra.changed_package_list", emptyArray<String>())
            "android.provider.Telephony.SMS_RECEIVED" -> intent.putExtra("pdus", emptyArray<Any>())
            else -> return
        }
        param.result = intent
    }

    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun getProcessName() = if (Build.VERSION.SDK_INT >= 28) Application.getProcessName() else Class.forName("android.app.ActivityThread").getDeclaredMethod("currentProcessName").invoke(null) as String
}