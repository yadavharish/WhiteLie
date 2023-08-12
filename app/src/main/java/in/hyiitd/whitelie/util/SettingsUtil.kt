package `in`.hyiitd.whitelie.util

import android.content.Context
import android.content.Intent
import android.net.Uri

internal object SettingsUtil {
    @JvmStatic
    fun viewSourceCode(ctx: Context) = ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yadavharishiitd/deceiver")))

    @JvmStatic
    fun viewDocumentation(ctx: Context) = ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yadavharishiitd/deceiver")))
}