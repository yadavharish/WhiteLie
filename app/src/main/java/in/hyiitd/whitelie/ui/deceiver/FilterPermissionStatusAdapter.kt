package `in`.hyiitd.whitelie.ui.deceiver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceiverBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.PkgsUtil
import `in`.hyiitd.whitelie.util.PkgsUtil.filterPermissionStatusMenu

class FilterPermissionStatusAdapter(val parentBinding: FragmentDeceiverBinding): RecyclerView.Adapter<FilterPermissionStatusAdapter.FilterPermissionStatusViewHolder>() {
    class FilterPermissionStatusViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = filterPermissionStatusMenu.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilterPermissionStatusViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FilterPermissionStatusViewHolder, position: Int) {
        with(holder) {
            with(filterPermissionStatusMenu[position]) {
                with(binding) {
                    tvName.text = name
                    val ctx = itemView.context
                    itemView.backgroundTintList = ctx.resources.getColorStateList(if (position == PkgsUtil.currentPermissionStatusFilter) R.color.theme1Green2 else R.color.theme1White, ctx.theme)
                    tvName.setTextColor(ContextCompat.getColor(ctx, if (position == PkgsUtil.currentPermissionStatusFilter) R.color.theme1Orange else R.color.theme1Green2))
                    itemView.setOnClickListener {
                        PkgsUtil.currentPermissionStatusFilter = value
                        PkgsUtil.filterInstalledPackages()
                        parentBinding.rvApps.adapter = AppAdapter(PkgsUtil.filteredPackages)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}