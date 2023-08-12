package `in`.hyiitd.whitelie.ui.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceitsBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.DeceitsUtil.currentPermissionSelected
import `in`.hyiitd.whitelie.util.PermsUtil.supportedPermissions

class DeceitsPermissionsAdapter(val parentBinding: FragmentDeceitsBinding): RecyclerView.Adapter<DeceitsPermissionsAdapter.DeceitsPermissionsViewHolder>() {
    class DeceitsPermissionsViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = supportedPermissions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeceitsPermissionsViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DeceitsPermissionsViewHolder, position: Int) {
        with(holder){
            with(supportedPermissions[position]) {
                with(binding) {
                    tvName.text = name
                    val ctx = itemView.context
                    val isSelected = name == currentPermissionSelected
                    itemView.backgroundTintList = ctx.resources.getColorStateList(if(isSelected) R.color.theme1Green2 else R.color.theme1White, ctx.theme)
                    tvName.setTextColor(ContextCompat.getColor(ctx, if(isSelected) R.color.theme1Orange else R.color.theme1Green2))

                    itemView.setOnClickListener {
                        currentPermissionSelected = if(name == currentPermissionSelected) "" else name
                        changeDeceitOptions(currentPermissionSelected, parentBinding)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun changeDeceitOptions(name: String, parentBinding: FragmentDeceitsBinding) {
        with(parentBinding) {
            if(name == "") {
                llDeceitAccount.visibility = android.view.View.VISIBLE
                llDeceitActivityRecognition.visibility = android.view.View.VISIBLE
                llDeceitApplications.visibility = android.view.View.VISIBLE
                llDeceitAudio.visibility = android.view.View.VISIBLE
                llDeceitCalendar.visibility = android.view.View.VISIBLE
                llDeceitCallLog.visibility = android.view.View.VISIBLE
                llDeceitCamera.visibility = android.view.View.VISIBLE
                llDeceitClipboard.visibility = android.view.View.VISIBLE
                llDeceitContacts.visibility = android.view.View.VISIBLE
                llDeceitLocation.visibility = android.view.View.VISIBLE
                llDeceitMessage.visibility = android.view.View.VISIBLE
                llDeceitNetwork.visibility = android.view.View.VISIBLE
                llDeceitNotifications.visibility = android.view.View.VISIBLE
                llDeceitSensor.visibility = android.view.View.VISIBLE
                llDeceitSync.visibility = android.view.View.VISIBLE
                llDeceitTelephony.visibility = android.view.View.VISIBLE
                llDeceitTracking.visibility = android.view.View.VISIBLE
                llDeceitVideo.visibility = android.view.View.VISIBLE
            } else {
                llDeceitAccount.visibility = android.view.View.GONE
                llDeceitActivityRecognition.visibility = android.view.View.GONE
                llDeceitApplications.visibility = android.view.View.GONE
                llDeceitAudio.visibility = android.view.View.GONE
                llDeceitCalendar.visibility = android.view.View.GONE
                llDeceitCallLog.visibility = android.view.View.GONE
                llDeceitCamera.visibility = android.view.View.GONE
                llDeceitClipboard.visibility = android.view.View.GONE
                llDeceitContacts.visibility = android.view.View.GONE
                llDeceitLocation.visibility = android.view.View.GONE
                llDeceitMessage.visibility = android.view.View.GONE
                llDeceitNetwork.visibility = android.view.View.GONE
                llDeceitNotifications.visibility = android.view.View.GONE
                llDeceitSensor.visibility = android.view.View.GONE
                llDeceitSync.visibility = android.view.View.GONE
                llDeceitTelephony.visibility = android.view.View.GONE
                llDeceitTracking.visibility = android.view.View.GONE
                llDeceitVideo.visibility = android.view.View.GONE
            }
            when(name) {
                "account" -> llDeceitAccount.visibility = android.view.View.VISIBLE
                "activityRecognition" -> llDeceitActivityRecognition.visibility = android.view.View.VISIBLE
                "applications" -> llDeceitApplications.visibility = android.view.View.VISIBLE
                "audio" -> llDeceitAudio.visibility = android.view.View.VISIBLE
                "calendar" -> llDeceitCalendar.visibility = android.view.View.VISIBLE
                "callLog" -> llDeceitCallLog.visibility = android.view.View.VISIBLE
                "camera" -> llDeceitCamera.visibility = android.view.View.VISIBLE
                "clipboard" -> llDeceitClipboard.visibility = android.view.View.VISIBLE
                "contacts" -> llDeceitContacts.visibility = android.view.View.VISIBLE
                "location" -> llDeceitLocation.visibility = android.view.View.VISIBLE
                "message" -> llDeceitMessage.visibility = android.view.View.VISIBLE
                "network" -> llDeceitNetwork.visibility = android.view.View.VISIBLE
                "notifications" -> llDeceitNotifications.visibility = android.view.View.VISIBLE
                "sensor" -> llDeceitSensor.visibility = android.view.View.VISIBLE
                "sync" -> llDeceitSync.visibility = android.view.View.VISIBLE
                "telephony" -> llDeceitTelephony.visibility = android.view.View.VISIBLE
                "tracking" -> llDeceitTracking.visibility = android.view.View.VISIBLE
                "video" -> llDeceitVideo.visibility = android.view.View.VISIBLE
                else -> {}
            }
        }
    }
}