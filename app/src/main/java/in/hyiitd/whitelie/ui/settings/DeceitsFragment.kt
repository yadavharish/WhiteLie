package `in`.hyiitd.whitelie.ui.settings

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import `in`.hyiitd.whitelie.DeceiverContentProvider
import `in`.hyiitd.whitelie.R
import `in`.hyiitd.whitelie.databinding.FragmentDeceitsBinding
import java.util.*
import kotlin.collections.HashMap


class DeceitsFragment : Fragment() {
    private var _binding: FragmentDeceitsBinding? = null
    private val binding get() = _binding!!
    private var currentActivityRecognitionSelected = ""

    @SuppressLint("Range")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().findViewById<BottomNavigationView>(R.id.navView).visibility = View.GONE
        _binding = FragmentDeceitsBinding.inflate(inflater, container, false)
        val ctx = requireContext()

        with(binding) {
            with(rvPermissionsMenu) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = DeceitsPermissionsAdapter(binding)
            }
            with(rvModes) {
                layoutManager = FlexboxLayoutManager(ctx, FlexDirection.ROW, FlexWrap.WRAP)
                adapter = DeceitsModesAdapter(binding)
            }
            llDeceitMenuPermissionsButton.setOnClickListener { updateMenus(0, binding, ctx) }
            llDeceitMenuModeButton.setOnClickListener { updateMenus(1, binding, ctx) }

