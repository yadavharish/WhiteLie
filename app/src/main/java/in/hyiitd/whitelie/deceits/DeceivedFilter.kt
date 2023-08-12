package `in`.hyiitd.whitelie.deceits

import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import de.robv.android.xposed.XC_MethodHook
import android.os.Process

object DeceivedFilter: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        val list = param.result as MutableList<*>? ?: return
        for(i in list.size downTo 1) {
            val uid: Int = if (list[i] == null) -1
            else when (param.method.name) {
                "getInstalledPackages", "getPackagesHoldingPermissions", "getPreferredPackages" -> (list[i] as PackageInfo).applicationInfo.uid
                "queryIntentActivities", "queryIntentActivityOptions" -> (list[i] as ResolveInfo).activityInfo.applicationInfo.uid
                "queryIntentContentProviders" -> (list[i] as ResolveInfo).providerInfo.applicationInfo.uid
                "queryIntentServices" -> (list[i] as ResolveInfo).serviceInfo.applicationInfo.uid
                else -> -1
            }
            if (uid != Process.myUid()) list.removeAt(i) //I'm not sure if this (Process.myUid()) will work
        }
        param.result = list
    }
}