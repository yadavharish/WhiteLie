package `in`.hyiitd.whitelie.ui.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentLogsBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.LogsUtil.currentLogsFilter
import `in`.hyiitd.whitelie.util.LogsUtil.filterLogsMenu
import `in`.hyiitd.whitelie.util.LogsUtil.filterRegisteredLogs
import `in`.hyiitd.whitelie.util.LogsUtil.filteredLogs

class FilterLogAdapter(val parentBinding: FragmentLogsBinding): RecyclerView.Adapter<FilterLogAdapter.FilterLogViewHolder>() {
    class FilterLogViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = filterLogsMenu.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilterLogViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FilterLogViewHolder, position: Int) {
        with(holder){
            with(filterLogsMenu[position]) {
                with(binding) {
                    tvName.text = name
                    val ctx = itemView.context
                    itemView.backgroundTintList = ctx.resources.getColorStateList(if(position == currentLogsFilter) R.color.theme1Green2 else R.color.theme1White, ctx.theme);
                    tvName.setTextColor(ContextCompat.getColor(ctx, if(position == currentLogsFilter) R.color.theme1Orange else R.color.theme1Green2))
                    itemView.setOnClickListener {
                        currentLogsFilter = value
                        filterRegisteredLogs()
                        parentBinding.rvLogs.adapter = LogAdapter(filteredLogs)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

//    fun updateUIs() {
//        bin
//        when(currentAppsFilter)
//    }
}