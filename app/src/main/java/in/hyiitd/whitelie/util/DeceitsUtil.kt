package `in`.hyiitd.whitelie.util

object DeceitsUtil {
    @JvmStatic val DECEIT_MODE_NONE = -1
    @JvmStatic val DECEIT_MODE_NORMAL = 0
    @JvmStatic val DECEIT_MODE_DECENT = 1
    @JvmStatic val DECEIT_MODE_DESPERATE = 2

    @JvmStatic var currentModeOption = -1
    @JvmStatic var currentPermissionSelected = ""

    @JvmStatic var supportedModes = listOf<String>("normal", "decent", "desperate")

    @JvmStatic fun updateDeceitValuesByMode(mode: Int) {

    }
}