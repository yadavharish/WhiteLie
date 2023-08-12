package `in`.hyiitd.whitelie.database

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class LogInfo (
    val time: String,
    val appName: String,
    val packageName: String,
    val msg: String) {
    @PrimaryKey(autoGenerate = true) var logId: Int = 0
    @Ignore var icon: Drawable? = null
    @Ignore var systemApp: Boolean = false
}