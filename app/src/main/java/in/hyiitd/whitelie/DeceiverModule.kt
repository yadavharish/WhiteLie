package `in`.hyiitd.whitelie

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.*
import android.content.Context.*
import android.database.Cursor
import android.location.Location
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge.*
import de.robv.android.xposed.XposedHelpers.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import `in`.hyiitd.whitelie.MainActivity.Companion.isXposedInstalled
import `in`.hyiitd.whitelie.deceits.*
import `in`.hyiitd.whitelie.util.MiscUtil
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.Method
import java.time.LocalDateTime


class DeceiverModule : IXposedHookLoadPackage {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var ctx: Context? = null

        @JvmStatic
        var handleLoadPackageFlag = true

        @JvmStatic
        var packageName: String = ""

        @JvmStatic
        fun addLog(msg: String) {
            Log.i("deceiverLogs", "$msg $packageName")
            val values = ContentValues()
//            values.put(DeceiverContentProvider.name, "${LocalDateTime.now().format(MiscUtil.dateTimeFormatterPattern)}#*#*#${packageName}#*#*#$msg")
            values.put(DeceiverContentProvider.name, "${LocalDateTime.now().format(MiscUtil.dateTimeFormatterPattern)}#*#*#com.facebook.katana#*#*#$msg")
            ctx?.contentResolver?.insert(DeceiverContentProvider.deceiverLogsContentURI, values)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        packageName = lpparam.packageName
        Log.i("xposed", "Loaded Package: $packageName")
        log("Loaded app: ${MainActivity.i} " + lpparam.packageName);
        log("Module loaded for package $packageName")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L");
        }

        HiddenApiBypass.addHiddenApiExemptions("Landroid/android/hardware/input/InputSensorInfo;")

        // returns the instance of argumented class_name Class
        fun getClass(clsName: String): Class<*> = Class.forName(clsName, false, lpparam.classLoader)

        fun getDeceivedApps(): Cursor? {
            val uri = Uri.parse("content://in.hyiitd.deceiver.provider/deceivedApps")
            val cr: ContentResolver = (ctx ?: return null).contentResolver
            val cpc: ContentProviderClient = cr.acquireContentProviderClient(uri) ?: return null
            return cpc.query(uri, null, null, null, null)
        }

        fun isPackageDeceived(): Boolean {
            val cursor = getDeceivedApps() ?: return false
            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    if(lpparam.packageName == cursor.getString(cursor.getColumnIndex("name")).split(" ")[0]) return true
                    cursor.moveToNext()
                }
            return false
        }

        fun isPermissionDeceived(permissionName: String): Boolean {
            val cursor = getDeceivedApps() ?: return false
            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val names = cursor.getString(cursor.getColumnIndex("name")).split(" ")
                    if(lpparam.packageName == names[0] && permissionName == names[1]) return true
                    cursor.moveToNext()
                }
            return false
        }

