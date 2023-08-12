package `in`.hyiitd.whitelie.ui.deceiver

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.DeceiverContentProvider
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.database.PermissionInfo
import `in`.hyiitd.whitelie.databinding.ListItemPermissionBinding
import `in`.hyiitd.whitelie.util.DBUtil.db
import `in`.hyiitd.whitelie.util.MiscUtil.dateTimeFormatterPattern
import `in`.hyiitd.whitelie.util.PermsUtil.getPermissionIcon
import java.time.LocalDateTime


private const val DISABLED_CARD_UI_CODE = 0
private const val ENABLED_CARD_UI_CODE = 1
private const val ACTIVE_CARD_UI_CODE = 2

class DPermissionAdapter(private val perms: List<PermissionInfo>): RecyclerView.Adapter<DPermissionAdapter.DPermissionViewHolder>() {
    class DPermissionViewHolder(val binding: ListItemPermissionBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = perms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DPermissionViewHolder(ListItemPermissionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DPermissionViewHolder, position: Int) {
        with(holder){
            with(perms[position]) {
                binding.tvPermissionName.text = permissionName
                updateUI(binding, itemView.context, if(!granted) DISABLED_CARD_UI_CODE else if(!deceived) ENABLED_CARD_UI_CODE else ACTIVE_CARD_UI_CODE, permissionName)
                itemView.setOnClickListener {
                    if(this.granted) {
                        deceived = !deceived
                        db.permDao().updateDeceivedStatus(packageName, permissionName, deceived, if(deceived) LocalDateTime.now().format(dateTimeFormatterPattern) else "")

                        it.context.grantUriPermission(packageName, Uri.parse(DeceiverContentProvider.deceivedAppsURL), Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        it.context.grantUriPermission(packageName, Uri.parse(DeceiverContentProvider.deceitStringsURL), Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        it.context.grantUriPermission(packageName, Uri.parse(DeceiverContentProvider.deceitSettingsURL), Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        if(deceived) {
                            Log.i("deceiverLogs", "$permissionName permission for $packageName added to deceiving list")
                            val values = ContentValues()
                            values.put(DeceiverContentProvider.name, "$packageName $permissionName")
                            it.context.contentResolver.insert(DeceiverContentProvider.deceivedAppsContentURI, values)
                        } else {
                            Log.i("deceiverLogs", "$permissionName permission for $packageName removed for deceiving list")
                            it.context.contentResolver.delete(DeceiverContentProvider.deceivedAppsContentURI, "name = '$packageName $permissionName'", null)
                        }

                        updateUI(binding, it.context, if(deceived) ACTIVE_CARD_UI_CODE else ENABLED_CARD_UI_CODE, permissionName)
                    }
                }
            }
        }
    }

    private fun updateUI(binding: ListItemPermissionBinding, ctx: Context, uiCode: Int, permName: String) {
        val contentColor = getColor(ctx, when(uiCode) {
            DISABLED_CARD_UI_CODE -> R.color.disabledCardContent
            ENABLED_CARD_UI_CODE -> R.color.theme1Green2
            ACTIVE_CARD_UI_CODE -> R.color.theme1Orange
            else -> R.color.disabledCardContent
        })
        val bgColor = getColor(ctx, when(uiCode) {
            DISABLED_CARD_UI_CODE -> R.color.nonSelectedCard
            ENABLED_CARD_UI_CODE -> R.color.theme1Blue
            ACTIVE_CARD_UI_CODE -> R.color.theme1Green2
            else -> R.color.nonSelectedCard
        })

        binding.cvPermission.background.setTint(bgColor)
        binding.tvPermissionName.setTextColor(contentColor)
        binding.ivPermissionIcon.setColorFilter(contentColor)
        binding.ivPermissionIcon.setImageDrawable(getPermissionIcon(permName, uiCode != ACTIVE_CARD_UI_CODE, ctx))
    }
}