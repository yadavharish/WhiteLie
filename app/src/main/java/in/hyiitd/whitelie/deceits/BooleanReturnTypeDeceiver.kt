package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook

object BooleanReturnTypeDeceiver: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return
        param.result = false
    }
}