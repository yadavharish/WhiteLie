package `in`.hyiitd.whitelie.deceits

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.os.Build
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule

object DeceivedAccountParcel: XC_MethodHook() {
    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return

        if(AccountManager.get(DeceiverModule.ctx).authenticatorTypes.find { it.type == (param.result as Account? ?: return).type && it.packageName == getProcessName() } == null) {
            if(DeceiverModule.ctx != null) {
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
                    param.result = Account(deceitStringsMap["accountName"] ?: "deceiver@hyiitd.in", deceitStringsMap["accountType"] ?: "com.google")
                }
            }
            else param.result = Account("deceiver@hyiitd.in", "com.google")
        }
    }
    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    fun getProcessName() = if (Build.VERSION.SDK_INT >= 28) Application.getProcessName() else Class.forName("android.app.ActivityThread").getDeclaredMethod("currentProcessName").invoke(null) as String
}