//        val activity = XposedHelpers.findClass("android.app.Activity", lpparam.classLoader)
        hookAllMethods(getClass("android.app.Activity"), "onCreate", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                if(lpparam.packageName == "in.hyiitd.deceiver") {
                    isXposedInstalled = true
                    log("isXposedInstalledChanged")
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    val forName = Class::class.java.getDeclaredMethod("forName", String::class.java)
                    val getDeclaredMethod = Class::class.java.getDeclaredMethod("getDeclaredMethod", String::class.java, arrayOf<Class<*>>()::class.java)

                    val vmRuntimeClass = forName.invoke(null, "dalvik.system.VMRuntime") as Class<*>
                    val getRuntime = getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null) as Method
                    val setHiddenApiExemptions = getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", arrayOf(arrayOf<String>()::class.java)) as Method

                    val vmRuntime = getRuntime.invoke(null)

                    setHiddenApiExemptions.invoke(vmRuntime, arrayOf("L"))
                }
            }
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                ctx = param.thisObject as Context

                if(handleLoadPackageFlag) {
                    DeceiverModule().handleLoadPackage(lpparam)
                    handleLoadPackageFlag = false
                }
            }
        })

        if(isPackageDeceived()) {
            if(isPermissionDeceived("account")) {
                addLog("Hooked Account permission data")
                hookAllMethods(getClass(Account.CREATOR.javaClass.name), "createFromParcel", DeceivedAccountParcel)
            }

            if(isPermissionDeceived("activityRecognition")) {
                addLog("Hooked Activity Recognition permission data")
                hookAllMethods(getClass("com.google.android.gms.location.ActivityRecognitionResult"), "extractResult", ActivityRecognitionDeceiver)
            }

            if(isPermissionDeceived("analytics")) {
                addLog("Hooked Analytics permission data")
                hookAllMethods(getClass("com.google.firebase.analytics.FirebaseAnalytics"),"getInstance", AnalyticsDeceiver)
                hookAllMethods(getClass("com.google.firebase.analytics.FirebaseAnalytics"), "setAnalyticsCollectionEnabled", AnalyticsDeceiver)
//                hookAllMethods(getClass("com.facebook.appevents.AppEventsLogger"), "activateApp", Blocker)                                CNF
//                hookAllMethods(getClass("com.facebook.appevents.AppEventsLogger"), "deactivateApp", Blocker)
//                hookAllMethods(getClass("com.facebook.appevents.AppEventsLogger"), "logEvent", Blocker)
//                hookAllMethods(getClass("com.facebook.appevents.AppEventsLogger"), "logPushNotificationOpen", Blocker)
//                hookAllMethods(getClass("com.mixpanel.android.mpmetrics.MixpanelAPI"), "track", Blocker)
//                hookAllMethods(getClass("com.mixpanel.android.mpmetrics.MixpanelAPI"), "trackMap", Blocker)
//                hookAllMethods(getClass("com.google.android.gms.analytics.GoogleAnalytics"), "getInstance", AnalyticsDeceiver)
//                hookAllMethods(getClass("com.google.android.gms.analytics.GoogleAnalytics"), "setDryRun", AnalyticsDeceiver)
//                hookAllMethods(getClass("com.segment.analytics.Analytics"), "setSingletonInstance", AnalyticsDeceiver)
//                hookAllMethods(getClass("com.segment.analytics.Analytics"), "optOut", AnalyticsDeceiver)
            }

            if(isPermissionDeceived("applications")) {
                addLog("Hooked Applications permission data")
                hookAllMethods(getClass("android.app.ActivityManager"), "getRecentTasks", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.app.ActivityManager"), "getRunningAppProcesses", DeceivedFilter)
                hookAllMethods(getClass("android.app.ActivityManager"), "getRunningServices", DeceivedFilter)
                hookAllMethods(getClass("android.app.ActivityManager"), "getRunningTasks", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.appwidget.AppWidgetManager"), "getInstalledProviders", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.appwidget.AppWidgetManager"), "getInstalledProvidersForPackage", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.appwidget.AppWidgetManager"), "getInstalledProvidersForProfile", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.content.Intent"), "CREATOR:createFromParcel", DeceivedIntentParcel)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "getInstalledApplications", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "getInstalledPackages", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "getPackagesHoldingPermissions", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "queryIntentActivities", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "getPreferredPackages", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "queryIntentActivityOptions", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "queryIntentContentProviders", DeceivedFilter)
