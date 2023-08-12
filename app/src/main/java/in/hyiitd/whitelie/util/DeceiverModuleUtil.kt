package `in`.hyiitd.whitelie.util

import android.content.Context
import `in`.hyiitd.whitelie.MainActivity.Companion.isXposedInstalled


internal object DeceiverModuleUtil {
    @JvmStatic var batterySaver = false
    @JvmStatic var xposedBridgeFilePath = "/system/framework/XposedBridge.jar"
    @JvmStatic var xposedBridgeClassName = "de.robv.android.xposed.XposedBridge"
    @JvmStatic var xposedBridgeGetVersionMethodName = "getXposedVersion"

    @JvmStatic
    fun isXPModuleInstalled(context: Context): Boolean {
//        try {
//            if (File(xposedBridgeFilePath).exists()) {
//                val getXposedVersion = DexClassLoader(File(xposedBridgeFilePath).path, context.getDir("dex", MODE_PRIVATE).path, null, getSystemClassLoader()).loadClass(xposedBridgeClassName).getDeclaredMethod(xposedBridgeGetVersionMethodName)
//                getXposedVersion.isAccessible = true
//                getXposedVersion.invoke(null)?.toString()?.toInt()
//            }
//            null
//        } catch (_: Exception) { null }
        return isXposedInstalled
    }

}