package `in`.hyiitd.whitelie.util

import java.time.format.DateTimeFormatter

internal object MiscUtil {
    @JvmStatic val dateTimeFormatterPattern = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
}