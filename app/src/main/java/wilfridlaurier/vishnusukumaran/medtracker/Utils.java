//MedTracker project
//Author: Vishnu Sukumaran - Wilfrid Laurier University
//Utils Class to have functions to store and retrieve Shared Preferences
//
package wilfridlaurier.vishnusukumaran.medtracker;
import android.content.Context;
import android.content.SharedPreferences;

 public class Utils {

    private static final String PREFERENCES_FILE = "materialsample_settings";

    /**
     * Function to read the Shared Preferences
     * @param ctx Provide the Context from where it is called
     * @param settingName Provide the name of the settings
     * @param defaultValue Provide the default value
     * @return return the settings
     */
    protected static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    /**
     * Function to save Shared Preferences
     * @param ctx Provide the Context from where it is called
     * @param settingName Provide the name of the settings
     * @param settingValue Provide the default value
     */

    protected static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

}