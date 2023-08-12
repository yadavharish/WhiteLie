package `in`.hyiitd.whitelie.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.hyiitd.whitelie.database.LogInfo
import `in`.hyiitd.whitelie.util.DBUtil.db
import `in`.hyiitd.whitelie.util.MiscUtil.dateTimeFormatterPattern
import java.time.LocalDateTime

internal object LogsUtil {
    @JvmStatic var registeredLogs: List<LogInfo> = emptyList()
    @JvmStatic var filteredLogs: List<LogInfo> = emptyList()

    @JvmStatic val LOGS_FILTER_SHOW_ALL_APPS = 0
    @JvmStatic val LOGS_FILTER_SHOW_SYSTEM_APPS = 1
    @JvmStatic val LOGS_FILTER_SHOW_USER_INSTALLED_APPS = 2
    @JvmStatic var currentLogsFilter = LOGS_FILTER_SHOW_ALL_APPS

    @JvmStatic val LOGS_SORT_BY_APP_NAME = 0
    @JvmStatic val LOGS_SORT_BY_TIME = 1
    @JvmStatic val LOGS_SORT_IN_ASCENDING = 0
    @JvmStatic val LOGS_SORT_IN_DESCENDING = 1
    @JvmStatic var currentLogsSortOption = LOGS_SORT_BY_TIME
    @JvmStatic var currentLogsSortOrder = LOGS_SORT_IN_DESCENDING

    @JvmStatic var filterLogsMenu = emptyList<MenuOption>()
    @JvmStatic var filterLogsSearchText = ""
    @JvmStatic var sortLogsMenu = emptyList<MenuOption>()
    @JvmStatic var sortOrderLogsMenu = emptyList<MenuOption>()

    @JvmStatic
    fun refreshRegisteredLogs(ctx: Context) {
        registeredLogs = db.logsDao().loadAll()
        fillLogsAdditionalInfo(ctx)
        filterRegisteredLogs()
    }

    @JvmStatic
    fun fillLogsAdditionalInfo(ctx: Context) {
        for(log in registeredLogs) fillLogAdditionalInfo(log, ctx)
    }

    @JvmStatic
    fun fillLogAdditionalInfo(log: LogInfo, ctx: Context) {
        with(log) {
            with(ctx.packageManager) {
                icon = getApplicationIcon(packageName)
                systemApp = (getApplicationInfo(packageName, PackageManager.GET_PERMISSIONS).flags and ApplicationInfo.FLAG_SYSTEM) != 0
            }
        }
    }

    @JvmStatic
    fun filterRegisteredLogs() {
        filteredLogs = when(currentLogsFilter) {
            LOGS_FILTER_SHOW_ALL_APPS -> registeredLogs
            LOGS_FILTER_SHOW_SYSTEM_APPS -> registeredLogs.filter { it.systemApp }
            LOGS_FILTER_SHOW_USER_INSTALLED_APPS -> registeredLogs.filter { !it.systemApp }
            else -> emptyList()
        }

        if(filterLogsSearchText != "") {
            filteredLogs = filteredLogs.filter { it.appName.contains(filterLogsSearchText) || it.packageName.contains(filterLogsSearchText) || it.time.contains(filterLogsSearchText) || it.msg.contains(filterLogsSearchText) }
        }
        sortFilteredLogs()
    }

    @JvmStatic
    fun sortFilteredLogs() {
        filteredLogs = with(filteredLogs) {
            when(currentLogsSortOption) {
                LOGS_SORT_BY_APP_NAME -> sortedWith(compareByDescending { it.appName })
                LOGS_SORT_BY_TIME -> sortedWith(compareByDescending<LogInfo> { LocalDateTime.parse(it.time, dateTimeFormatterPattern) }.thenBy { it.appName })
                else -> emptyList()
            }
        }

        filteredLogs = when(currentLogsSortOrder) {
            LOGS_SORT_IN_ASCENDING -> filteredLogs.reversed()
            LOGS_SORT_IN_DESCENDING -> filteredLogs
            else -> emptyList()
        }
    }

    @JvmStatic
    fun initMenus(ctx: Context) {
        val listMenuOptionType = object: TypeToken<List<MenuOption>>() {}.type
        filterLogsMenu = Gson().fromJson(ctx.assets.open("filter_logs_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
        sortLogsMenu = Gson().fromJson(ctx.assets.open("sort_logs_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
        sortOrderLogsMenu = Gson().fromJson(ctx.assets.open("sort_order_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
    }
}