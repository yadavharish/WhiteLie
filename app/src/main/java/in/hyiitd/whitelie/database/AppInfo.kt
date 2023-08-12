package `in`.hyiitd.whitelie.database

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class AppInfo(
    val appName: String,
    @PrimaryKey val packageName: String,
    val systemApp: Boolean) {
    @Ignore var icon: Drawable? = null
    @Ignore var noOfReqPerm: Int = 0
    @Ignore var noOfGrantPerm: Int = 0
    @Ignore var noOfDeceivedPerm: Int = 0
    @Ignore var permissions: List<PermissionInfo> = emptyList()
}