package `in`.hyiitd.whitelie.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentHomeBinding
import `in`.hyiitd.whitelie.util.DeceiverModuleUtil
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_ALL_APPS
import `in`.hyiitd.whitelie.util.PkgsUtil.APPS_FILTER_SHOW_DECEIVED_APPS
import `in`.hyiitd.whitelie.util.SettingsUtil.viewDocumentation
import `in`.hyiitd.whitelie.util.SettingsUtil.viewSourceCode
import `in`.hyiitd.whitelie.util.UIUtil.createHomeFragCountCardViewsUI
import `in`.hyiitd.whitelie.util.UIUtil.createHomeFragDeceiverButtonsUI
import `in`.hyiitd.whitelie.util.UIUtil.createHomeFragLatestDeceivedAppCardViewUI
import `in`.hyiitd.whitelie.util.UIUtil.createHomeFragLatestLogsCardViewUI
import `in`.hyiitd.whitelie.util.UIUtil.createHomeFragModuleInstallationCardViewUI
import `in`.hyiitd.whitelie.util.UIUtil.greeting
import `in`.hyiitd.whitelie.util.UIUtil.homeFragmentCreated
import `in`.hyiitd.whitelie.util.UIUtil.openDeceiverFragFromHomeFragCountCardView


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeFragmentCreated = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        with(binding) {
            //get this username from user and the moduleInstalledStatus
            val userName = "user123"
            val ctx = requireContext()
            val moduleInstalledProperly = DeceiverModuleUtil.isXPModuleInstalled(requireContext())
//            val moduleInstalledProperly = DeceiverModule.isXposedInstalled()
            val activity = requireActivity()

//            tvGreeting.text = "${greeting()},\n$userName"                                                            //heading of home fragment
            tvGreeting.text = "${greeting()}"                                                            //heading of home fragment
            createHomeFragModuleInstallationCardViewUI(moduleInstalledProperly, ctx, this, activity)          //creating cardView indicating module installation status
            createHomeFragCountCardViewsUI(moduleInstalledProperly, this, activity)                           //creating count status cardViews
            createHomeFragLatestDeceivedAppCardViewUI(this, ctx, moduleInstalledProperly)                     //creating latestDeceivedApp cardView
            createHomeFragLatestLogsCardViewUI(this, ctx, activity, moduleInstalledProperly)                  //creating latestLogs cardView
            createHomeFragDeceiverButtonsUI(this, ctx, moduleInstalledProperly)                               //creating deceiver buttons

            cvViewDocsCard.setOnClickListener { viewDocumentation(requireContext()) }
            mcvSourceCodeCardCodeButton.setOnClickListener { viewSourceCode(requireContext()) }
            cvViewDeceitsExecutedButton.setOnClickListener { Navigation.findNavController(it).navigate(HomeFragmentDirections.actionNavigationHomeToNavigationDeceits()) }
            cvViewDeceivedAppsButton.setOnClickListener { openDeceiverFragFromHomeFragCountCardView(APPS_FILTER_SHOW_ALL_APPS, APPS_FILTER_SHOW_DECEIVED_APPS, requireActivity().findViewById(R.id.navView)) }

            val centerToLeftAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_move_card_from_center_to_left)
            val rightToCenterAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_move_card_from_right_to_center)

//            cvStarterFragStartDeceivingButton.setOnClickListener {
//                cvStarterFragCard1.startAnimation(centerToLeftAnimation)
//                cvStarterFragCard2.startAnimation(rightToCenterAnimation)
//            }
//            cvStarterFragStartDeceivingButton.setOnClickListener { cvStarterFragCard1.startAnimation(centerToLeftAnimation) }
//            cvStarterFragStartDeceivingButton.setOnClickListener { cvStarterFragCard1.startAnimation(centerToLeftAnimation) }

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}