package com.example.s364765;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.avtaleapp.R;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);


        // Lytter etter endringer i innstillinger (sms tjeneste på/av)
        findPreference("smsTjeneste").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object tjeneste) {
                boolean smsTjenesteAktivert = (Boolean) tjeneste;

                // Oppdaterer sharedPreferences
                final SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("smsTjeneste", smsTjenesteAktivert);
                editor.apply();

                // Sender broadcast signal for å oppdatere alarm
                if (smsTjenesteAktivert) {
                    Intent intent = new Intent();
                    intent.setAction("com.example.service.SMS_TJENESTE_STARTET_SIGNAL");
                    requireActivity().sendBroadcast(intent);


                    Log.d("SettingsFragment", "SMS-tjenesten er på.");
                    Log.d("SettingsFragment", "Sender egendefinert broadcast");
                } else {
                    // Stopp tjenesten
                    Intent intent = new Intent(requireContext(), MinPeriodisk.class);
                    requireContext().stopService(intent);
                    Log.d("SettingsFragment", "SMS-tjenesten er av.");
                }

                return true;
            }
        });

        // Lytter etter endringer i innstillinger (sms tidspunkt)
        findPreference("smsTidspunkt").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object tidspunkt) {
                String nyttTidspunkt = (String) tidspunkt;

                // Oppdaterer sharedPreferences
                final SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("smsTidspunkt", nyttTidspunkt);
                editor.apply();

                // Sender broadcast signal for å oppdatere alarm
                Intent intent = new Intent();
                intent.setAction("com.example.service.SMS_TJENESTE_STARTET_SIGNAL");
                requireActivity().sendBroadcast(intent);

                Log.d("SettingsFragment", "Tidspunkt for SMS endret til: " + nyttTidspunkt);

                return true;
            }
        });

        // Lytter etter endringer i innstillinger (sms innhold)
        findPreference("smsMelding").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object melding) {
                String nyMelding = (String) melding;

                // Oppdaterer sharedPreferences
                final SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("smsMelding", nyMelding);
                editor.apply();

                // Sender broadcast signal for å oppdatere alarm
                Intent intent = new Intent();
                intent.setAction("com.example.service.SMS_TJENESTE_STARTET_SIGNAL");
                requireActivity().sendBroadcast(intent);

                Log.d("SettingsFragment", "SMS innhold endret til: " + nyMelding);

                return true;
            }
        });

    }
}
