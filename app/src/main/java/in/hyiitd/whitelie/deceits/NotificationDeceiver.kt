package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook
import android.app.Notification

object NotificationDeceiver: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        val result = param.result as Notification ?: return
        param.result = Notification(result.icon, "NotificationByDeceiver", result.`when`)
    }
}