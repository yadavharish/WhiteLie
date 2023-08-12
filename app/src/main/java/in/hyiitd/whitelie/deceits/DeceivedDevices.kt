package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook
import android.media.AudioDeviceInfo

object DeceivedDevices: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        val result = param.result
        if(result == null || (result as Array<*>).isEmpty()) return
        param.result = emptyArray<AudioDeviceInfo>()
    }
}