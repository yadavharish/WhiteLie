package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook

object Blocker: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        param.result = null //setting result as null in beforeHookedMethod() will block the original method to be called
    }
}