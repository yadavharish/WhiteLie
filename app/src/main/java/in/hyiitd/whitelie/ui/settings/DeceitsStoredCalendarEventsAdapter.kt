package `in`.hyiitd.whitelie.ui.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.databinding.FragmentDeceitsBinding
import `in`.hyiitd.whitelie.databinding.ListItemStoredCalendarEventBinding

class DeceitsStoredCalendarEventsAdapter(val calendarEventStrings: List<String>, val parentBinding: FragmentDeceitsBinding): RecyclerView.Adapter<DeceitsStoredCalendarEventsAdapter.DeceitsStoredCalendarEventsViewHolder>() {
    class DeceitsStoredCalendarEventsViewHolder(val binding: ListItemStoredCalendarEventBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = calendarEventStrings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeceitsStoredCalendarEventsViewHolder(ListItemStoredCalendarEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DeceitsStoredCalendarEventsViewHolder, position: Int) {
        with(holder){
            with(binding) {
                val ctx = itemView.context
                val names = calendarEventStrings[position].split("#*#*#")
//                phoneNumber.text = names[0]
//                callerName.text = names[1]
//                callType.text = names[2]
//                callDate.text = names[3]
//                callDuration.text = names[4]
//
//                btnDeleteCallLogDetails.setOnClickListener {
//                    ctx.contentResolver.delete(DeceiverContentProvider.deceitCallLogsContentURI, "name = '${callLogStrings[position]}'", null)
//                    parentBinding.rvStoredCallLogs.adapter = DeceitsStoredCalendarEventsAdapter(DeceitsFragment.getCallLogsStrings(itemView.context), parentBinding)
//                    notifyDataSetChanged()
//                }
//
//                btnEditCallLogDetails.setOnClickListener {
//                    parentBinding.etCallLogPhoneNumber.setText(names[0])
//                    parentBinding.etCallLogCallerName.setText(names[1])
//                    parentBinding.etCallLogCallType.setText(names[2])
//                    parentBinding.etCallLogCallDate.setText(names[3])
//                    parentBinding.etCallLogCallDuration.setText(names[4])
//                    ctx.contentResolver.delete(DeceiverContentProvider.deceitCallLogsContentURI, "name = '${callLogStrings[position]}'", null)
//                    parentBinding.rvStoredCallLogs.adapter = DeceitsStoredCalendarEventsAdapter(DeceitsFragment.getCallLogsStrings(itemView.context), parentBinding)
//                    notifyDataSetChanged()
//                }
            }
        }
    }
}