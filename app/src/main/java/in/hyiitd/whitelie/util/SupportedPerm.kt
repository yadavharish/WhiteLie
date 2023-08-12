package `in`.hyiitd.whitelie.util

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SupportedPerm(
    @SerializedName("permission") @Expose var permission: String,
    @SerializedName("name") @Expose var name: String
)
