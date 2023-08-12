package `in`.hyiitd.whitelie.deceits

import android.hardware.camera2.CameraAccessException
import de.robv.android.xposed.XC_MethodHook

object DeceivedCamera2Open: XC_MethodHook() {
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        param.result = CameraAccessException(CameraAccessException.CAMERA_DISABLED, "deceived")
    }
}

//CAMERA_DISABLED = 1
//CAMERA_DISCONNECTED = 2
//CAMERA_ERROR = 3
//CAMERA_IN_USE = 4
//MAX_CAMERAS_IN_USE = 5