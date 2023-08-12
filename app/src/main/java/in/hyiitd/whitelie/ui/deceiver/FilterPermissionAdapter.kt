package `in`.hyiitd.whitelie.ui.deceiver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceiverBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.PermsUtil.supportedPermissions
import `in`.hyiitd.whitelie.util.PkgsUtil
import `in`.hyiitd.whitelie.util.PkgsUtil.filterPermissionsSelected

class FilterPermissionAdapter(val parentBinding: FragmentDeceiverBinding): RecyclerView.Adapter<FilterPermissionAdapter.FilterPermissionViewHolder>() {
    class FilterPermissionViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = supportedPermissions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilterPermissionViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FilterPermissionViewHolder, position: Int) {
        with(holder){
            with(supportedPermissions[position]) {
                with(binding) {
                    tvName.text = name
                    val ctx = itemView.context
                    val isSelected = name in filterPermissionsSelected
                    itemView.backgroundTintList = ctx.resources.getColorStateList(if (isSelected) R.color.theme1Green2 else R.color.theme1White, ctx.theme)
                    tvName.setTextColor(ContextCompat.getColor(ctx, if (isSelected) R.color.theme1Orange else R.color.theme1Green2))
                    itemView.setOnClickListener {
                        filterPermissionsSelected = if(isSelected) filterPermissionsSelected - name
                        else filterPermissionsSelected + name
                        PkgsUtil.filterInstalledPackages()
                        parentBinding.rvApps.adapter = AppAdapter(PkgsUtil.filteredPackages)
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }
}