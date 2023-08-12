package `in`.hyiitd.whitelie.util

import android.content.Context
import androidx.room.Room
import `in`.hyiitd.whitelie.MainActivity
import `in`.hyiitd.whitelie.database.DeceiverDatabase

internal object DBUtil {
    @JvmStatic val dbName = "deceiver-database"
    @JvmStatic lateinit var db: DeceiverDatabase

    @JvmStatic
    fun initDB(ctx: Context?) {
        val context: Context = (ctx ?: MainActivity.context) as Context
        db = Room.databaseBuilder(context, DeceiverDatabase::class.java, dbName).allowMainThreadQueries().build()
    }

}