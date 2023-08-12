package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook

object DeceivedAsEmptyList: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null || (param.result as Array<*>).isEmpty()) return
        param.result = ArrayList<Any>()
    }
}