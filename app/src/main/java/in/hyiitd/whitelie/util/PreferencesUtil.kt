package `in`.hyiitd.whitelie.util

import android.content.Context
import android.content.Context.*
import android.content.SharedPreferences

internal object PreferencesUtil {
    @JvmStatic lateinit var sharedPrefs: SharedPreferences
    @JvmStatic lateinit var sharedPrefsEditor: SharedPreferences.Editor
    @JvmStatic var sharedPrefsMode = MODE_APPEND
    @JvmStatic var sharedPrefsFileName: String = "inHyiitdDeceiverSharedPrefs"

    @JvmStatic
    fun initSharedPreferences(ctx: Context) {
        sharedPrefs = ctx.getSharedPreferences(sharedPrefsFileName, sharedPrefsMode)
        sharedPrefsEditor = sharedPrefs.edit()
        sharedPrefsEditor.putString("name", "harish")
        sharedPrefsEditor.putInt("age", 23)
        sharedPrefsEditor.apply()
    }



    // Storing the key and its value as the data fetched from edittext
//    myEdit.putString("name", name.getText().toString());
//    myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
//
//// Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
//    myEdit.commit();


    //read data
    // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
//    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);

//// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
//    String s1 = sh.getString("name", "");
//    int a = sh.getInt("age", 0);
//
//// We can then use the data
//    name.setText(s1);
//    age.setText(String.valueOf(a));
}