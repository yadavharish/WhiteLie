package `in`.hyiitd.whitelie.ui.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceitsBinding
import `in`.hyiitd.whitelie.databinding.ListItemFilterTabBinding
import `in`.hyiitd.whitelie.util.DeceitsUtil
import `in`.hyiitd.whitelie.util.DeceitsUtil.supportedModes
import `in`.hyiitd.whitelie.util.DeceitsUtil.updateDeceitValuesByMode

class DeceitsModesAdapter(val parentBinding: FragmentDeceitsBinding): RecyclerView.Adapter<DeceitsModesAdapter.DeceitsModesViewHolder>() {
    class DeceitsModesViewHolder(val binding: ListItemFilterTabBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = supportedModes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeceitsModesViewHolder(ListItemFilterTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DeceitsModesViewHolder, position: Int) {
        with(holder){
            with(binding) {
                tvName.text = supportedModes[position]
                val ctx = itemView.context
                val isSelected = position == DeceitsUtil.currentModeOption
                itemView.backgroundTintList = ctx.resources.getColorStateList(if(isSelected) R.color.theme1Green2 else R.color.theme1White, ctx.theme)
                tvName.setTextColor(ContextCompat.getColor(ctx, if(isSelected) R.color.theme1Orange else R.color.theme1Green2))

                itemView.setOnClickListener {
                    if(position != DeceitsUtil.currentModeOption) {
                        DeceitsUtil.currentModeOption = position
                        updateDeceitValuesByMode(position)
                        notifyDataSetChanged()
                    } else { }
                }
            }
        }
    }
}