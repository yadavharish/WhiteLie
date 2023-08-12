package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook
import com.google.android.gms.analytics.GoogleAnalytics
import com.segment.analytics.Analytics
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.hyiitd.whitelie.util.PreferencesUtil.sharedPrefs

object AnalyticsDeceiver: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        when(param.thisObject.javaClass.name) {
            "com.google.android.gms.analytics.GoogleAnalytics" -> {
                when (param.method.name) {
                    "getInstance" ->  (param.result as GoogleAnalytics).setDryRun(sharedPrefs.getBoolean("deceitsGoogleAnalyticsDryRun", true))
                    "setDryRun" -> param.args[0] = sharedPrefs.getBoolean("deceitsGADryRun", true)
                    else -> {}
                }
            }
            "com.segment.analytics.Analytics" -> {
                when (param.method.name) {
                    "setSingletonInstance" ->  (param.args[0] as Analytics ?: return).optOut(sharedPrefs.getBoolean("deceitsSegmentAnalyticsOptOut", true))
                    "optOut" -> param.args[0] = sharedPrefs.getBoolean("deceitsSegmentOptOut", true)
                    else -> {}
                }
            }
            "com.google.firebase.analytics.FirebaseAnalytics" -> {
                when (param.method.name) {
                    "getInstance" -> (param.result as FirebaseAnalytics ?: return).setAnalyticsCollectionEnabled(sharedPrefs.getBoolean("deceitsFirebaseAnalyticsCollection", false))
                    "setAnalyticsCollectionEnabled" -> param.args[0] = sharedPrefs.getBoolean("deceitsFirebaseAnalyticsCollection", false)
                    else -> {}
                }
            }
            else -> {}
        }
    }
}