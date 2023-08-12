package `in`.hyiitd.whitelie.ui.deceiver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.database.PermissionInfo
import `in`.hyiitd.whitelie.databinding.FragmentDeceiverAppBinding
import `in`.hyiitd.whitelie.util.DBUtil.db
import `in`.hyiitd.whitelie.util.PkgsUtil.fillPackageAdditionalInfo
import `in`.hyiitd.whitelie.util.UIUtil.gridSize


class DeceiverAppFragment : Fragment() {

    private var _binding: FragmentDeceiverAppBinding? = null
    private val args: DeceiverAppFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().findViewById<BottomNavigationView>(R.id.navView).visibility = View.GONE
        _binding = FragmentDeceiverAppBinding.inflate(inflater, container, false)

        val appInfo = db.appDao().loadByPackageName(args.appData.packageName)[0]
        fillPackageAdditionalInfo(appInfo, requireContext())
        val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(args.appData.packageName)
        val uninstallIntent = Intent(Intent.ACTION_DELETE, Uri.parse("package:" + args.appData.packageName))

        with(binding) {
            tvAppName.text = appInfo.appName
            tvPackageName.text = appInfo.packageName
            ivAppIcon.setImageDrawable(appInfo.icon)
            appInfo.permissions = appInfo.permissions.sortedWith(compareByDescending<PermissionInfo> { it.deceived }.thenByDescending { it.granted }.thenBy { it.permissionName })
            with(rvPermissions) {
                layoutManager = GridLayoutManager(activity, gridSize(resources))
                adapter = DPermissionAdapter(appInfo.permissions)
            }
            if (launchIntent != null) {
                llDeceiverAppMenuLaunchApp.setOnClickListener { startActivity(launchIntent) }
            } else {
                ivDeceiverAppMenuLaunchApp.setColorFilter(getColor(requireContext() , R.color.blue5))
                tvDeceiverAppMenuLaunchApp.setTextColor(getColor(requireContext() , R.color.blue5))
            }
//            if (uninstallIntent != null) {
//                llDeceiverAppMenuLaunchApp.setOnClickListener { startActivity(uninstallIntent) }
//            } else {
//                ivDeceiverAppMenuLaunchApp.setColorFilter(getColor(requireContext() , R.color.blue5))
//                tvDeceiverAppMenuLaunchApp.setTextColor(getColor(requireContext() , R.color.blue5))
//            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().findViewById<BottomNavigationView>(R.id.navView).visibility = View.VISIBLE
    }
}