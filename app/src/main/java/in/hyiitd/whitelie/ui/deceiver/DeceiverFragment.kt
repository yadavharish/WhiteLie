package `in`.hyiitd.whitelie.ui.deceiver

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceiverBinding
import `in`.hyiitd.whitelie.util.DBUtil
import `in`.hyiitd.whitelie.util.PkgsUtil
import `in`.hyiitd.whitelie.util.UIUtil.gridSize


class DeceiverFragment : Fragment() {
    private var _binding: FragmentDeceiverBinding? = null

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDeceiverBinding.inflate(inflater, container, false)
        val ctx = requireContext()
        PkgsUtil.refreshInstalledPackages(ctx)
        PkgsUtil.initMenus(ctx)
        val appAdapter = AppAdapter(PkgsUtil.filteredPackages)

        with(binding) {
            with(rvApps) {
                layoutManager = GridLayoutManager(ctx, gridSize(resources))
                adapter = appAdapter
            }
            with(rvFilterApps) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = FilterAppAdapter(binding)
            }
            with(rvFilterPermissionStatus) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = FilterPermissionStatusAdapter(binding)
            }
            with(rvFilterPermissions) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = FilterPermissionAdapter(binding)
            }
            with(rvSortApps) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = SortAppAdapter(binding)
            }
            with(rvSortOrderApps) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = SortAppOrderAdapter(binding)
            }
            llDeceiverMenuFilterButton.setOnClickListener { updateMenus(0, binding, ctx) }
            llDeceiverMenuSortButton.setOnClickListener { updateMenus(1, binding, ctx) }
            llDeceiverMenuClearDeceitsButton.setOnClickListener {4
                DBUtil.db.permDao().clearAllDeceived()
                PkgsUtil.refreshInstalledPackages(ctx)
                rvApps.adapter = AppAdapter(PkgsUtil.filteredPackages)
                appAdapter.notifyDataSetChanged()
            }
            llDeceiverMenuClearDeceitsButton.setOnTouchListener { view, motionEvent ->
                ivDeceiverMenuClearDeceits.setImageDrawable(getDrawable(ctx, if(motionEvent.action == MotionEvent.ACTION_DOWN) R.drawable.ic_clear_solid else R.drawable.ic_clear_outline))
                tvDeceiverMenuClearDeceits.typeface = ResourcesCompat.getFont(ctx, if(motionEvent.action == MotionEvent.ACTION_DOWN) R.font.red_hat_mono_medium else R.font.red_hat_mono)
                true
            }
            llDeceiverMenuRefreshButton.setOnClickListener { PkgsUtil.refreshInstalledPackages(ctx) }
            llDeceiverMenuRefreshButton.setOnTouchListener { view, motionEvent ->
                ivDeceiverMenuRefresh.setImageDrawable(getDrawable(ctx, if(motionEvent.action == MotionEvent.ACTION_DOWN) R.drawable.ic_refresh_solid else R.drawable.ic_refresh_outline))
                tvDeceiverMenuRefresh.typeface = ResourcesCompat.getFont(ctx, if(motionEvent.action == MotionEvent.ACTION_DOWN) R.font.red_hat_mono_medium else R.font.red_hat_mono)
                true
            }
            ivDeceiverSearchButton.setOnClickListener {
                PkgsUtil.filterInstalledPackages()
                rvApps.adapter = AppAdapter(PkgsUtil.filteredPackages)
                appAdapter.notifyDataSetChanged()
            }
            etDeceiverSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    PkgsUtil.filterSearchText = etDeceiverSearch.text.toString()
                }
            })
            return root
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvApps.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateMenus(menu: Int, binding: FragmentDeceiverBinding, ctx: Context) {
        with(binding) {
            val filterMenuShow = menu == 0 && llFilterMenu.visibility != View.VISIBLE
            val sortMenuShow = menu == 1 && llSortMenu.visibility != View.VISIBLE
            llFilterMenu.visibility = if(filterMenuShow) View.VISIBLE else View.GONE
            llSortMenu.visibility = if(sortMenuShow) View.VISIBLE else View.GONE
            ivDeceiverMenuFilter.setImageDrawable(getDrawable(ctx, if(filterMenuShow) R.drawable.ic_filter_solid else R.drawable.ic_filter_outline))
            ivDeceiverMenuSort.setImageDrawable(getDrawable(ctx, if(sortMenuShow) R.drawable.ic_sort_solid else R.drawable.ic_sort_outline))
            tvDeceiverMenuFilter.typeface = ResourcesCompat.getFont(ctx, if(filterMenuShow) R.font.red_hat_mono_medium else R.font.red_hat_mono)
            tvDeceiverMenuSort.typeface = ResourcesCompat.getFont(ctx, if(sortMenuShow) R.font.red_hat_mono_medium else R.font.red_hat_mono)
        }
    }
}