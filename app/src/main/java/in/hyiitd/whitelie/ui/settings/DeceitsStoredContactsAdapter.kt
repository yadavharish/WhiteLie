package `in`.hyiitd.whitelie.ui.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.DeceiverContentProvider
import `in`.hyiitd.whitelie.databinding.FragmentDeceitsBinding
import `in`.hyiitd.whitelie.databinding.ListItemStoredContactBinding

class DeceitsStoredContactsAdapter(val contactStrings: List<String>, val parentBinding: FragmentDeceitsBinding): RecyclerView.Adapter<DeceitsStoredContactsAdapter.DeceitsStoredContactsViewHolder>() {
    class DeceitsStoredContactsViewHolder(val binding: ListItemStoredContactBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = contactStrings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeceitsStoredContactsViewHolder(ListItemStoredContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DeceitsStoredContactsViewHolder, position: Int) {
        with(holder){
            with(binding) {
                val ctx = itemView.context
                val names = contactStrings[position].split("#*#*#")
                phoneNumber.text = names[0]
                contactName.text = names[1]

                btnDeleteContactsDetails.setOnClickListener {
                    ctx.contentResolver.delete(DeceiverContentProvider.deceitContactsContentURI, "name = '${contactStrings[position]}'", null)
                    parentBinding.rvStoredContacts.adapter = DeceitsStoredContactsAdapter(DeceitsFragment.getContactsStrings(itemView.context), parentBinding)
                    notifyDataSetChanged()
                }

                btnEditContactsDetails.setOnClickListener {
                    parentBinding.etContactsPhoneNumber.setText(names[0])
                    parentBinding.etContactsContactName.setText(names[1])
                    ctx.contentResolver.delete(DeceiverContentProvider.deceitContactsContentURI, "name = '${contactStrings[position]}'", null)
                    parentBinding.rvStoredContacts.adapter = DeceitsStoredContactsAdapter(DeceitsFragment.getContactsStrings(itemView.context), parentBinding)
                    notifyDataSetChanged()
                }
            }
        }
    }
}