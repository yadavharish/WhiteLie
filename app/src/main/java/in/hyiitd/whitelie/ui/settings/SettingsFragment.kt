package `in`.hyiitd.whitelie.ui.settings

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import `in`.hyiitd.whitelie.DeceiverContentProvider
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentSettingsBinding
import `in`.hyiitd.whitelie.util.DeceiverModuleUtil.batterySaver
import `in`.hyiitd.whitelie.util.SettingsUtil.viewDocumentation
import `in`.hyiitd.whitelie.util.SettingsUtil.viewSourceCode

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("Range")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        with(binding) {
            updateBatterySaverButtonUI(this, requireContext())

            val deceitSettingsCursor = context?.contentResolver?.query(DeceiverContentProvider.deceitSettingsContentURI, null, null, null, null)
            if(deceitSettingsCursor == null || deceitSettingsCursor.count == 0) {
                val values = ContentValues()
                values.put(DeceiverContentProvider.name, "batterySaver false")
                batterySaver = false

                context?.contentResolver?.insert(DeceiverContentProvider.deceitStringsContentURI, values)
            }
            else {
                val deceitSettingsMap = HashMap<String, String>()
                if (deceitSettingsCursor.moveToFirst())
                    while (!deceitSettingsCursor.isAfterLast) {
                        val names = deceitSettingsCursor.getString(deceitSettingsCursor.getColumnIndex("name")).split(" ")
                        deceitSettingsMap[names[0]] = names[1]
                        deceitSettingsCursor.moveToNext()
                    }
                batterySaver = deceitSettingsMap["batterySaver"].toBoolean()
            }

            updateBatterySaverButtonUI(this, ctx)

            cvSettingsDeceits.setOnClickListener { Navigation.findNavController(it).navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationDeceits()) }
            cvSettingsViewSourceCode.setOnClickListener { viewSourceCode(requireContext()) }
            cvSettingsDocumentation.setOnClickListener { viewDocumentation(requireContext()) }
            cvSettingsAbout.setOnClickListener { Navigation.findNavController(it).navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationAbout()) }
            cvSettingsBatterySaver.setOnClickListener {
                batterySaver = !batterySaver
                updateBatterySaverButtonUI(this, ctx)
                ctx.contentResolver?.delete(DeceiverContentProvider.deceitSettingsContentURI, "name LIKE 'batterySaver %'", null)
                val values = ContentValues()
                values.put(DeceiverContentProvider.name, "batterySaver $batterySaver")
                context?.contentResolver?.insert(DeceiverContentProvider.deceitSettingsContentURI, values)
            }
        }
        return binding.root
    }

    private fun updateBatterySaverButtonUI(binding: FragmentSettingsBinding, ctx: Context) {
        with(binding) {
            cvSettingsBatterySaver.background.setTint(getColor(ctx, if(batterySaver) R.color.theme1Green2 else R.color.theme1Blue))
            ivSettingsBatterySaver.setImageDrawable(getDrawable(ctx, if(batterySaver) R.drawable.ic_battery_full else R.drawable.ic_battery_empty))
            ivSettingsBatterySaver.setColorFilter(getColor(ctx, if(batterySaver) R.color.theme1Orange else R.color.theme1Green2))
            tvSettingsBatterySaver.typeface = getFont(ctx, if(batterySaver) R.font.red_hat_mono_medium else R.font.red_hat_mono)
            tvSettingsBatterySaver.setTextColor(getColor(ctx, if(batterySaver) R.color.theme1Orange else R.color.theme1Green2))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}