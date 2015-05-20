package edu.washington.bbarron.quizdroid;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        // set preference change listeners
        setPrefListener(findPreference("pref_url"));
        setPrefListener(findPreference("pref_freq"));
    }

    // sets the onPreferenceChangeListener for the passed preference
    // sets the preference with the default value specified in the xml
    private static void setPrefListener(Preference preference) {
        preference.setOnPreferenceChangeListener(prefChangeListener);

        // set preferences with default values
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        prefChangeListener.onPreferenceChange(preference, sharedPref.getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener prefChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // checks if value is int, then check if it is positive
            // verifies that the frequency is greater than zero
            // if value is not an int (inputting a url) then it sets preference to the new value
            if (!(newValue instanceof Integer && (int) newValue <= 0)) {
                preference.setSummary(newValue.toString());
            }
            return true;
        }
    };
}