            val deceitStringCursor = context?.contentResolver?.query(DeceiverContentProvider.deceitStringsContentURI, null, null, null, null)
            if(deceitStringCursor == null || deceitStringCursor.count == 0) {
                val values = ContentValues()

                values.put(DeceiverContentProvider.name, "accountName deceiver@hyiitd.in")
                etAccountName.setText("deceiver@gmail.com")
                values.put(DeceiverContentProvider.name, "accountType com.google")
                etAccountType.setText("com.google")

                values.put(DeceiverContentProvider.name, "activityRecognised unknown")
                updateActivityRecognitionButtonsUI("unknown", ctx)
                currentActivityRecognitionSelected = "unknown"

                values.put(DeceiverContentProvider.name, "clipboardLabel Deceiver")
                etClipboardLabel.setText("Deceiver")
                values.put(DeceiverContentProvider.name, "clipboardText Deceiver")
                etClipboardText.setText("Deceiver")

                values.put(DeceiverContentProvider.name, "locationLatitude 0.000000")
                etLocationLatitude.setText("0.000000")
                values.put(DeceiverContentProvider.name, "locationLongitude 0.000000")
                etLocationLongitude.setText("0.000000")

                values.put(DeceiverContentProvider.name, "adClientId unknown")
                etTelephonyAndroidId.setText("unknown")
                values.put(DeceiverContentProvider.name, "iccId unknown")
                etTelephonyIccId.setText("unknown")
                values.put(DeceiverContentProvider.name, "telephonyImei unknown")
                etTelephonyImei.setText("unknown")
                values.put(DeceiverContentProvider.name, "mcc unknown")
                etTelephonyMcc.setText("unknown")
                values.put(DeceiverContentProvider.name, "telephonyMeid unknown")
                etTelephonyMeid.setText("unknown")
                values.put(DeceiverContentProvider.name, "mnc unknown")
                etTelephonyMnc.setText("unknown")
                values.put(DeceiverContentProvider.name, "phoneNumber unknown")
                etTelephonyPhoneNumber.setText("unknown")
                values.put(DeceiverContentProvider.name, "simSerialNumber unknown")
                etTelephonySimSerialNumber.setText("unknown")
                values.put(DeceiverContentProvider.name, "subscriberId unknown")
                etTelephonySubscriberId.setText("unknown")
                values.put(DeceiverContentProvider.name, "voiceMailAlphaTag unknown")
                etTelephonyVoiceMailAlphaTag.setText("unknown")

                values.put(DeceiverContentProvider.name, "adClientId unknown")
                etTrackingAdClientId.setText("unknown")
                values.put(DeceiverContentProvider.name, "androidId unknown")
                etTrackingAndroidId.setText("unknown")
                values.put(DeceiverContentProvider.name, "bluetoothName unknown")
                etTrackingBluetoothName.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildBoard unknown")
                etTrackingBuildBoard.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildBootLoader unknown")
                etTrackingBuildBootloader.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildBrand unknown")
                etTrackingBuildBrand.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildDevice unknown")
                etTrackingBuildDevice.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildDisplay unknown")
                etTrackingBuildDisplay.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildHardware unknown")
                etTrackingBuildHardware.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildHost unknown")
                etTrackingBuildHost.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildId unknown")
                etTrackingBuildId.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildManufacturer unknown")
                etTrackingBuildManufacturer.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildModel unknown")
                etTrackingBuildModel.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildOdmSku unknown")
                etTrackingBuildODMSKU.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildProduct unknown")
                etTrackingBuildProduct.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildQEMU unknown")
                etTrackingBuildQEMU.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildRadioVersion unknown")
                etTrackingBuildRadio.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildSku unknown")
                etTrackingBuildSKU.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildTags unknown")
                etTrackingBuildTags.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildType unknown")
                etTrackingBuildType.setText("unknown")
                values.put(DeceiverContentProvider.name, "buildUser unknown")
                etTrackingBuildUser.setText("unknown")
                values.put(DeceiverContentProvider.name, "subscriptionInfoCarrierName unknown")
                etTrackingCarrierName.setText("unknown")
                values.put(DeceiverContentProvider.name, "countryIso unknown")
                etTrackingCountryIso.setText("unknown")
                values.put(DeceiverContentProvider.name, "operatorName unknown")
                etTrackingOperatorName.setText("unknown")
                values.put(DeceiverContentProvider.name, "serialNumber unknown")
                etTrackingSerialNumber.setText("unknown")

                context?.contentResolver?.insert(DeceiverContentProvider.deceitStringsContentURI, values)
            }
            else {
                val deceitStringsMap = HashMap<String, String>()
                if (deceitStringCursor.moveToFirst())
                    while (!deceitStringCursor.isAfterLast) {
                        val names = deceitStringCursor.getString(deceitStringCursor.getColumnIndex("name")).split(" ")
                        deceitStringsMap[names[0]] = names[1]
                        deceitStringCursor.moveToNext()
                    }

                etAccountName.setText(deceitStringsMap["accountName"])
                etAccountType.setText(deceitStringsMap["accountType"])

                updateActivityRecognitionButtonsUI(deceitStringsMap["activityRecognised"].toString(), ctx)
                currentActivityRecognitionSelected = deceitStringsMap["activityRecognised"].toString()

                etClipboardLabel.setText(deceitStringsMap["clipboardLabel"])
                etClipboardText.setText(deceitStringsMap["clipboardText"])

                etLocationLatitude.setText(deceitStringsMap["locationLatitude"])
                etLocationLongitude.setText(deceitStringsMap["locationLongitude"])

                etTelephonyAndroidId.setText(deceitStringsMap["adClientId"])
                etTelephonyIccId.setText(deceitStringsMap["iccId"])
                etTelephonyImei.setText(deceitStringsMap["telephonyImei"])
                etTelephonyMcc.setText(deceitStringsMap["mcc"])
                etTelephonyMeid.setText(deceitStringsMap["telephonyMeid"])
                etTelephonyMnc.setText(deceitStringsMap["mnc"])
                etTelephonyPhoneNumber.setText(deceitStringsMap["phoneNumber"])
                etTelephonySimSerialNumber.setText(deceitStringsMap["simSerialNumber"])
                etTelephonySubscriberId.setText(deceitStringsMap["subscriberId"])
                etTelephonyVoiceMailAlphaTag.setText(deceitStringsMap["voiceMailAlphaTag"])

                etTrackingAdClientId.setText(deceitStringsMap["adClientId"])
                etTrackingAndroidId.setText(deceitStringsMap["androidId"])
                etTrackingBluetoothName.setText(deceitStringsMap["bluetoothName"])
                etTrackingBuildBoard.setText(deceitStringsMap["buildBoard"])
                etTrackingBuildBootloader.setText(deceitStringsMap["buildBootLoader"])
                etTrackingBuildBrand.setText(deceitStringsMap["buildBrand"])
                etTrackingBuildDevice.setText(deceitStringsMap["buildDevice"])
                etTrackingBuildDisplay.setText(deceitStringsMap["buildDisplay"])
                etTrackingBuildHardware.setText(deceitStringsMap["buildHardware"])
                etTrackingBuildHost.setText(deceitStringsMap["buildHost"])
                etTrackingBuildId.setText(deceitStringsMap["buildId"])
                etTrackingBuildManufacturer.setText(deceitStringsMap["buildManufacturer"])
                etTrackingBuildModel.setText(deceitStringsMap["buildModel"])
                etTrackingBuildODMSKU.setText(deceitStringsMap["buildOdmSku"])
                etTrackingBuildProduct.setText(deceitStringsMap["buildProduct"])
                etTrackingBuildQEMU.setText(deceitStringsMap["buildQEMU"])
                etTrackingBuildRadio.setText(deceitStringsMap["buildRadioVersion"])
                etTrackingBuildSKU.setText(deceitStringsMap["buildSku"])
                etTrackingBuildTags.setText(deceitStringsMap["buildTags"])
                etTrackingBuildType.setText(deceitStringsMap["buildType"])
                etTrackingBuildUser.setText(deceitStringsMap["buildUser"])
                etTrackingCarrierName.setText(deceitStringsMap["subscriptionInfoCarrierName"])
                etTrackingCountryIso.setText(deceitStringsMap["countryIso"])
                etTrackingOperatorName.setText(deceitStringsMap["operatorName"])
                etTrackingSerialNumber.setText(deceitStringsMap["serialNumber"])
            }

