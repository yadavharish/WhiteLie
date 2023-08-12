package `in`.hyiitd.whitelie.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.hyiitd.whitelie.database.AppInfo
import `in`.hyiitd.whitelie.database.PermissionInfo

internal object PkgsUtil {
    @JvmStatic var installedPackages: List<AppInfo> = emptyList()
    @JvmStatic var filteredPackages: List<AppInfo> = emptyList()

    @JvmStatic val APPS_FILTER_SHOW_ALL_APPS = 0
    @JvmStatic val APPS_FILTER_SHOW_SYSTEM_APPS = 1
    @JvmStatic val APPS_FILTER_SHOW_USER_INSTALLED_APPS = 2
    @JvmStatic val APPS_FILTER_SHOW_REQ_APPS = 1
    @JvmStatic val APPS_FILTER_SHOW_GRANTED_APPS = 2
    @JvmStatic val APPS_FILTER_SHOW_DECEIVED_APPS = 3
    @JvmStatic var currentAppsFilter = APPS_FILTER_SHOW_USER_INSTALLED_APPS
    @JvmStatic var currentPermissionStatusFilter = APPS_FILTER_SHOW_REQ_APPS

    @JvmStatic val APPS_SORT_BY_APP_NAME = 0
    @JvmStatic val APPS_SORT_BY_REQ = 1
    @JvmStatic val APPS_SORT_BY_GRANTED = 2
    @JvmStatic val APPS_SORT_BY_DECEIVED = 3
    @JvmStatic val APPS_SORT_IN_ASCENDING = 0
    @JvmStatic val APPS_SORT_IN_DESCENDING = 1
    @JvmStatic var currentAppsSortOption = APPS_SORT_BY_DECEIVED
    @JvmStatic var currentAppsSortOrder = APPS_SORT_IN_DESCENDING

    @JvmStatic var filterAppMenu = emptyList<MenuOption>()
    @JvmStatic var filterPermissionStatusMenu = emptyList<MenuOption>()
    @JvmStatic var filterPermissionsSelected = emptyList<String>()
    @JvmStatic var filterSearchText = ""
    @JvmStatic var sortAppMenu = emptyList<MenuOption>()
    @JvmStatic var sortOrderAppMenu = emptyList<MenuOption>()

    @JvmStatic
    fun refreshInstalledPackages(ctx: Context) {
        installedPackages = DBUtil.db.appDao().loadAll()
        fillPackagesAdditionalInfo(ctx)
        filterInstalledPackages()
    }

    @JvmStatic
    fun fillPackagesAdditionalInfo(ctx: Context) {
        for(pkg in installedPackages) fillPackageAdditionalInfo(pkg, ctx)
    }

    @JvmStatic
    fun fillPackageAdditionalInfo(pkg: AppInfo, ctx: Context) {
        with(pkg) {
            icon = ctx.packageManager.getApplicationIcon(packageName)
            permissions = DBUtil.db.permDao().loadAllByPackageName(packageName)
            noOfReqPerm = permissions.size
            noOfGrantPerm = permissions.filter{ it.granted }.size
            noOfDeceivedPerm = permissions.filter{ it.deceived }.size
        }
    }

    @JvmStatic
    fun filterInstalledPackages() {
        filteredPackages = when(currentAppsFilter) {
            APPS_FILTER_SHOW_ALL_APPS -> installedPackages
            APPS_FILTER_SHOW_SYSTEM_APPS -> installedPackages.filter { it.systemApp }
            APPS_FILTER_SHOW_USER_INSTALLED_APPS -> installedPackages.filter { !it.systemApp }
            else -> emptyList()
        }

        filteredPackages = when(currentPermissionStatusFilter) {
            APPS_FILTER_SHOW_ALL_APPS -> filteredPackages
            APPS_FILTER_SHOW_REQ_APPS -> filteredPackages.filter { it.noOfReqPerm > 0 }
            APPS_FILTER_SHOW_GRANTED_APPS -> filteredPackages.filter { it.noOfGrantPerm > 0 }
            APPS_FILTER_SHOW_DECEIVED_APPS -> filteredPackages.filter { it.noOfDeceivedPerm > 0 }
            else -> emptyList()
        }

        if(filterPermissionsSelected.isNotEmpty())
            filteredPackages = filteredPackages.filter {
                var flag = false
                for(perm in it.permissions) if(perm.permissionName in filterPermissionsSelected) {
                    flag = true
                    break
                }
                flag
            }

        if(filterSearchText != "") {
            filteredPackages = filteredPackages.filter { it.appName.contains(filterSearchText) || it.packageName.contains(filterSearchText) }
        }
        sortFilteredPackages()
    }

    @JvmStatic
    fun refreshAppAndPermDB(ctx: Context) {
        val pm = ctx.packageManager
        for(pkg in pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)) {
            for (perm in PermsUtil.supportedPermissions)
                if (perm.permission == "" || (pkg.requestedPermissions != null && pkg.requestedPermissions.contains(perm.permission)))
                    DBUtil.db.permDao().insert(PermissionInfo(pkg.packageName, perm.name, (perm.permission == "") || (pm.checkPermission(perm.permission, pkg.packageName) == PackageManager.PERMISSION_GRANTED), false, ""))
            DBUtil.db.appDao().insert(AppInfo(pkg.applicationInfo.loadLabel(pm).toString(), pkg.packageName, (pkg.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0))
        }
        refreshInstalledPackages(ctx)
    }

    @JvmStatic
    fun sortFilteredPackages() {
        filteredPackages = with(filteredPackages) {
            when(currentAppsSortOption) {
                APPS_SORT_BY_APP_NAME -> sortedWith(compareByDescending { it.appName })
                APPS_SORT_BY_REQ -> sortedWith(compareByDescending<AppInfo> { it.noOfReqPerm }.thenByDescending { it.noOfDeceivedPerm }.thenBy { it.appName })
                APPS_SORT_BY_GRANTED -> sortedWith(compareByDescending<AppInfo> { it.noOfGrantPerm }.thenByDescending { it.noOfDeceivedPerm }.thenByDescending { it.noOfReqPerm }.thenBy { it.appName })
                APPS_SORT_BY_DECEIVED -> sortedWith(compareByDescending <AppInfo> { it.noOfDeceivedPerm }.thenByDescending { it.noOfGrantPerm }.thenByDescending { it.noOfReqPerm }.thenBy { it.appName })
                else -> emptyList()
            }
        }

        filteredPackages = when(currentAppsSortOrder) {
            APPS_SORT_IN_ASCENDING -> filteredPackages.reversed()
            APPS_SORT_IN_DESCENDING -> filteredPackages
            else -> emptyList()
        }
    }

    @JvmStatic
    fun initMenus(ctx: Context) {
        val listMenuOptionType = object: TypeToken<List<MenuOption>>() {}.type
        filterAppMenu = Gson().fromJson(ctx.assets.open("filter_app_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
        filterPermissionStatusMenu = Gson().fromJson(ctx.assets.open("filter_permission_status_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
        sortAppMenu = Gson().fromJson(ctx.assets.open("sort_app_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
        sortOrderAppMenu = Gson().fromJson(ctx.assets.open("sort_order_menu.json").bufferedReader().use { it.readText() }, listMenuOptionType)
    }
}