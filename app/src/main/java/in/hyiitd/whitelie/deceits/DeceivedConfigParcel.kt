package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook
import android.content.res.Configuration
import `in`.hyiitd.whitelie.util.PreferencesUtil.sharedPrefs

object DeceivedConfigParcel: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        (param.result as Configuration).mcc = sharedPrefs.getInt("deceitsMcc", 0)
        (param.result as Configuration).mnc = sharedPrefs.getInt("deceitsMnc", 0)
    }
}