package `in`.hyiitd.whitelie.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PermsUtil {
    @JvmStatic var supportedPermissions = emptyList<SupportedPerm>()

    @JvmStatic
    fun initSupportedPermissions(ctx: Context) {
        val supportedPermsJson = ctx.assets.open("supported_permissions.json").bufferedReader().use { it.readText() }
        val listSupportedPermsType = object: TypeToken<List<SupportedPerm>>() {}.type
        supportedPermissions = Gson().fromJson(supportedPermsJson, listSupportedPermsType)
    }

    @JvmStatic
    fun getPermissionIcon(permName: String, outline: Boolean, context: Context) = ContextCompat.getDrawable(context, context.resources.getIdentifier(getPermissionIconName(permName, outline), "drawable", context.packageName))!!

    @JvmStatic
    fun getPermissionIconName(permName: String, outline: Boolean): String = "ic_${when(permName) {
        "activityRecognition" -> "activity"
        "callLog" -> "call_log"
        else -> permName
    }}_${if(outline) "outline" else "solid"}"

    @JvmStatic
    fun isPackageDeceived(pkgName: String): Boolean = DBUtil.db.permDao().loadAllDeceivedPermissionsByPackageName(pkgName).isNotEmpty()

    @JvmStatic
    fun isPermissionDeceivedForPackage(permName: String, pkgName: String): Boolean = DBUtil.db.permDao().loadAllDeceivedPermissionsByPackageName(pkgName).contains(permName)
}