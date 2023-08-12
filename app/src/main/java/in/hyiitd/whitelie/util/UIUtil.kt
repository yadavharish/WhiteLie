package `in`.hyiitd.whitelie.util

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentHomeBinding
import `in`.hyiitd.whitelie.ui.deceiver.DeceiverAppNavData
import `in`.hyiitd.whitelie.ui.home.HomeFragmentDirections
import `in`.hyiitd.whitelie.ui.home.LatestDeceivedAppPermissionsAdapter
import `in`.hyiitd.whitelie.util.DBUtil.db
import `in`.hyiitd.whitelie.util.LogsUtil.filterLogsSearchText
import `in`.hyiitd.whitelie.util.LogsUtil.refreshRegisteredLogs
import `in`.hyiitd.whitelie.util.LogsUtil.registeredLogs
import `in`.hyiitd.whitelie.util.MiscUtil.dateTimeFormatterPattern
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_ALL_APPS
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_DECEIVED_APPS
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_GRANTED_APPS
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_REQ_APPS
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_SYSTEM_APPS
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_USER_INSTALLED_APPS
import java.time.LocalDateTime
import java.util.*


internal object UIUtil {
    @JvmStatic var homeFragmentCreated = false

    @JvmStatic
    fun toGrayscale(image: Drawable?): Drawable? {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        image?.colorFilter = ColorMatrixColorFilter(matrix)
        return image
    }

    @JvmStatic
    fun gridSize(resources: Resources): Int = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4

