package `in`.hyiitd.whitelie.deceits

import de.robv.android.xposed.callbacks.XC_LoadPackage

class MethodsDeceived {
    fun getLocationMethod(clsName: String, mthdName: String, lpparam: XC_LoadPackage.LoadPackageParam) {
        when(clsName) {
            "android.location.Location" -> {
            }
        }

    }

}