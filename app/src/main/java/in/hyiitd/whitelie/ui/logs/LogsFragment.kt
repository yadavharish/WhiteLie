package `in`.hyiitd.whitelie.ui.logs

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import `in`.hyiitd.whitelie.DeceiverContentProvider
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.database.LogInfo
import `in`.hyiitd.whitelie.databinding.FragmentLogsBinding
import `in`.hyiitd.whitelie.util.DBUtil.db
import `in`.hyiitd.whitelie.util.LogsUtil
import `in`.hyiitd.whitelie.util.LogsUtil.filterLogsSearchText
import `in`.hyiitd.whitelie.util.MiscUtil
import java.time.LocalDateTime
import kotlin.random.Random

class LogsFragment : Fragment() {
    private var _binding: FragmentLogsBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged", "Range")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        val ctx = requireContext()

//        val pkg = PkgsUtil.installedPackages[Random.nextInt(PkgsUtil.installedPackages.size)]
//        db.logsDao().insert(LogInfo(LocalDateTime.now().plusSeconds(Random.nextInt().toLong()).format(MiscUtil.dateTimeFormatterPattern), pkg.appName, pkg.packageName, "${if(i%2 == 0) "hi" else "hello"} ${pkg.appName} got spoofed"))
        //using PkgsUtil.installedPackages find appName from packageName
//        LogInfo(time, appName, pkgName, msg)
        val pm = ctx.packageManager
        val deceiverLogsCursor = ctx.contentResolver.query(DeceiverContentProvider.deceiverLogsContentURI, null, null, null, null)
        if(deceiverLogsCursor != null && deceiverLogsCursor.count != 0) {
            if (deceiverLogsCursor.moveToFirst())
                while (!deceiverLogsCursor.isAfterLast) {
                    val names = deceiverLogsCursor.getString(deceiverLogsCursor.getColumnIndex("name")).split("#*#*#")
                    try {
                        db.logsDao().insert(LogInfo(names[0], pm.getApplicationLabel(pm.getApplicationInfo(names[1], PackageManager.GET_META_DATA)).toString(), names[1], names[2]))
                    } catch (_: java.lang.Exception) { }
                    deceiverLogsCursor.moveToNext()
                }
            deceiverLogsCursor.close()
            ctx.contentResolver.delete(DeceiverContentProvider.deceiverLogsContentURI, null, null)
        }

        LogsUtil.refreshRegisteredLogs(ctx)
        LogsUtil.initMenus(ctx)
        val logAdapter = LogAdapter(LogsUtil.filteredLogs)

        with(binding) {
            binding.etLogsSearch.setText(filterLogsSearchText)
            filterLogsSearchText = ""

            with(rvLogs) {
                layoutManager = LinearLayoutManager(context)
                adapter = logAdapter
            }
            with(rvFilterLogs) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = FilterLogAdapter(binding)
            }
            with(rvSortLogs) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = SortLogAdapter(binding)
            }
            with(rvSortOrderLogs) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = SortLogOrderAdapter(binding)
            }

            fun addLog(msg: String) {
                val values = ContentValues()
                values.put(DeceiverContentProvider.name, "${LocalDateTime.now().format(MiscUtil.dateTimeFormatterPattern)}#*#*#com.facebook.katana#*#*#$msg")
                context?.contentResolver?.insert(DeceiverContentProvider.deceiverLogsContentURI, values)
            }

            llLogsMenuFilterButton.setOnClickListener {
                updateMenus(0, binding, ctx)
                if(Random.nextBoolean()) {
                    addLog("mic access requested")
                    addLog("audio data requested deceived with blank audio from local store")
                } else if(Random.nextBoolean()) {
                    addLog("location data requested")
                    addLog("location data deceived to: Lat:37.484937, Long:-122.148454")
                } else {
                    addLog("calendar data requested")
                    addLog("calendar data deceived with 8 user defined events")
                }
            }
            llLogsMenuSortButton.setOnClickListener { updateMenus(1, binding, ctx) }
            llLogsMenuClearLogsButton.setOnClickListener {
                db.logsDao().deleteAll()
                LogsUtil.refreshRegisteredLogs(ctx)
                rvLogs.adapter = logAdapter
                logAdapter.notifyDataSetChanged()
            }
            llLogsMenuClearLogsButton.setOnTouchListener { _, motionEvent ->
                ivLogsMenuClearLogs.setImageDrawable(ContextCompat.getDrawable(ctx, if (motionEvent.action == MotionEvent.ACTION_DOWN) R.drawable.ic_clear_solid else R.drawable.ic_clear_outline))
                tvLogsMenuClearLogs.typeface = ResourcesCompat.getFont(ctx, if(motionEvent.action == MotionEvent.ACTION_DOWN) R.font.red_hat_mono_medium else R.font.red_hat_mono)
                true
            }

            ivLogsSearchButton.setOnClickListener {
                LogsUtil.filterRegisteredLogs()
                rvLogs.adapter = logAdapter
                logAdapter.notifyDataSetChanged()
            }

            etLogsSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    filterLogsSearchText = etLogsSearch.text.toString()
                }
            })

            etLogsSearch.setText(filterLogsSearchText)
            filterLogsSearchText = ""

            return root
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvLogs.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //updates the filter and sort button and menu UI on Logs Fragment
    private fun updateMenus(menu: Int, binding: FragmentLogsBinding, ctx: Context) {
        with(binding) {
            val filterMenuShow = menu == 0 && llFilterMenu.visibility != VISIBLE
            val sortMenuShow = menu == 1 && llSortMenu.visibility != VISIBLE
            llFilterMenu.visibility = if(filterMenuShow) VISIBLE else GONE
            llSortMenu.visibility = if(sortMenuShow) VISIBLE else GONE
            ivLogsMenuFilter.setImageDrawable(ContextCompat.getDrawable(ctx, if (filterMenuShow) R.drawable.ic_filter_solid else R.drawable.ic_filter_outline))
            ivLogsMenuSort.setImageDrawable(ContextCompat.getDrawable(ctx, if (sortMenuShow) R.drawable.ic_sort_solid else R.drawable.ic_sort_outline))
            tvLogsMenuFilter.typeface = ResourcesCompat.getFont(ctx, if(filterMenuShow) R.font.red_hat_mono_medium else R.font.red_hat_mono)
            tvLogsMenuSort.typeface = ResourcesCompat.getFont(ctx, if(sortMenuShow) R.font.red_hat_mono_medium else R.font.red_hat_mono)
        }
    }
}