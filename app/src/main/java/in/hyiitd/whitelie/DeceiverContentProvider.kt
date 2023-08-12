package `in`.hyiitd.whitelie

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class DeceiverContentProvider : ContentProvider() {
    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            deceivedAppsUriCode -> "vnd.android.cursor.dir/deceivedApps"
            deceitStringsUriCode -> "vnd.android.cursor.dir/deceitStrings"
            deceitSettingsUriCode -> "vnd.android.cursor.dir/deceitSettings"
            deceiverLogsUriCode -> "vnd.android.cursor.dir/deceiverLogs"
            deceitCallLogsUriCode -> "vnd.android.cursor.dir/deceitCallLogs"
            deceitCalendarEventsUriCode -> "vnd.android.cursor.dir/deceitCalendarEvents"
            deceitContactsUriCode -> "vnd.android.cursor.dir/deceitContacts"
            deceitMessagesUriCode -> "vnd.android.cursor.dir/deceitMessages"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return db != null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.tables = when(uri) {
            deceivedAppsContentURI -> DeceivedAppsTableName
            deceitStringsContentURI -> DeceitStringsTableName
            deceitSettingsContentURI -> DeceitSettingsTableName
            deceiverLogsContentURI -> DeceiverLogsTableName
            deceitCallLogsContentURI -> DeceitCallLogsTableName
            deceitCalendarEventsContentURI -> DeceitCalendarEventsTableName
            deceitContactsContentURI -> DeceitContactsTableName
            deceitMessagesContentURI -> DeceitMessagesTableName
            else ->  throw IllegalArgumentException("Unknown URI $uri")
        }
        when (uriMatcher.match(uri)) {
            deceivedAppsUriCode, deceitStringsUriCode, deceitSettingsUriCode, deceiverLogsUriCode, deceitCallLogsUriCode, deceitCalendarEventsUriCode, deceitContactsUriCode, deceitMessagesUriCode -> qb.projectionMap = values
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        val c = qb.query(db, projection, selection, selectionArgs, null, null, if (sortOrder == null || sortOrder === "") id else sortOrder)
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val tableName = when(uri) {
            deceivedAppsContentURI -> DeceivedAppsTableName
            deceitStringsContentURI -> DeceitStringsTableName
            deceitSettingsContentURI -> DeceitSettingsTableName
            deceiverLogsContentURI -> DeceiverLogsTableName
            deceitCallLogsContentURI -> DeceitCallLogsTableName
            deceitCalendarEventsContentURI -> DeceitCalendarEventsTableName
            deceitContactsContentURI -> DeceitContactsTableName
            deceitMessagesContentURI -> DeceitMessagesTableName
            else ->  throw SQLiteException("Failed to add a record into $uri")
        }
        val rowID = db!!.insert(tableName, "", values)
        if (rowID > 0) {
            val u = ContentUris.withAppendedId(uri, rowID)
            context!!.contentResolver.notifyChange(u, null)
            return u
        }
        throw SQLiteException("Failed to add a record into $uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val tableName = when(uri) {
            deceivedAppsContentURI -> DeceivedAppsTableName
            deceitStringsContentURI -> DeceitStringsTableName
            deceitSettingsContentURI -> DeceitSettingsTableName
            deceiverLogsContentURI -> DeceiverLogsTableName
            deceitCallLogsContentURI -> DeceitCallLogsTableName
            deceitCalendarEventsContentURI -> DeceitCalendarEventsTableName
            deceitContactsContentURI -> DeceitContactsTableName
            deceitMessagesContentURI -> DeceitMessagesTableName
            else ->  throw SQLiteException("Failed to add a record into $uri")
        }
        val count = when (uriMatcher.match(uri)) {
            deceivedAppsUriCode, deceitStringsUriCode, deceitSettingsUriCode, deceiverLogsUriCode, deceitCallLogsUriCode, deceitCalendarEventsUriCode, deceitContactsUriCode, deceitMessagesUriCode -> db!!.update(tableName, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val tableName = when(uri) {
            deceivedAppsContentURI -> DeceivedAppsTableName
            deceitStringsContentURI -> DeceitStringsTableName
            deceitSettingsContentURI -> DeceitSettingsTableName
            deceiverLogsContentURI -> DeceiverLogsTableName
            deceitCallLogsContentURI -> DeceitCallLogsTableName
            deceitCalendarEventsContentURI -> DeceitCalendarEventsTableName
            deceitContactsContentURI -> DeceitContactsTableName
            deceitMessagesContentURI -> DeceitMessagesTableName
            else ->  throw SQLiteException("Failed to add a record into $uri")
        }
        val count = when (uriMatcher.match(uri)) {
            deceivedAppsUriCode, deceitStringsUriCode, deceitSettingsUriCode, deceiverLogsUriCode, deceitCallLogsUriCode, deceitCalendarEventsUriCode, deceitContactsUriCode, deceitMessagesUriCode -> db!!.delete(tableName, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    private var db: SQLiteDatabase? = null

    private class DatabaseHelper
    constructor(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DeceivedAppsCreateTableSQL)
            db.execSQL(DeceitStringsCreateTableSQL)
            db.execSQL(DeceitSettingsCreateTableSQL)
            db.execSQL(DeceiverLogsCreateTableSQL)
            db.execSQL(DeceitCallLogsCreateTableSQL)
            db.execSQL(DeceitCalendarEventsCreateTableSQL)
            db.execSQL(DeceitContactsCreateTableSQL)
            db.execSQL(DeceitMessagesCreateTableSQL)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $DeceivedAppsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceitStringsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceitSettingsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceiverLogsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceitCallLogsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceitCalendarEventsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceitContactsTableName")
            db.execSQL("DROP TABLE IF EXISTS $DeceitMessagesTableName")
            onCreate(db)
        }
    }

    companion object {
        const val PROVIDER_NAME = "in.hyiitd.deceiver.provider"
        const val deceivedAppsURL = "content://$PROVIDER_NAME/deceivedApps"
        const val deceitStringsURL = "content://$PROVIDER_NAME/deceitStrings"
        const val deceitSettingsURL = "content://$PROVIDER_NAME/deceitSettings"
        const val deceiverLogsURL = "content://$PROVIDER_NAME/deceiverLogs"
        const val deceitCallLogsURL = "content://$PROVIDER_NAME/deceitCallLogs"
        const val deceitCalendarEventsURL = "content://$PROVIDER_NAME/deceitCalendarEvents"
        const val deceitContactsURL = "content://$PROVIDER_NAME/deceitContacts"
        const val deceitMessagesURL = "content://$PROVIDER_NAME/deceitMessages"
        val deceivedAppsContentURI = Uri.parse(deceivedAppsURL)
        val deceitStringsContentURI = Uri.parse(deceitStringsURL)
        val deceitSettingsContentURI = Uri.parse(deceitSettingsURL)
        val deceiverLogsContentURI = Uri.parse(deceiverLogsURL)
        val deceitCallLogsContentURI = Uri.parse(deceitCallLogsURL)
        val deceitCalendarEventsContentURI = Uri.parse(deceitCalendarEventsURL)
        val deceitContactsContentURI = Uri.parse(deceitContactsURL)
        val deceitMessagesContentURI = Uri.parse(deceitMessagesURL)
        const val id = "id"
        const val name = "name"
        const val deceivedAppsUriCode = 1
        const val deceitStringsUriCode = 2
        const val deceitSettingsUriCode = 3
        const val deceiverLogsUriCode = 4
        const val deceitCallLogsUriCode = 5
        const val deceitCalendarEventsUriCode = 6
        const val deceitContactsUriCode = 7
        const val deceitMessagesUriCode = 8
        var uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private val values: HashMap<String, String>? = null

        init {
            uriMatcher.addURI(PROVIDER_NAME, "deceivedApps", deceivedAppsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceivedApps/*", deceivedAppsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitStrings", deceitStringsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitStrings/*", deceitStringsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitSettings", deceitSettingsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitSettings/*", deceitSettingsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceiverLogs", deceiverLogsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceiverLogs/*", deceiverLogsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitCallLogs", deceitCallLogsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitCallLogs/*", deceitCallLogsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitCalendarEvents", deceitCalendarEventsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitCalendarEvents/*", deceitCalendarEventsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitContacts", deceitContactsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitContacts/*", deceitContactsUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitMessages", deceitMessagesUriCode)
            uriMatcher.addURI(PROVIDER_NAME, "deceitMessages/*", deceitMessagesUriCode)
        }

        const val DATABASE_NAME = "DeceivedAppsDB"
        const val DeceivedAppsTableName = "DeceivedApps"
        const val DeceitStringsTableName = "DeceitStrings"
        const val DeceitSettingsTableName = "DeceitSettings"
        const val DeceiverLogsTableName = "DeceiverLogs"
        const val DeceitCallLogsTableName = "DeceitCallLogs"
        const val DeceitCalendarEventsTableName = "DeceitCalendarEvents"
        const val DeceitContactsTableName = "DeceitContacts"
        const val DeceitMessagesTableName = "DeceitMessages"
        const val DATABASE_VERSION = 1
        const val DeceivedAppsCreateTableSQL = "CREATE TABLE $DeceivedAppsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceitStringsCreateTableSQL = "CREATE TABLE $DeceitStringsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceitSettingsCreateTableSQL = "CREATE TABLE $DeceitSettingsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceiverLogsCreateTableSQL = "CREATE TABLE $DeceiverLogsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceitCallLogsCreateTableSQL = "CREATE TABLE $DeceitCallLogsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceitCalendarEventsCreateTableSQL = "CREATE TABLE $DeceitCalendarEventsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceitContactsCreateTableSQL = "CREATE TABLE $DeceitContactsTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
        const val DeceitMessagesCreateTableSQL = "CREATE TABLE $DeceitMessagesTableName (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL);"
    }
}