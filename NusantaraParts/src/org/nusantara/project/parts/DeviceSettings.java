package org.nusantara.project.parts;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SELinux;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import org.nusantara.project.parts.fps.FPSInfoService;
import org.nusantara.project.parts.kcal.KcalSettingsActivity;
import org.nusantara.project.parts.preferences.SecureSettingListPreference;
import org.nusantara.project.parts.preferences.SecureSettingSwitchPreference;
import org.nusantara.project.parts.preferences.VibrationSeekBarPreference;
import org.nusantara.project.parts.preferences.CustomSeekBarPreference;

import org.nusantara.project.parts.su.SuShell;
import org.nusantara.project.parts.su.SuTask;
import android.util.Log;

import org.nusantara.project.parts.R;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener, Controller {

    private static final String TAG = "DeviceSettings";

    private static Context mContext;
    private SwitchPreference mSelinuxMode;
    private SwitchPreference mSelinuxPersistence;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.nusantara_project_main, rootKey);
        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        PreferenceScreen prefScreen = getPreferenceScreen();

        VibrationSeekBarPreference vibrationSystemStrength = (VibrationSeekBarPreference) findPreference(PREF_VIBRATION_SYSTEM_STRENGTH);
        vibrationSystemStrength.setEnabled(FileUtils.fileWritable(VIBRATION_SYSTEM_PATH));
        vibrationSystemStrength.setOnPreferenceChangeListener(this);

        VibrationSeekBarPreference vibrationNotificationStrength = (VibrationSeekBarPreference) findPreference(PREF_VIBRATION_NOTIFICATION_STRENGTH);
        vibrationNotificationStrength.setEnabled(FileUtils.fileWritable(VIBRATION_NOTIFICATION_PATH));
        vibrationNotificationStrength.setOnPreferenceChangeListener(this);

        VibrationSeekBarPreference vibrationCallStrength = (VibrationSeekBarPreference) findPreference(PREF_VIBRATION_CALL_STRENGTH);
        vibrationCallStrength.setEnabled(FileUtils.fileWritable(VIBRATION_CALL_PATH));
        vibrationCallStrength.setOnPreferenceChangeListener(this);

        CustomSeekBarPreference headphone_gain = (CustomSeekBarPreference) findPreference(PREF_HEADPHONE_GAIN);
        if (FileUtils.fileWritable(HEADPHONE_GAIN_PATH)) {
            headphone_gain.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(headphone_gain);
        }

        CustomSeekBarPreference microphone_gain = (CustomSeekBarPreference) findPreference(PREF_MICROPHONE_GAIN);
        if (FileUtils.fileWritable(MICROPHONE_GAIN_PATH)) {
            microphone_gain.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(microphone_gain);
        }

        CustomSeekBarPreference torch_brightness = (CustomSeekBarPreference) findPreference(PREF_TORCH_BRIGHTNESS);
        if (FileUtils.fileWritable(TORCH_1_BRIGHTNESS_PATH));
          if (FileUtils.fileWritable(TORCH_2_BRIGHTNESS_PATH)) {
            torch_brightness.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(findPreference(TORCH_KEY));
        }

        SecureSettingSwitchPreference dim = (SecureSettingSwitchPreference) findPreference(PREF_BACKLIGHT_DIMMER);
        if (FileUtils.fileWritable(BACKLIGHT_DIMMER_PATH)) {
            dim.setChecked(Dimmer.isCurrentlyEnabled(this.getContext()));
            dim.setOnPreferenceChangeListener(new Dimmer(getContext()));
        } else {
            prefScreen.removePreference(dim);
        }

        SwitchPreference fpsInfo = (SwitchPreference) findPreference(PREF_KEY_FPS_INFO);
        fpsInfo.setChecked(prefs.getBoolean(PREF_KEY_FPS_INFO, false));
        fpsInfo.setOnPreferenceChangeListener(this);

        Preference kcal = findPreference(PREF_DEVICE_KCAL);
        kcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KcalSettingsActivity.class);
            startActivity(intent);
            return true;
        });

        // SELinux
        Preference selinuxCategory = findPreference(SELINUX_CATEGORY);
        Preference selinuxExp = findPreference(SELINUX_EXPLANATION);
        mSelinuxMode = (SwitchPreference) findPreference(PREF_SELINUX_MODE);
        mSelinuxMode.setChecked(SELinux.isSELinuxEnforced());

        mSelinuxPersistence =
            (SwitchPreference) findPreference(PREF_SELINUX_PERSISTENCE);
        mSelinuxPersistence.setChecked(getContext()
            .getSharedPreferences("selinux_pref", Context.MODE_PRIVATE)
            .contains(PREF_SELINUX_MODE));

        // Disabling root required switches if unrooted and letting the user know
        if (!FileUtils.isRooted(getContext())) {
            mSelinuxMode.setEnabled(false);
            mSelinuxPersistence.setEnabled(false);
            mSelinuxPersistence.setChecked(false);
            selinuxExp.setSummary(selinuxExp.getSummary() + "\n" +
                getResources().getString(R.string.selinux_unrooted_summary));
          } else {
            mSelinuxPersistence.setOnPreferenceChangeListener(this);
            mSelinuxMode.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {
            case PREF_TORCH_BRIGHTNESS:
                FileUtils.setValue(TORCH_1_BRIGHTNESS_PATH, (int) value);
                FileUtils.setValue(TORCH_2_BRIGHTNESS_PATH, (int) value);
                break;

            case PREF_VIBRATION_SYSTEM_STRENGTH:
                double VibrationSystemValue = (int) value / 100.0 * (MAX_VIBRATION - MIN_VIBRATION) + MIN_VIBRATION;
                FileUtils.setValue(VIBRATION_SYSTEM_PATH, VibrationSystemValue);
                break;

            case PREF_VIBRATION_NOTIFICATION_STRENGTH:
                double VibrationNotificationValue = (int) value / 100.0 * (MAX_VIBRATION - MIN_VIBRATION) + MIN_VIBRATION;
                FileUtils.setValue(VIBRATION_NOTIFICATION_PATH, VibrationNotificationValue);
                break;

            case PREF_VIBRATION_CALL_STRENGTH:
                double VibrationCallValue = (int) value / 100.0 * (MAX_VIBRATION - MIN_VIBRATION) + MIN_VIBRATION;
                FileUtils.setValue(VIBRATION_CALL_PATH, VibrationCallValue);
                break;

            case PREF_HEADPHONE_GAIN:
                FileUtils.setValue(HEADPHONE_GAIN_PATH, value + " " + value);
                break;

            case PREF_MICROPHONE_GAIN:
                FileUtils.setValue(MICROPHONE_GAIN_PATH, (int) value);
                break;

            case PREF_KEY_FPS_INFO:
                boolean enabled = (Boolean) value;
                Intent fpsinfo = new Intent(this.getContext(), FPSInfoService.class);
                if (enabled) {
                    this.getContext().startService(fpsinfo);
                } else {
                    this.getContext().stopService(fpsinfo);
                }
                break;

            case PREF_SELINUX_MODE:
                boolean on = (Boolean) value;
                new SwitchSelinuxTask(getActivity()).execute(on);
                setSelinuxEnabled(on, mSelinuxPersistence.isChecked());
                break;
            case PREF_SELINUX_PERSISTENCE:
                setSelinuxEnabled(mSelinuxMode.isChecked(), (Boolean) value);
                break;
        }
        return true;
    }

    private void setSelinuxEnabled(boolean status, boolean persistent) {
      SharedPreferences.Editor editor = getContext()
        .getSharedPreferences("selinux_pref", Context.MODE_PRIVATE).edit();
      if (persistent) {
        editor.putBoolean(PREF_SELINUX_MODE, status);
      } else {
        editor.remove(PREF_SELINUX_MODE);
      }
        editor.apply();
        mSelinuxMode.setChecked(status);
     }

    private class SwitchSelinuxTask extends SuTask<Boolean> {
      public SwitchSelinuxTask(Context context) {
        super(context);
      }

      @Override
      protected void sudoInBackground(Boolean... params) throws SuShell.SuDeniedException {
        if (params.length != 1) {
            Log.e(TAG, "SwitchSelinuxTask: invalid params count");
            return;
         }
         if (params[0]) {
            SuShell.runWithSuCheck("setenforce 1");
         } else {
            SuShell.runWithSuCheck("setenforce 0");
         }
      }

      @Override
      protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (!result) {
          // Did not work, so restore actual value
          setSelinuxEnabled(SELinux.isSELinuxEnforced(), mSelinuxPersistence.isChecked());
         }
      }
   }
}
