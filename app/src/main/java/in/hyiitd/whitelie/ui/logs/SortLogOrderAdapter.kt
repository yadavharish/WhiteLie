package `in`.hyiitd.whitelie.ui.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentLogsBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.LogsUtil.currentLogsSortOrder
import `in`.hyiitd.whitelie.util.LogsUtil.filteredLogs
import `in`.hyiitd.whitelie.util.LogsUtil.sortFilteredLogs
import `in`.hyiitd.whitelie.util.LogsUtil.sortOrderLogsMenu

class SortLogOrderAdapter(val parentBinding: FragmentLogsBinding): RecyclerView.Adapter<SortLogOrderAdapter.SortLogOrderViewHolder>() {
    class SortLogOrderViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = sortOrderLogsMenu.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SortLogOrderViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SortLogOrderViewHolder, position: Int) {
        with(holder){
            with(sortOrderLogsMenu[position]) {
                with(binding) {
                    tvName.text = name
                    val ctx = itemView.context
                    itemView.backgroundTintList = ctx.resources.getColorStateList(if(position == currentLogsSortOrder) R.color.theme1Green2 else R.color.theme1White, ctx.theme);
                    tvName.setTextColor(ContextCompat.getColor(ctx, if(position == currentLogsSortOrder) R.color.theme1Orange else R.color.theme1Green2))
                    itemView.setOnClickListener {
                        currentLogsSortOrder = value
                        sortFilteredLogs()
                        parentBinding.rvLogs.adapter = LogAdapter(filteredLogs)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}