package `in`.hyiitd.whitelie.database

import androidx.room.*

@Dao
interface PermissionInfoDao {
    @Query("SELECT * FROM permissionInfo")
    fun loadAll(): List<PermissionInfo>

    @Query("SELECT permissionName FROM permissionInfo WHERE packageName = :pkgName AND deceived = 1")
    fun loadAllDeceivedPermissionsByPackageName(pkgName: String): List<String>

    @Query("SELECT * FROM permissionInfo WHERE deceived = 1")
    fun loadAllDeceivedPermissions(): List<PermissionInfo>

    @Query("SELECT * FROM permissionInfo WHERE packageName = :pkgName")
    fun loadAllByPackageName(pkgName: String): List<PermissionInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(permInfo: PermissionInfo)

    @Query("DELETE FROM permissionInfo")
    fun deleteAll()

    @Query("UPDATE permissionInfo SET deceived = :deceived, deceivedTime = :deceivedTime WHERE packageName = :pkgName AND permissionName = :permName")
    fun updateDeceivedStatus(pkgName: String, permName: String, deceived: Boolean, deceivedTime: String)

    @Query("UPDATE permissionInfo SET deceived = 0")
    fun clearAllDeceived()
}