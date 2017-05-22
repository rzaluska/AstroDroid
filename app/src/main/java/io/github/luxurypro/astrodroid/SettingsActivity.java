package io.github.luxurypro.astrodroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        SharedPreferences sharedPreferences;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.my_preference_screen);
        }

        @Override
        public void onResume() {
            super.onResume();

            sharedPreferences = getPreferenceManager().getSharedPreferences();

            // we want to watch the preference values' changes
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);

            Map<String, ?> preferencesMap = sharedPreferences.getAll();
            // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
            for (Map.Entry<String, ?> preferenceEntry : preferencesMap.entrySet()) {
                Preference preference = findPreference(preferenceEntry.getKey());
                if (preference instanceof EditTextPreference)
                    preference.setSummary((CharSequence) preferenceEntry.getValue());
            }
        }

        @Override
        public void onPause() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            Preference preference = findPreference(key);
            if (preference instanceof EditTextPreference)
                preference.setSummary(((EditTextPreference) preference).getText());
        }
    }
}
