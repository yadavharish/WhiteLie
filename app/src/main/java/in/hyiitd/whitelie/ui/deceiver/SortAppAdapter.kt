package `in`.hyiitd.whitelie.ui.deceiver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceiverBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.PkgsUtil.currentAppsSortOption
import `in`.hyiitd.whitelie.util.PkgsUtil.filteredPackages
import `in`.hyiitd.whitelie.util.PkgsUtil.sortAppMenu
import `in`.hyiitd.whitelie.util.PkgsUtil.sortFilteredPackages

class SortAppAdapter(val parentBinding: FragmentDeceiverBinding): RecyclerView.Adapter<SortAppAdapter.SortAppViewHolder>() {
    class SortAppViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = sortAppMenu.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SortAppViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SortAppViewHolder, position: Int) {
        with(holder){
            with(sortAppMenu[position]) {
                with(binding) {
                    tvName.text = name
                    val ctx = itemView.context
                    itemView.backgroundTintList = ctx.resources.getColorStateList(if(position == currentAppsSortOption) R.color.theme1Green2 else R.color.theme1White, ctx.theme);
                    tvName.setTextColor(ContextCompat.getColor(ctx, if(position == currentAppsSortOption) R.color.theme1Orange else R.color.theme1Green2))
                    itemView.setOnClickListener {
                        currentAppsSortOption = value
                        sortFilteredPackages()
                        parentBinding.rvApps.adapter = AppAdapter(filteredPackages)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}