package `in`.hyiitd.whitelie.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PermissionInfo::class, AppInfo::class, LogInfo::class], version = 1)
abstract class DeceiverDatabase : RoomDatabase() {
    abstract fun permDao(): PermissionInfoDao
    abstract fun appDao(): AppInfoDao
    abstract fun logsDao(): LogInfoDao
}