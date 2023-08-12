package `in`.hyiitd.whitelie.ui.deceiver

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.database.AppInfo
import `in`.hyiitd.whitelie.databinding.ListItemAppBinding
import `in`.hyiitd.whitelie.util.UIUtil.toGrayscale

private const val DISABLED_CARD_UI_CODE = 0
private const val ENABLED_CARD_UI_CODE = 1
private const val ACTIVE_CARD_UI_CODE = 2

class AppAdapter(private val apps: List<AppInfo>): RecyclerView.Adapter<AppAdapter.AppViewHolder>() {
    class AppViewHolder(val binding: ListItemAppBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = apps.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AppViewHolder(ListItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        with(holder){
            with(apps[position]) {
                binding.tvAppName.text = appName
                binding.tvPackageName.text = packageName
                binding.tvNoOfPermsReq.text = noOfReqPerm.toString()
                binding.tvNoOfPermsGrant.text = noOfGrantPerm.toString()
                binding.tvNoOfPermsSpoof.text = noOfDeceivedPerm.toString()
                binding.ivCriticalIcon.visibility = if(systemApp) View.VISIBLE else View.GONE
                updateUI(binding, itemView.context, if(noOfGrantPerm == 0) DISABLED_CARD_UI_CODE else if(noOfDeceivedPerm == 0) ENABLED_CARD_UI_CODE else ACTIVE_CARD_UI_CODE, icon)
                itemView.setOnClickListener { Navigation.findNavController(it).navigate(DeceiverFragmentDirections.actionNavigationDeceiverToNavigationDeceiverApp(DeceiverAppNavData(packageName))) }
            }
        }
    }

    private fun updateUI(binding: ListItemAppBinding, ctx: Context, uiCode: Int, icon: Drawable?) {
        val contentColor = getColor(ctx, when(uiCode) {
            DISABLED_CARD_UI_CODE -> R.color.disabledCardContent
            ENABLED_CARD_UI_CODE -> R.color.theme1Green2
            ACTIVE_CARD_UI_CODE -> R.color.theme1Orange
            else -> android.R.color.darker_gray
        })
        val bgColor = getColor(ctx, when(uiCode) {
            DISABLED_CARD_UI_CODE -> R.color.nonSelectedCard
            ENABLED_CARD_UI_CODE -> R.color.theme1Blue
            ACTIVE_CARD_UI_CODE -> R.color.theme1Green2
            else -> android.R.color.darker_gray

        })
        with(binding) {
            tvAppName.setTextColor(contentColor)
            tvPackageName.setTextColor(contentColor)
            tvNoOfPermsReq.setTextColor(contentColor)
            tvNoOfPermsGrant.setTextColor(contentColor)
            tvNoOfPermsSpoof.setTextColor(contentColor)
            ivNoOfPermsReqIcon.setColorFilter(contentColor)
            ivNoOfPermsGrantIcon.setColorFilter(contentColor)
            ivNoOfPermsSpoofIcon.setColorFilter(contentColor)
            cvApp.background.setTint(bgColor)
            with(ivCriticalIcon) {
                setImageDrawable(getDrawable(ctx, R.drawable.critical_triangle_outline))
                setColorFilter(contentColor)
            }
            with(ivAppIcon) {
                setImageDrawable(if(uiCode == DISABLED_CARD_UI_CODE) toGrayscale(icon) else icon)
                alpha = if(uiCode == DISABLED_CARD_UI_CODE) 0.15f else 1.0f
            }
        }
    }
}