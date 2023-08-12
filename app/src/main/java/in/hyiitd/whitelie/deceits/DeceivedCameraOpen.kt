package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook
import java.lang.RuntimeException

object DeceivedCameraOpen: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        param.result = RuntimeException("deceived")
    }
}