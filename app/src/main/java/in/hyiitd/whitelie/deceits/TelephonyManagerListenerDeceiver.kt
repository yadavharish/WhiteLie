package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.XC_MethodHook

object TelephonyManagerListenerDeceiver: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        if(param.args[1] as Int and 1024 != 0) param.args[1] = (param.args[1] as Int xor 1024)
        else if(param.args[1] as Int and 16 != 0) param.args[1] = (param.args[1] as Int xor 16)
    }
}

//need to learn about the event flag