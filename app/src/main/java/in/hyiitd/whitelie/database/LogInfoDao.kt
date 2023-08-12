package `in`.hyiitd.whitelie.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LogInfoDao {
    @Query("SELECT * FROM logInfo")
    fun loadAll(): List<LogInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logInfo: LogInfo)

    @Query("DELETE FROM logInfo")
    fun deleteAll()
}