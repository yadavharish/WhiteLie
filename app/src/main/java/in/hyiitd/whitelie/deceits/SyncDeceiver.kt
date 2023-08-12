package `in`.hyiitd.whitelie.deceits

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.SyncInfo
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule
import org.lsposed.hiddenapibypass.HiddenApiBypass

object SyncDeceiver: XC_MethodHook() {
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return

        param.result = if(DeceiverModule.ctx != null) {
            val cursor = DeceiverModule.ctx!!.contentResolver.query(Uri.parse("content://in.hyiitd.deceiver.provider/deceitStrings"), null, null, null, null)
            val deceitStringsMap = mutableMapOf<String, String>()
            if (cursor != null) {
                if (cursor.moveToFirst())
                    while (!cursor.isAfterLast) {
                        val names = cursor.getString(cursor.getColumnIndex("name")).split(" ")
                        deceitStringsMap[names[0]] = names[1]
                        cursor.moveToNext()
                    }
                cursor.close()

                val instance: Any = HiddenApiBypass.newInstance(Class.forName("android.content.SyncInfo"), deceitStringsMap["syncAuthorityId"] ?: 0, Account(deceitStringsMap["syncAccountName"] ?: "deceiver@hyiitd.in", deceitStringsMap["syncAccountType"] ?: "com.google"), deceitStringsMap["syncAuthority"] ?: "deceivedAuthority", deceitStringsMap["syncStartTime"]?.toLong() ?: 0.toLong())

                when(param.method.name) {
                    "getCurrentSync" -> instance as SyncInfo
                    "getCurrentSyncs" -> mutableListOf(instance as SyncInfo)
                    else -> param.result
                }
            }
            else param.result
        }
        else param.result
    }
}