            etAccountName.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("accountName", etClipboardLabel.text.toString(),"deceiver@hyiitd.in") }
            etAccountType.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("accountType", etClipboardText.text.toString(),"com.google") }
            btnClipboardRandom.setOnClickListener {
                var randomString = ""
                for(i in 0..(3..32).random()) randomString += getRandomAlphaNumericCharacter()
                etClipboardLabel.setText(randomString)
                updateDeceitStringContent("accountName", randomString,"deceiver@hyiitd.in")

                randomString = getRandomAccountType()
                etClipboardText.setText(randomString)
                updateDeceitStringContent("accountType", randomString,"com.google")
            }

            btnActivityRecognitionInVehicle.setOnClickListener {
                if(currentActivityRecognitionSelected != "inVehicle") {
                    updateDeceitStringContent("activityRecognised", "inVehicle", "unknown")
                    updateActivityRecognitionButtonsUI("inVehicle", ctx)
                    currentActivityRecognitionSelected = "inVehicle"
                }
            }
            btnActivityRecognitionOnBicycle.setOnClickListener {
                if(currentActivityRecognitionSelected != "onBicycle") {
                    updateDeceitStringContent("activityRecognised", "onBicycle", "unknown")
                    updateActivityRecognitionButtonsUI("onBicycle", ctx)
                    currentActivityRecognitionSelected = "onBicycle"
                }
            }
            btnActivityRecognitionOnFoot.setOnClickListener {
                if(currentActivityRecognitionSelected != "onFoot") {
                    updateDeceitStringContent("activityRecognised", "onFoot", "unknown")
                    updateActivityRecognitionButtonsUI("onFoot", ctx)
                    currentActivityRecognitionSelected = "onFoot"
                }
            }
            btnActivityRecognitionStill.setOnClickListener {
                if(currentActivityRecognitionSelected != "still") {
                    updateDeceitStringContent("activityRecognised", "still", "unknown")
                    updateActivityRecognitionButtonsUI("still", ctx)
                    currentActivityRecognitionSelected = "still"
                }
            }
            btnActivityRecognitionUnknown.setOnClickListener {
                if(currentActivityRecognitionSelected != "unknown") {
                    updateDeceitStringContent("activityRecognised", "unknown", "unknown")
                    updateActivityRecognitionButtonsUI("unknown", ctx)
                    currentActivityRecognitionSelected = "unknown"
                }
            }
            btnActivityRecognitionTilting.setOnClickListener {
                if(currentActivityRecognitionSelected != "tilting") {
                    updateDeceitStringContent("activityRecognised", "tilting", "unknown")
                    updateActivityRecognitionButtonsUI("tilting", ctx)
                    currentActivityRecognitionSelected = "tilting"
                }
            }
            btnActivityRecognitionWalking.setOnClickListener {
                if(currentActivityRecognitionSelected != "walking") {
                    updateDeceitStringContent("activityRecognised", "walking", "unknown")
                    updateActivityRecognitionButtonsUI("walking", ctx)
                    currentActivityRecognitionSelected = "walking"
                }
            }
            btnActivityRecognitionRunning.setOnClickListener {
                if(currentActivityRecognitionSelected != "running") {
                    updateDeceitStringContent("activityRecognised", "running", "unknown")
                    updateActivityRecognitionButtonsUI("running", ctx)
                    currentActivityRecognitionSelected = "running"
                }
            }

            with(rvStoredCallLogs) {
                layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                adapter = DeceitsStoredCallLogAdapter(getCallLogsStrings(ctx), binding)
            }
            btnCallLogSaveRecord.setOnClickListener {
                val cv = ContentValues()
                cv.put(DeceiverContentProvider.name, "${etCallLogPhoneNumber.text}#*#*#${etCallLogCallerName.text}#*#*#${etCallLogCallType.text}#*#*#${etCallLogCallDate.text}#*#*#${etCallLogCallDuration.text}")
                ctx.contentResolver.insert(DeceiverContentProvider.deceitCallLogsContentURI, cv)
                etCallLogPhoneNumber.text.clear()
                etCallLogCallerName.text.clear()
                etCallLogCallType.text.clear()
                etCallLogCallDate.text.clear()
                etCallLogCallDuration.text.clear()
                rvStoredCallLogs.adapter = DeceitsStoredCallLogAdapter(getCallLogsStrings(ctx), binding)
            }

            with(rvStoredContacts) {
                layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                adapter = DeceitsStoredContactsAdapter(getContactsStrings(ctx), binding)
            }
            btnContactsSaveRecord.setOnClickListener {
                val cv = ContentValues()
                cv.put(DeceiverContentProvider.name, "${etContactsPhoneNumber.text}#*#*#${etContactsContactName.text}")
                ctx.contentResolver.insert(DeceiverContentProvider.deceitContactsContentURI, cv)
                etContactsPhoneNumber.text.clear()
                etContactsContactName.text.clear()
                rvStoredContacts.adapter = DeceitsStoredContactsAdapter(getContactsStrings(ctx), binding)
            }

            etClipboardLabel.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) {
                Log.i("deceiverLogs", "clipboardLabel value for deceiving updated to ${etClipboardLabel.text.toString()} in Deceiver Database")
                updateDeceitStringContent("clipboardLabel", etClipboardLabel.text.toString(),"Deceiver")
            } }
            etClipboardText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) {
                Log.i("deceiverLogs", "clipboardText value for deceiving updated to ${etClipboardText.text.toString()} in Deceiver Database")
                updateDeceitStringContent("clipboardText", etClipboardText.text.toString(),"Deceiver")
            } }
            btnClipboardRandom.setOnClickListener  {
                var randomString = ""
                for(i in 0..(3..32).random()) randomString += getRandomAlphaNumericWithSymbolCharacter()
                etClipboardLabel.setText(randomString)
                Log.i("deceiverLogs", "clipboardText value for deceiving updated to ${etClipboardLabel.text.toString()} in Deceiver Database")
                updateDeceitStringContent("clipboardLabel", randomString,"Deceiver")

                randomString = ""
                for(i in 0..(3..32).random()) randomString += getRandomAlphaNumericWithSymbolCharacter()
                etClipboardText.setText(randomString)
                Log.i("deceiverLogs", "clipboardText value for deceiving updated to ${etClipboardText.text.toString()} in Deceiver Database")
                updateDeceitStringContent("clipboardText", randomString,"Deceiver")
            }

            etLocationLatitude.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) {
                Log.i("deceiverLogs", "locationLatitude value for deceiving updated to ${etLocationLatitude.text.toString()} in Deceiver Database")
                updateDeceitStringContent("locationLatitude", etLocationLatitude.text.toString(),"0.000000")
            } }
            etLocationLongitude.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) {
                Log.i("deceiverLogs", "locationLongitude value for deceiving updated to ${etLocationLongitude.text.toString()} in Deceiver Database")
                updateDeceitStringContent("locationLongitude", etLocationLongitude.text.toString(),"0.000000")
            } }
            btnLocationRandom.setOnClickListener {
                val rand = Random()
                var randomString = String.format("%.6f", (rand.nextFloat()*180)-90)
                etLocationLatitude.setText(randomString)
                Log.i("deceiverLogs", "locationLatitude value for deceiving updated to ${etLocationLatitude.text.toString()} in Deceiver Database")
                updateDeceitStringContent("locationLatitude", randomString,"0.000000")

                randomString = String.format("%.6f", (rand.nextFloat()*360)-180)
                etLocationLongitude.setText(randomString)
                Log.i("deceiverLogs", "locationLongitude value for deceiving updated to ${etLocationLongitude.text.toString()} in Deceiver Database")
                updateDeceitStringContent("locationLongitude", randomString,"0.000000")
            }

            etTelephonyAndroidId.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("adClientId", etTelephonyAndroidId.text.toString(),"unknown")}
            etTelephonyIccId.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("iccId", etTelephonyIccId.text.toString(),"unknown")}
            etTelephonyImei.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("telephonyImei", etTelephonyImei.text.toString(),"unknown")}
            etTelephonyMcc.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("mcc", etTelephonyMcc.text.toString(),"unknown")}
            etTelephonyMeid.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("telephonyMeid", etTelephonyMeid.text.toString(),"unknown")}
            etTelephonyMnc.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("mnc", etTelephonyMnc.text.toString(),"unknown")}
            etTelephonyPhoneNumber.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("phoneNumber", etTelephonyPhoneNumber.text.toString(),"unknown")}
            etTelephonySimSerialNumber.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("simSerialNumber", etTelephonySimSerialNumber.text.toString(),"unknown")}
            etTelephonySubscriberId.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("subscriberId", etTelephonySubscriberId.text.toString(),"unknown")}
            etTelephonyVoiceMailAlphaTag.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("voiceMailAlphaTag", etTelephonyVoiceMailAlphaTag.text.toString(),"unknown")}

            etTrackingAdClientId.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("adClientId", etTrackingAdClientId.text.toString(),"unknown")}
            etTrackingAndroidId.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("androidId", etTrackingAndroidId.text.toString(),"unknown")}
            etTrackingBluetoothName.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("bluetoothName", etTrackingBluetoothName.text.toString(),"unknown")}
            etTrackingBuildBoard.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildBoard", etTrackingBuildBoard.text.toString(),"unknown")}
            etTrackingBuildBootloader.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildBootLoader", etTrackingBuildBootloader.text.toString(),"unknown")}
            etTrackingBuildBrand.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildBrand", etTrackingBuildBrand.text.toString(),"unknown")}
            etTrackingBuildDevice.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildDevice", etTrackingBuildDevice.text.toString(),"unknown")}
            etTrackingBuildDisplay.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildDisplay", etTrackingBuildDisplay.text.toString(),"unknown")}
            etTrackingBuildHardware.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildHardware", etTrackingBuildHardware.text.toString(),"unknown")}
            etTrackingBuildHost.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildHost", etTrackingBuildHost.text.toString(),"unknown")}
            etTrackingBuildId.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildId", etTrackingBuildId.text.toString(),"unknown")}
            etTrackingBuildManufacturer.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildManufacturer", etTrackingBuildManufacturer.text.toString(),"unknown")}
            etTrackingBuildModel.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildModel", etTrackingBuildModel.text.toString(),"unknown")}
            etTrackingBuildODMSKU.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildOdmSku", etTrackingBuildODMSKU.text.toString(),"unknown")}
            etTrackingBuildProduct.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildProduct", etTrackingBuildProduct.text.toString(),"unknown")}
            etTrackingBuildQEMU.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildQEMU", etTrackingBuildQEMU.text.toString(),"unknown")}
            etTrackingBuildRadio.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildRadioVersion", etTrackingBuildRadio.text.toString(),"unknown")}
            etTrackingBuildSKU.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildSku", etTrackingBuildSKU.text.toString(),"unknown")}
            etTrackingBuildTags.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildTags", etTrackingBuildTags.text.toString(),"unknown")}
            etTrackingBuildType.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildType", etTrackingBuildType.text.toString(),"unknown")}
            etTrackingBuildUser.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("buildUser", etTrackingBuildUser.text.toString(),"unknown")}
            etTrackingCarrierName.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("subscriptionInfoCarrierName", etTrackingCarrierName.text.toString(),"unknown")}
            etTrackingCountryIso.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("countryIso", etTrackingCountryIso.text.toString(),"unknown")}
            etTrackingOperatorName.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("operatorName", etTrackingOperatorName.text.toString(),"unknown")}
            etTrackingSerialNumber.onFocusChangeListener = OnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateDeceitStringContent("serialNumber", etTrackingSerialNumber.text.toString(),"unknown")}

            return root
        }
    }

    fun updateDeceitStringContent(name: String, newValue: String, defaultValue: String) {
        context?.contentResolver?.delete(DeceiverContentProvider.deceitStringsContentURI, "name LIKE '$name %'", null)
        val values = ContentValues()
        values.put(DeceiverContentProvider.name, "$name ${if(newValue == "") defaultValue else newValue}")
        context?.contentResolver?.insert(DeceiverContentProvider.deceitStringsContentURI, values)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<BottomNavigationView>(R.id.navView).visibility = View.VISIBLE
        _binding = null
    }

    fun updateMenus(menu: Int, binding: FragmentDeceitsBinding, ctx: Context) {
        with(binding) {
            val permissionsMenuShow = menu == 0 && llPermissionMenu.visibility != View.VISIBLE
            val modesMenuShow = menu == 1 && llModesMenu.visibility != View.VISIBLE
            llPermissionMenu.visibility = if(permissionsMenuShow) View.VISIBLE else View.GONE
            llModesMenu.visibility = if(modesMenuShow) View.VISIBLE else View.GONE
            ivDeceitMenuPermissions.setImageDrawable(ContextCompat.getDrawable(ctx, if (permissionsMenuShow) R.drawable.ic_megaphone_solid else R.drawable.ic_megaphone_outline))
            ivDeceitMenuMode.setImageDrawable(ContextCompat.getDrawable(ctx, if (modesMenuShow) R.drawable.ic_mode_solid else R.drawable.ic_mode_outline))
            tvDeceitMenuPermissions.typeface = ResourcesCompat.getFont(ctx, if(permissionsMenuShow) R.font.red_hat_mono_medium else R.font.red_hat_mono)
            tvDeceitMenuMode.typeface = ResourcesCompat.getFont(ctx, if(modesMenuShow) R.font.red_hat_mono_medium else R.font.red_hat_mono)
        }
    }

    fun getRandomAlphaNumericWithSymbolCharacter(): Char {
        return listOf(('0'..'9'), ('a'..'z'), ('A'..'Z'), ('!'..'/')).flatten().random()
    }

    fun getRandomAlphaNumericCharacter(): Char {
        return listOf(('0'..'9'), ('a'..'z'), ('A'..'Z')).flatten().random()
    }

    fun getRandomAccountType(): String {
        return listOf("google.com", "facebook.com", "xyz", "abc").random()
    }

    fun updateActivityRecognitionButtonsUI(newActivityRecognized: String, ctx: Context) {
        with(binding) {
            when(currentActivityRecognitionSelected) {
                "inVehicle" -> {
                    btnActivityRecognitionInVehicle.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionInVehicle.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "onBicycle" -> {
                    btnActivityRecognitionOnBicycle.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionOnBicycle.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "onFoot" -> {
                    btnActivityRecognitionOnFoot.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionOnFoot.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "still" -> {
                    btnActivityRecognitionStill.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionStill.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "unknown" -> {
                    btnActivityRecognitionUnknown.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionUnknown.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "tilting" -> {
                    btnActivityRecognitionTilting.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionTilting.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "walking" -> {
                    btnActivityRecognitionWalking.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionWalking.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
                "running" -> {
                    btnActivityRecognitionRunning.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1White, ctx.theme)
                    tvBtnActivityRecognitionRunning.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Green2))
                }
            }
            when(newActivityRecognized) {
                "inVehicle" -> {
                    btnActivityRecognitionInVehicle.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionInVehicle.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "onBicycle" -> {
                    btnActivityRecognitionOnBicycle.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionOnBicycle.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "onFoot" -> {
                    btnActivityRecognitionOnFoot.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionOnFoot.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "still" -> {
                    btnActivityRecognitionStill.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionStill.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "unknown" -> {
                    btnActivityRecognitionUnknown.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionUnknown.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "tilting" -> {
                    btnActivityRecognitionTilting.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionTilting.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "walking" -> {
                    btnActivityRecognitionWalking.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionWalking.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
                "running" -> {
                    btnActivityRecognitionRunning.backgroundTintList = ctx.resources.getColorStateList(R.color.theme1Green2, ctx.theme)
                    tvBtnActivityRecognitionRunning.setTextColor(ContextCompat.getColor(ctx, R.color.theme1Orange))
                }
            }
        }
    }

    companion object {
        @JvmStatic
        @SuppressLint("Range")
        fun getCallLogsStrings(ctx: Context): List<String> {
            val callLogsStrings = mutableListOf<String>()
            val callLogsCursor = ctx.contentResolver.query(DeceiverContentProvider.deceitCallLogsContentURI, null, null, null)
            if(callLogsCursor != null)
                if (callLogsCursor.moveToFirst())
                    while (!callLogsCursor.isAfterLast) {
                        callLogsStrings += callLogsCursor.getString(callLogsCursor.getColumnIndex("name"))
                        callLogsCursor.moveToNext()
                    }
            return callLogsStrings
        }

        @JvmStatic
        @SuppressLint("Range")
        fun getContactsStrings(ctx: Context): List<String> {
            val contactsStrings = mutableListOf<String>()
            val contactsCursor = ctx.contentResolver.query(DeceiverContentProvider.deceitContactsContentURI, null, null, null)
            if(contactsCursor != null)
                if (contactsCursor.moveToFirst())
                    while (!contactsCursor.isAfterLast) {
                        contactsStrings += contactsCursor.getString(contactsCursor.getColumnIndex("name"))
                        contactsCursor.moveToNext()
                    }
            return contactsStrings
        }
    }
}