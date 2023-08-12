package `in`.hyiitd.whitelie.util

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MenuOption(
    @SerializedName("name") @Expose var name: String,
    @SerializedName("value") @Expose var value: Int
)
