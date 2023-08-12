package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.content.SyncInfo
import android.hardware.Sensor
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule
import org.lsposed.hiddenapibypass.HiddenApiBypass
//import android.hardware.input.InputSensorInfo

object SensorDeceiver: XC_MethodHook() {
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("Range")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        if(param.result == null) return

        param.result = if(DeceiverModule.ctx != null) {
            val cursor = DeceiverModule.ctx!!.contentResolver.query(Uri.parse("content://in.hyiitd.deceiver.provider/deceitStrings"), null, null, null, null)
            val deceitStringsMap = mutableMapOf<String, String>()
            if (cursor != null) {
                if (cursor.moveToFirst())
                    while (!cursor.isAfterLast) {
                        val names = cursor.getString(cursor.getColumnIndex("name")).split(" ")
                        deceitStringsMap[names[0]] = names[1]
                        cursor.moveToNext()
                    }
                cursor.close()

                val instance: Any = HiddenApiBypass.newInstance(Class.forName("android.hardware.input.InputSensorInfo"), deceitStringsMap["sensorName"] ?: "deceiverSensor", deceitStringsMap["sensorVendor"] ?: "deceiver", deceitStringsMap["sensorVersion"]?.toInt() ?: 0, 0, deceitStringsMap["sensorTypeInt"]?.toInt() ?: 0, deceitStringsMap["sensorMaxRange"]?.toFloat() ?: 0.0F, deceitStringsMap["sensorResolution"]?.toFloat() ?: 0.0F, deceitStringsMap["sensorPower"]?.toFloat() ?: 0.0F, deceitStringsMap["sensorMinDelay"]?.toInt() ?: 0, 0, 0, deceitStringsMap["sensorTypeString"] ?: "deceiverSensorType", "",  deceitStringsMap["sensorMaxDelay"]?.toInt() ?: 0, 0,  deceitStringsMap["sensorId"]?.toInt() ?: 0)
                val instance2: Any = HiddenApiBypass.newInstance(Class.forName("android.hardware.Sensor"), instance)
                when(param.method.name) {
                    "getDefaultSensor" -> instance2 as Sensor
                    "getCurrentSyncs" -> mutableListOf(instance as SyncInfo)
                    else -> param.result
                }
            }
            else param.result
        }
        else param.result
    }
}