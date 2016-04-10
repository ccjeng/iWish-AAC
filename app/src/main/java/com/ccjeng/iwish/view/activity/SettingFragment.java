package com.ccjeng.iwish.view.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.utils.RealmMigration;

/**
 * Created by andycheng on 2016/3/27.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener  {

    private static final String RESTORE = "data_restore";
    private static final String BACKUP = "data_backup";
    private static final String CLEAR = "frequency_clear";


    private Preference mBackup;
    private Preference mRestore;
    private Preference mClear;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        mBackup = findPreference(BACKUP);
        mRestore = findPreference(RESTORE);
        mClear = findPreference(CLEAR);

        mBackup.setOnPreferenceClickListener(this);
        mRestore.setOnPreferenceClickListener(this);
        mClear.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        RealmMigration migration = new RealmMigration(getActivity());

        if (preference == mBackup){
            migration.backup();

        } else if (preference == mRestore){
            migration.restore();

        } else if (preference == mClear) {
            migration.FrequencyClear();
        }

        return false;
    }
}
