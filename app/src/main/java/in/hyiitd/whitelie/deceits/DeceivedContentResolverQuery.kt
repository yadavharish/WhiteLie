package `in`.hyiitd.whitelie.deceits

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge.*
import `in`.hyiitd.whitelie.DeceiverContentProvider
import `in`.hyiitd.whitelie.DeceiverModule
import `in`.hyiitd.whitelie.database.DeceitDBHelper


object DeceivedContentResolverQuery: XC_MethodHook() {
    @SuppressLint("Range", "SwitchIntDef")
    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        val uri = param.args[0] as Uri? ?: return
        if(uri.authority !in arrayListOf("call_log", "com.android.calendar", "com.android.contacts", "sms")) return

        val cursor = param.result as Cursor? ?: return
        cursor.moveToFirst()

        DeceiverModule.addLog("Requested ${when(uri.authority) {
            "call_log" -> "Call Logs"
            "com.android.calendar" -> "Calendar"
            "com.android.contacts" -> "Contacts"
            "sms" -> "Messages"
            else -> "ContentResolver.Query()"
        }} Data")

        val tableName = when(uri.authority) {
            "call_log" -> "deceived_call_log_table"
            "com.android.calendar" -> "deceived_calendar_table"
            "com.android.contacts" -> "deceived_contacts_table"
            "sms" -> "deceived_sms_table"
            else -> ""
        }

        val createTableSQLQuery = "CREATE TABLE IF NOT EXISTS $tableName ${when(uri.authority) {
            "call_log" -> "(number TEXT, formatted_number TEXT, raw_number TEXT, enriched_calling_details TEXT, number_attributes TEXT, name TEXT, type INT, call_type INT, normalized_number TEXT, timestamp BIGINT, date BIGINT, last_modified BIGINT, call_mapping_id BIGINT, duration BIGINT, subscription_id TEXT DEFAULT '8991000000000000000F', phone_account_id TEXT '8991000000000000000F', add_for_all_users INTEGER DEFAULT 1, photo_id INTEGER DEFAULT 0, data_usage INTEGER DEFAULT 0, presentation INTEGER DEFAULT 1, features INT DEFAULT 68, is_read BOOLEAN DEFAULT 1, new BOOLEAN DEFAULT 0, countryiso TEXT DEFAULT 'IN', priority INTEGER DEFAULT 0, missed_reason INTEGER DEFAULT 0, block_reason INTEGER DEFAULT 0, via_number TEXT DEFAULT '', numberlabel TEXT DEFAULT '', post_dial_digits TEXT DEFAULT '', subscription_component_name TEXT DEFAULT 'com.android.phone/com.android.services.telephony.TelephonyConnectionService', call_screening_app_name TEXT, subject TEXT, voicemail_uri TEXT, composer_photo_uri TEXT, lookup_uri TEXT, matched_number TEXT, call_screening_component_name TEXT, geocoded_location TEXT, location TEXT, is_voicemail_call INTEGER DEFAULT 0, phone_account_hidden INTEGER DEFAULT 0, atlas_call_details TEXT, call_screen_details TEXT, call_recording_details TEXT, call_verification_details TEXT, calling_networks_attributes TEXT, xatu_call_details TEXT, fides_details TEXT, dobby_calllog_details TEXT, voicemail_call_tag TEXT, transcription_state TEXT, voicemail_transcription TEXT, callee_id TEXT, spam_status TEXT, phone_account_component_name TEXT DEFAULT 'com.android.phone/com.android.services.telephony.TelephonyConnectionService', phone_account_address TEXT DEFAULT '')"
            "com.android.calendar" -> "(originalAllDay INTEGER, account_type TEXT DEFAULT 'com.google', exrule TEXT, mutators TEXT, originalInstanceTime INTEGER, allDay INTEGER, allowedReminders TEXT DEFAULT '0,1,2', rrule TEXT, canOrganizerRespond INTEGER DEFAULT 1, lastDate BIGINT, visible INTEGER DEFAULT 1, calendar_id INTEGER DEFAULT 2, hasExtendedProperties INTEGER DEFAULT 0, calendar_access_level INTEGER DEFAULT 700, selfAttendeeStatus INTEGER DEFAULT 0, allowedAvailability TEXT DEFAULT '0,1', eventColor_index TEXT DEFAULT '5', isOrganizer INTEGER DEFAULT 1,_sync_id TEXT, calendar_color_index TEXT DEFAULT '14', _id INTEGER PRIMARY KEY AUTOINCREMENT, guestsCanInviteOthers INTEGER DEFAULT 1, allowedAttendeeTypes TEXT DEFAULT '0,1,2', dtstart BIGINT, guestsCanSeeGuests INTEGER DEFAULT 1, sync_data9 TEXT, sync_data8 TEXT, exdate TEXT, sync_data7 TEXT, sync_data6 TEXT, sync_data1 TEXT, description TEXT, eventTimezone TEXT DEFAULT 'Asia/Kolkata', availability INTEGER, title TEXT, ownerAccount TEXT, sync_data5 TEXT, sync_data4 TEXT, sync_dáta3 TEXT, sync_data2 TEXT, duration TEXT, lastSynced INTEGER DEFAULT 0, guestsCanModify INTEGER DEFAULT 0, cal_sync3 TEXT, rdate TEXT, cal_sync2 TEXT, maxReminders INTEGER DEFAULT 5, isPrimary INTEGER DEFAULT 1, cal_sync1 TEXT, cal_sync10 TEXT, account_name TEXT, cal_sync7 TEXT, cal_sync6 TEXT, cal_sync5 TEXT, cal_sync4 TEXT, cal_sync9 TEXT, calendar_color INTEGER DEFAULT -6299161, cal_sync8 TEXT, dirty INTEGER DEFAULT 0, calendar_timezone TEXT DEFAULT 'Asia/Kolkata', accessLevel INTEGER DEFAULT 0, eventLocation TEXT DEFAULT '', hasAlarm INTEGER DEFAULT 0, uid2445 TEXT, deleted INTEGER DEFAULT 0, eventColor INTEGER DEFAULT -272549, organizer TEXT, eventStatus INTEGER DEFAULT 1, customAppUri TEXT, canModifyTimeZone INTEGER DEFAULT 1, eventEndTimezone TEXT, customAppPackage TEXT, original_sync_id TEXT, hasAttendeeData INTEGER DEFAULT 1, displayColor INTEGER DEFAULT -272549, dtend INTEGER, original_id TEXT, sync_data10 TEXT, calendar_displayName TEXT)"
            "com.android.contacts" -> "(last_time_contacted INTEGER DEFAULT 0, phonetic_name TEXT, custom_ringtone TEXT, contact_status_ts INTEGER, pinned INTEGER DEFAULT 0, photo_id INTEGER, photo_file_id INTEGER, contact_status_res_package TEXT, contact_chat_capability INTEGER, contact_status_icon INTEGER, display_name_alt TEXT, sort_key_alt TEXT, in_visible_group INTEGER DEFAULT 1, starred INTEGER DEFAULT 0, contact_status_label INTEGER, phonebook_label TEXT, is_user_profile INTEGER DEFAULT 0, has_phone_number INTEGER DEFAULT 1, display_name_source INTEGER DEFAULT 40, phonetic_name_style INTEGER DEFAULT 0, send_to_voicemail INTEGER DEFAULT 0, lookup TEXT, phonebook_label_alt TEXT, contact_last_updated_timestamp BIGINT, photo_uri TEXT, phonebook_bucket INTEGER, contact_status TEXT, display_name TEXT, sort_key TEXT, photo_thumb_uri TEXT, contact_presence NUMBER, in_default_directory INTEGER DEFAULT 1, times_contacted INTEGER DEFAULT 0, _id INTEGER PRIMARY KEY, name_raw_contact_id INTEGER, phonebook_bucket_alt INTEGER)"
            "sms" -> "(_id INTEGER PRIMARY KEY AUTOINCREMENT, thread_id INTEGER AUTOINCREMENT, address TEXT, person INTEGER, date BIGINT, date_sent BIGINT, protocol INTEGER DEFAULT 0, read INTEGER DEFAULT 1, status INTEGER, type INTEGER, reply_path_present BOOLEAN, subject TEXT, body TEXT, service_center TEXT, locked INTEGER DEFAULT 0, sub_id INTEGER DEFAULT 1, error_code INTEGER DEFAULT 0, creator TEXT DEFAULT 'com.google.android.apps.messaging', seen INTEGER DEFAULT 1)"
            else -> ""
        }}"

        val uriCode = when(uri.authority) {
            "call_log" -> DeceiverContentProvider.deceitCallLogsContentURI
            "com.android.calendar" -> DeceiverContentProvider.deceitCalendarEventsContentURI
            "com.android.contacts" -> DeceiverContentProvider.deceitContactsContentURI
            "sms" -> DeceiverContentProvider.deceitMessagesContentURI
            else -> Uri.EMPTY
        }

        val db = DeceitDBHelper(DeceiverModule.ctx!!, createTableSQLQuery, tableName)
        db.deleteAllData()

        val deceitCursor = DeceiverModule.ctx!!.contentResolver.query(uriCode, null, null, null, null)
        if(deceitCursor != null && deceitCursor.count != 0) {
            if (deceitCursor.moveToFirst()) {
                while (!deceitCursor.isAfterLast) {
                    val names = deceitCursor.getString(deceitCursor.getColumnIndex("name")).split("#*#*#")
                    val cv = ContentValues()
                    when(uri.authority) {
                        "call_log" -> {
                            cv.put("number", names[0])
                            cv.put("formatted_number", names[0])
                            cv.put("raw_number", names[0])
                            cv.put("enriched_calling_details", names[0])
                            cv.put("number_attributes", names[0])
                            cv.put("normalized_number", names[0])
                            cv.put("name", names[1])
                            cv.put("type", names[2].toInt())
                            cv.put("call_type", names[2].toInt())
                            cv.put("date", names[3].toInt())
                            cv.put("timestamp", names[3].toInt())
                            cv.put("last_modified", names[3].toInt())
                            cv.put("call_mapping_id", names[3].toInt())
                            cv.put("duration", names[4].toInt())
                        }
                        "com.android.calendar" -> {
                            cv.put("allDay", names[0])
                            cv.put("lastDate", names[0])
                            cv.put("dtstart", names[0])
                            cv.put("description", names[0])
                            cv.put("availability", names[0])
                            cv.put("title", names[0])
                            cv.put("ownerAccount", names[0])
                            cv.put("dtend", names[0])
                            cv.put("calendar_displayName", names[0])
                            cv.put("duration", names[0])
                            cv.put("account_name", names[0])
                            cv.put("organizer", names[0])
                        }
                        "com.android.contacts" -> {
                            cv.put("display_name_alt", names[0])
                            cv.put("sort_key_alt", names[0])
                            cv.put("phonebook_label", names[0])
                            cv.put("phonebook_label_alt", names[0])
                            cv.put("contact_last_updated_timestamp", names[0])
                            cv.put("sort_key", names[0])
                            cv.put("display_name", names[0])
                            cv.put("phonebook_bucket_alt", names[0])
                            cv.put("phonebook_bucket", names[0])
                            cv.put("name_raw_contact_id", names[0])
                            cv.put("account_name", names[0])
                            cv.put("organizer", names[0])
                        }
                        "sms" -> {
                            cv.put("address", names[1])
                            cv.put("service_center", names[1])
                            cv.put("date", names[1])
                            cv.put("date_sent", names[1])
                            cv.put("status", names[1])
                            cv.put("type", names[1])
                            cv.put("reply_path_present", names[1])
                            cv.put("body", names[1])
                        }
                    }
                    db.insertData(cv)
                    deceitCursor.moveToNext()
                }
                deceitCursor.close()
            }
            cursor.close()
            param.result = db.getAllData()

            DeceiverModule.addLog("Deceived requested ${when(uri.authority) {
                "call_log" -> "Call Logs"
                "com.android.calendar" -> "Calendar"
                "com.android.contacts" -> "Contacts"
                "sms" -> "Messages"
                else -> "ContentResolver.Query()"
            }} Data")
        }
    }
}