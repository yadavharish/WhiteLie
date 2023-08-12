package `in`.hyiitd.whitelie

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import `in`.hyiitd.whitelie.databinding.ActivityMainBinding
import `in`.hyiitd.whitelie.util.DBUtil
import `in`.hyiitd.whitelie.util.DeceiverModuleUtil.isXPModuleInstalled
import `in`.hyiitd.whitelie.util.PermsUtil
import `in`.hyiitd.whitelie.util.PkgsUtil
import `in`.hyiitd.whitelie.util.PreferencesUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        @Volatile var context: Context? = null
        @Volatile @JvmStatic var i = 0
        @JvmStatic var isXposedInstalled = true
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        initApp()
        context = applicationContext
//        DeceiverModule.ctx = applicationContext
        super.onCreate(savedInstanceState)
        i = 1

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        binding.navView.visibility = if(isXPModuleInstalled(this)) View.VISIBLE else View.GONE



        val cr: ContentResolver = applicationContext.contentResolver
        val cursor = cr.query(
            Uri.parse("content://in.hyiitd.deceiver.provider/deceivedApps"),
            null,
            null,
            null,
            null
        )!!
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                Log.i("tester", "onCreate Hooked, ${cursor.getString(cursor.getColumnIndex("name"))}")
                cursor.moveToNext()
            }
        }
    }

    private fun initApp() {
        DBUtil.initDB(applicationContext)
        PermsUtil.initSupportedPermissions(applicationContext)
        if(DBUtil.db.appDao().loadAll().isEmpty()) PkgsUtil.refreshAppAndPermDB(applicationContext)
        PkgsUtil.refreshInstalledPackages(applicationContext)
        PreferencesUtil.initSharedPreferences(applicationContext)
    }

    fun getAppContext(): Context? {
        return context
    }
}