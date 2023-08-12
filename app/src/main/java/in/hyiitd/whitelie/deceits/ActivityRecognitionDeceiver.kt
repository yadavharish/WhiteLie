package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.net.Uri
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.ActivityRecognitionResult
import de.robv.android.xposed.XC_MethodHook
import `in`.hyiitd.whitelie.DeceiverModule

object ActivityRecognitionDeceiver: XC_MethodHook() {
    @SuppressLint("Range", "VisibleForTests")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        val result = param.result as ActivityRecognitionResult? ?: return

        if(DeceiverModule.ctx != null) {
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

                param.result = ActivityRecognitionResult(DetectedActivity(getActivityRecognisedInt(deceitStringsMap["activityRecognized"] ?: "unknown"), 100), result.time, result.elapsedRealtimeMillis)
            }
            else param.result = ActivityRecognitionResult(DetectedActivity(getActivityRecognisedInt("unknown"), 100), result.time, result.elapsedRealtimeMillis)
        }
    }

    private fun getActivityRecognisedInt(actRecognised: String): Int {
        return when(actRecognised) {
            "inVehicle" -> 0
            "onBicycle" -> 1
            "onFoot" -> 2
            "still" -> 3
            "tilting" -> 5
            "walking" -> 7
            "running" -> 8
            else -> 4   //unknown
        }
    }
}