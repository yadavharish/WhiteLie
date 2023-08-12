package `in`.hyiitd.whitelie.database

import androidx.room.*

@Dao
interface AppInfoDao {
    @Query("SELECT * FROM appInfo")
    fun loadAll(): List<AppInfo>

    @Query("SELECT * FROM appInfo WHERE packageName = :pkgName")
    fun loadByPackageName(pkgName: String): List<AppInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appInfo: AppInfo)

    @Query("DELETE FROM appInfo")
    fun deleteAll()
}