    @JvmStatic
    fun greeting() = "good${when(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Morning"
        12 -> if(Calendar.getInstance().get(Calendar.MINUTE) == 0) "Noon" else "Afternoon"
        in 13..16 -> "Afternoon"
        else -> "Evening"
    }}"

    @JvmStatic
    fun getCountValuesForHomeFrag(): IntArray {
        val perms = DBUtil.db.permDao().loadAll()
        val apps = DBUtil.db.appDao().loadAll()
        val systemApps = apps.filter { it.systemApp }
        val userApps = apps.filter { !it.systemApp }
        var noOfDeceivedUserApps = 0
        var noOfDeceivedSystemApps = 0
        for(app in userApps) if(perms.any { it.packageName == app.packageName && it.deceived }) noOfDeceivedUserApps++
        for(app in systemApps) if(perms.any { it.packageName == app.packageName && it.deceived }) noOfDeceivedSystemApps++
        return intArrayOf(perms.size, perms.filter { it.granted }.size, perms.filter { it.deceived }.size, noOfDeceivedUserApps, userApps.size, noOfDeceivedSystemApps, systemApps.size)
    }

    @JvmStatic
    fun openDeceiverFragFromHomeFragCountCardView(appFilter: Int, permStatusFilter: Int, navView: BottomNavigationView) {
        PkgsUtil.currentAppsFilter = appFilter
        PkgsUtil.currentPermissionStatusFilter = permStatusFilter
        navView.selectedItemId = R.id.navigation_deceiver
    }

    @JvmStatic
    fun openDeceiverAppFragFromHomeFragLatestDeceivedCardView(view: View, packageName: String) {
        Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavigationHomeToNavigationDeceiverApp(DeceiverAppNavData(packageName)))
    }

    @JvmStatic
    fun createHomeFragModuleInstallationCardViewUI(installedProperly: Boolean, ctx: Context, binding: FragmentHomeBinding, activity: Activity) {
        val contentColor = getColor(ctx, if (installedProperly) R.color.oceanGreen else R.color.salmon)
        with(binding) {
            cvModuleInstallation.strokeColor = contentColor
            tvModuleInstallation.setTextColor(contentColor)
            tvModuleInstallation.text = ctx.getString(if (installedProperly) R.string.module_properly_installed else R.string.module_not_properly_installed)
            ivModuleInstallation.setColorFilter(contentColor)
            ivModuleInstallation.setImageDrawable(getDrawable(ctx, if (installedProperly) R.drawable.circle_tick_outline else R.drawable.critical_circle))
        }
    }

    @JvmStatic
    fun createHomeFragCountCardViewsUI(installedProperly: Boolean, binding: FragmentHomeBinding, activity: Activity) {
        with(binding) {
            //updating count values
            val values = if(installedProperly) getCountValuesForHomeFrag() else intArrayOf()
            val tvs = arrayOf(tvNoOfPermissionsCardRequestedCount, tvNoOfPermissionsCardGrantedCount, tvNoOfPermissionsCardDeceivedCount, tvNoOfUserAppsDeceivedCount, tvNoOfUserAppsInstalledCount, tvNoOfSystemAppsDeceivedCount, tvNoOfSystemAppsInstalledCount)
            if(!installedProperly) for(tv in tvs) tv.text = "-"                                         //if module is not installed, update count values with "-"
            else if(homeFragmentCreated) for(i in 0..6) tvs[i].text = values[i].toString()        //if animation is already shown, then directly update the value
            else {
                val animators = Array<ValueAnimator>(7) { ValueAnimator() }
                for(i in 0..6) {
                    animators[i].setObjectValues(0, values[i])
                    animators[i].addUpdateListener { animation -> tvs[i].text = animation.animatedValue.toString() }
                    animators[i].duration = 1000
                    animators[i].start()
                }
                homeFragmentCreated = true
            }

            //updating UI based upon moduleInstallStatus
            if(!installedProperly) {
                val headingTVs = arrayOf(tvNoOfPermissionsCardHeading, tvNoOfPermissionsCardRequestedHeading, tvNoOfPermissionsCardGrantedHeading, tvNoOfPermissionsCardDeceivedHeading, tvNoOfUserAppsCardHeading, tvNoOfUserAppsCardDeceivedHeading, tvNoOfUserAppsCardInstalledHeading, tvNoOfSystemAppsCardHeading, tvNoOfSystemAppsCardDeceivedHeading, tvNoOfSystemAppsCardInstalledHeading)
                val disabledPrimaryColor = getColor(activity, R.color.grey2)
                val disabledSecondaryColor = getColor(activity, R.color.grey)
                val disabledTertiaryColor = getColor(activity, R.color.grey3)
                cvNoOfPermissions.background.setTint(disabledSecondaryColor)
                cvNoOfUserApps.background.setTint(disabledSecondaryColor)
                cvNoOfSystemApps.strokeColor = disabledSecondaryColor
                for(tv in tvs) tv.setTextColor(disabledPrimaryColor)
                for(i in 0..6) headingTVs[i].setTextColor(disabledPrimaryColor)
                for(i in 7..9) headingTVs[i].setTextColor(disabledTertiaryColor)
            } else {
                //setting click listeners for tvs'
                val appFilters = intArrayOf(APPS_FILTER_SHOW_ALL_APPS, APPS_FILTER_SHOW_ALL_APPS, APPS_FILTER_SHOW_ALL_APPS, APPS_FILTER_SHOW_USER_INSTALLED_APPS, APPS_FILTER_SHOW_USER_INSTALLED_APPS, APPS_FILTER_SHOW_SYSTEM_APPS, APPS_FILTER_SHOW_SYSTEM_APPS)
                val permStatusFilters = intArrayOf(APPS_FILTER_SHOW_REQ_APPS, APPS_FILTER_SHOW_GRANTED_APPS, APPS_FILTER_SHOW_DECEIVED_APPS, APPS_FILTER_SHOW_DECEIVED_APPS, APPS_FILTER_SHOW_ALL_APPS, APPS_FILTER_SHOW_DECEIVED_APPS, APPS_FILTER_SHOW_ALL_APPS)
                val navView = activity.findViewById<BottomNavigationView>(R.id.navView)
                for(i in 0..6) tvs[i].setOnClickListener { openDeceiverFragFromHomeFragCountCardView(appFilters[i], permStatusFilters[i], navView) }
            }
        }
    }

    @JvmStatic
    fun createHomeFragLatestDeceivedAppCardViewUI(binding: FragmentHomeBinding, ctx: Context, moduleInstalled: Boolean) {
        with(binding) {
            val packagesDeceived = db.permDao().loadAllDeceivedPermissions()
            if(packagesDeceived.isEmpty() || !moduleInstalled) {
                cvLatestAppDeceivedApp.visibility = View.GONE
                cvLatestAppDeceivedPermission.visibility = View.GONE
                tvLatestAppDeceivedErrMsg.visibility = View.VISIBLE
                if(!moduleInstalled) {
                    val disabledSecondaryColor = getColor(ctx, R.color.grey)
                    cvLatestAppDeceivedCardHeading.setTextColor(disabledSecondaryColor)
                    cvLatestAppDeceivedCard.strokeColor = disabledSecondaryColor
                    tvLatestAppDeceivedErrMsg.setTextColor(disabledSecondaryColor)
                }
            }
            else {
                val latestDeceivedPermission = packagesDeceived.sortedWith(compareByDescending { LocalDateTime.parse(it.deceivedTime, dateTimeFormatterPattern) })[0].packageName
                val latestDeceivedAppInfo = db.appDao().loadByPackageName(latestDeceivedPermission)[0]
                PkgsUtil.fillPackageAdditionalInfo(latestDeceivedAppInfo, ctx)
                with(latestDeceivedAppInfo) {
                    tvLatestAppDeceivedAppName.text = appName
                    tvLatestAppDeceivedPackageName.text = packageName
                    tvLatestAppDeceivedNoOfPermsReq.text = noOfReqPerm.toString()
                    tvLatestAppDeceivedNoOfPermsGrant.text = noOfGrantPerm.toString()
                    tvLatestAppDeceivedNoOfPermsDeceived.text = noOfDeceivedPerm.toString()
                    ivLatestAppDeceivedAppIcon.setImageDrawable(icon)
                    ivLatestAppDeceivedCriticalIcon.visibility = if(systemApp) View.VISIBLE else View.GONE
                    with(rvLatestAppDeceivedPermissions) {
                        layoutManager = LinearLayoutManager(ctx)
                        adapter = LatestDeceivedAppPermissionsAdapter(permissions.filter { it.deceived })
                    }
                    cvLatestAppDeceivedApp.setOnClickListener { openDeceiverAppFragFromHomeFragLatestDeceivedCardView(it, packageName) }
                    cvLatestAppDeceivedPermission.setOnClickListener { openDeceiverAppFragFromHomeFragLatestDeceivedCardView(it, packageName) }
                }
            }
        }
    }

    @JvmStatic
    fun createHomeFragLatestLogsCardViewUI(binding: FragmentHomeBinding, ctx: Context, activity: Activity, moduleInstalled: Boolean) {
        refreshRegisteredLogs(ctx)
        with(binding) {
            if(registeredLogs.isEmpty() || !moduleInstalled) {
                cvLatestLog0.visibility = View.GONE
                cvLatestLog1.visibility = View.GONE
                tvLatestLogsErrMsg.visibility = View.VISIBLE
                if(!moduleInstalled) {
                    val disabledSecondaryColor = getColor(ctx, R.color.grey)
                    cvLatestLogsCardHeading.setTextColor(disabledSecondaryColor)
                    cvLatestLogsCard.strokeColor = disabledSecondaryColor
                    tvLatestLogsErrMsg.setTextColor(disabledSecondaryColor)
                    llNavViewBackground.visibility = View.GONE
                }
            }
            else {
                val sortedRegisteredLogs = registeredLogs.sortedWith(compareByDescending { LocalDateTime.parse(it.time, dateTimeFormatterPattern) })
                with(sortedRegisteredLogs[0]) {
                    tvLatestLog0AppName.text = appName
                    tvLatestLog0PackageName.text = packageName
                    tvLatestLog0Time.text = time
                    tvLatestLog0Msg.text = msg
                    ivLatestLog0AppIcon.setImageDrawable(icon)
                    cvLatestLog0.setOnClickListener {
                        //change search text
                        filterLogsSearchText = packageName
                        activity.findViewById<BottomNavigationView>(R.id.navView).selectedItemId = R.id.navigation_logs
                    }
                }
                if(registeredLogs.size == 1) { cvLatestLog1.visibility = View.GONE }
                else {
                    with(sortedRegisteredLogs[1]) {
                        tvLatestLog1AppName.text = appName
                        tvLatestLog1PackageName.text = packageName
                        tvLatestLog1Time.text = time
                        tvLatestLog1Msg.text = msg
                        ivLatestLog1AppIcon.setImageDrawable(icon)
                        cvLatestLog1.setOnClickListener {
                            //change search text
                            filterLogsSearchText = packageName
                            activity.findViewById<BottomNavigationView>(R.id.navView).selectedItemId = R.id.navigation_logs
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    fun createHomeFragDeceiverButtonsUI(binding: FragmentHomeBinding, ctx: Context, moduleInstalled: Boolean) {
        with(binding) {
            if(!moduleInstalled) {
                val disabledPrimaryColor = getColor(ctx, R.color.grey2)
                val disabledSecondaryColor = getColor(ctx, R.color.grey)
                cvViewDeceivedAppsButton.background.setTint(disabledSecondaryColor)
                tvViewDeceivedApps.setTextColor(disabledPrimaryColor)

                cvViewDeceitsExecutedButton.strokeColor = disabledSecondaryColor
                tvViewDeceitsExecuted.setTextColor(disabledSecondaryColor)
            }
        }
    }
}