//                hookAllMethods(getClass("android.content.pm.PackageManager"), "queryIntentServices", DeceivedFilter)
            }

            if(isPermissionDeceived("audio")) {
                addLog("Hooked Audio permission data")
                hookAllMethods(getClass("android.media.AudioRecord"), "startRecording", Blocker)
                hookAllMethods(getClass("android.media.AudioRecord"), "stop", Blocker)
                hookAllMethods(getClass("android.media.AudioRecord"), "getActiveRecordingConfigurations", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.media.AudioRecord"), "getDevices", DeceivedDevices)
                hookAllMethods(getClass("android.media.AudioRecord"), "registerAudioDeviceCallback", Blocker)
                hookAllMethods(getClass("android.media.AudioRecord"), "registerAudioDeviceCallback", Blocker)
                hookAllMethods(getClass("android.media.AudioRecord"), "registerAudioRecordingCallback", Blocker)
                hookAllMethods(getClass("android.media.MediaRecorder"), "setAudioSource", Blocker)
                hookAllMethods(getClass("android.media.MediaRecorder"), "start", Blocker)
                hookAllMethods(getClass("android.media.MediaRecorder"), "stop", Blocker)
            }

            if(isPermissionDeceived("calendar")) {
                addLog("Hooked Calendar permission data")
                hookAllMethods(getClass("android.content.ContentResolver"), "query", DeceivedContentResolverQuery)
            }

            if(isPermissionDeceived("callLog")) {
                addLog("Hooked Call Log permission data")
                hookAllMethods(getClass("android.content.ContentResolver"), "query", DeceivedContentResolverQuery)
            }

            if(isPermissionDeceived("camera")) {
                addLog("Hooked Camera permission data")
                hookAllMethods(getClass("android.hardware.Camera"), "getCameraInfo", DeceivedCameraOpen)
                hookAllMethods(getClass("android.hardware.Camera"), "getNumberOfCameras", IntegerReturnTypeDeceiver)
                hookAllMethods(getClass("android.hardware.Camera"), "open", DeceivedCameraOpen)
                hookAllMethods(getClass("android.hardware.Camera"), "openLegacy", DeceivedCameraOpen)
                hookAllMethods(getClass("android.hardware.camera2.CameraManager"), "openCamera", DeceivedCamera2Open)
                hookAllMethods(getClass("android.hardware.camera2.CameraManager"), "openCamera", DeceivedCamera2Open)
            }

            if(isPermissionDeceived("clipboard")) {
                addLog("Hooked methods responsible for Clipboard data for")
                hookAllMethods(getClass(ClipData.CREATOR.javaClass.name), "createFromParcel", DeceivedClipdataParcel)
            }

            if(isPermissionDeceived("contacts")) {
                addLog("Hooked Contacts permission data")
                hookAllMethods(getClass("android.content.ContentResolver"), "query", DeceivedContentResolverQuery)
            }

            if(isPermissionDeceived("location")) {
                addLog("Hooked methods responsible for Location data for")
                hookAllMethods(getClass(Location.CREATOR.javaClass.name), "createFromParcel", LocationDeceiver)
                hookAllMethods(getClass("android.location.LocationManager"), "addNmeaListener", BooleanReturnTypeDeceiver)
                hookAllMethods(getClass("android.location.LocationManager"), "addProximityAlert", Blocker)
                hookAllMethods(getClass("android.os.BaseBundle"), "get", LocationDeceiver)
//                hookAllMethods(getClass("com.google.android.gms.location.Geofence\$Builder"), "setCircularRegion", LocationDeceiver)
//                hookAllMethods(getClass("com.google.android.gms.location.places.PlaceLikelihoodBuffer"), "get", NullifyResult)                            CNF
//                hookAllMethods(getClass("com.google.android.gms.location.places.PlaceLikelihoodBuffer"), "getCount", IntegerReturnTypeDeceiver)
            }

            if(isPermissionDeceived("message")) {
                addLog("Hooked Messages permission data")
                hookAllMethods(getClass("android.content.Intent"), "CREATOR:createFromParcel", DeceivedIntentParcel)
                hookAllMethods(getClass("android.content.ContentResolver"), "query", DeceivedContentResolverQuery)
                hookAllMethods(getClass("android.telephony.SmsManager"), "getAllMessagesFromIcc", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.telephony.SmsManager"), "sendDataMessage", Blocker)
                hookAllMethods(getClass("android.telephony.SmsManager"), "sendMultimediaMessage", Blocker)
                hookAllMethods(getClass("android.telephony.SmsManager"), "sendMultipartTextMessage", Blocker)
                hookAllMethods(getClass("android.telephony.SmsManager"), "sendTextMessage", Blocker)
            }

            if(isPermissionDeceived("network")) {
                addLog("Hooked Network permission data")
                hookAllMethods(getClass("android.net.NetworkInfo"), "getExtraInfo", NullifyResult)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getAllCellInfo", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getCellLocation", NullifyResult)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getNeighboringCellInfo", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "listen", TelephonyManagerListenerDeceiver)
                hookAllMethods(getClass("android.net.wifi.WifiManager"), "getConfiguredNetworks", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.net.wifi.WifiManager"), "getScanResults", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.net.wifi.WifiManager"), "getDhcpInfo", StringReturnTypeDeceiver2)
                hookAllMethods(getClass("android.net.wifi.WifiManager"), "getConnectionInfo", StringReturnTypeDeceiver2)
                hookAllMethods(getClass("android.net.wifi.WifiInfo"), "getBSSID", TrackingDeceiver)
                hookAllMethods(getClass("android.net.wifi.WifiInfo"), "getSSID", TrackingDeceiver)
            }

            if(isPermissionDeceived("notifications")) {
                addLog("Hooked Notifications permission data")
                hookAllMethods(getClass("android.service.notification.StatusBarNotification"), "getNotification", NotificationDeceiver)
            }

            if(isPermissionDeceived("sensor")) {
                addLog("Hooked Sensors permission data")
                hookAllMethods(getClass("android.hardware.SensorManager"), "getDefaultSensor", NullifyResult)
                hookAllMethods(getClass("android.hardware.SensorManager"), "getDynamicSensorList", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.hardware.SensorManager"), "getSensorList", DeceivedAsEmptyList)
                hookAllMethods(getClass("android.hardware.SensorManager"), "getSensors", IntegerReturnTypeDeceiver)
            }

            if(isPermissionDeceived("sync")) {
                addLog("Hooked Sync permission data")
                hookAllMethods(getClass("android.content.ContentResolver"), "getCurrentSync", SyncDeceiver)
                hookAllMethods(getClass("android.content.ContentResolver"), "getCurrentSyncs", SyncDeceiver)
            }

            if(isPermissionDeceived("telephony")) {
                addLog("Hooked Telephony permission data")
                hookAllMethods(getClass("android.telephony.SubscriptionInfo"), "getIccId", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.SubscriptionInfo"), "getMcc", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.SubscriptionInfo"), "getMnc", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.SubscriptionInfo"), "getNumber", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getDeviceId", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getImei", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getLine1Number", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getMeid", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getSimSerialNumber", TelephonyDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getSubscriberId", NullifyResult)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getVoiceMailAlphaTag", StringReturnTypeDeceiver2)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getVoiceMailNumber", StringReturnTypeDeceiver2)
            }

            if(isPermissionDeceived("tracking")) {
                addLog("Hooked Tracking permission data")
                hookAllMethods(getClass("android.os.Build"), "getRadioVersion", TrackingDeceiver)
                hookAllMethods(getClass("android.os.Build"), "getSerial", TrackingDeceiver)
                hookAllMethods(getClass("android.os.Build"), "getString", TrackingDeceiver)
                hookAllMethods(getClass("android.os.SystemProperties"), "get", TrackingDeceiver)
                hookAllMethods(getClass("android.provider.Settings\$Secure"), "getString", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.SubscriptionInfo"), "getCarrierName", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.SubscriptionInfo"), "getCountryIso", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getNetworkCountryIso", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getNetworkOperator", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getNetworkOperatorName", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getSimCountryIso", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getSimOperator", TrackingDeceiver)
                hookAllMethods(getClass("android.telephony.TelephonyManager"), "getSimOperatorName", TrackingDeceiver)
                hookAllMethods(getClass("com.google.android.gms.ads.identifier.AdvertisingIdClient\$Info"), "getId", TrackingDeceiver)
            }

            if(isPermissionDeceived("video")) {
                addLog("Hooked Video permission data")
                hookAllMethods(getClass("android.media.MediaRecorder"), "setVideoSource", Blocker)
                hookAllMethods(getClass("android.media.MediaRecorder"), "start", Blocker)
                hookAllMethods(getClass("android.media.MediaRecorder"), "stop", Blocker)
            }
        }
    }
}