package `in`.hyiitd.whitelie.ui.logs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.database.LogInfo
import `in`.hyiitd.whitelie.databinding.ListItemLogBinding
import `in`.hyiitd.whitelie.ui.deceiver.*

class LogAdapter(private val logs: List<LogInfo>): RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    class LogViewHolder(val binding: ListItemLogBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = logs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LogViewHolder(ListItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        with(holder){
            with(logs[position]) {
                with(binding) {
                    vLogoTopBar.visibility = if(position == 0) View.GONE else View.VISIBLE
                    vLogoBottomBar.visibility = if(position == logs.size - 1) View.GONE else View.VISIBLE
                    tvLogAppName.text = appName
                    tvLogPackageName.text = packageName
                    tvLogMessage.text = msg
                    tvLogTime.text = time
                    ivLogAppIcon.setImageDrawable(icon)
                    itemView.setOnClickListener { Navigation.findNavController(it).navigate(LogsFragmentDirections.actionNavigationLogsToNavigationDeceiverApp(DeceiverAppNavData(packageName))) }
                }
            }
        }
    }
}