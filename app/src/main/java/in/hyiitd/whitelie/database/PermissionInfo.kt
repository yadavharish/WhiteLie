package `in`.hyiitd.whitelie.database

import androidx.room.Entity

@Entity(primaryKeys = ["packageName", "permissionName"])
data class PermissionInfo(
    val packageName: String,
    val permissionName: String,
    val granted: Boolean,
    var deceived: Boolean,
    var deceivedTime: String
)