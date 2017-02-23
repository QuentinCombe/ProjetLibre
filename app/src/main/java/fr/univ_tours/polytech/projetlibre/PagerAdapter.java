package fr.univ_tours.polytech.projetlibre;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by quent on 11/02/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MainActivity.TAB_PROFILE:
                ProfileTab tabProfile = new ProfileTab();
                return tabProfile;
            case MainActivity.TAB_MAP:
                MapTab mapTab = new MapTab();
                return mapTab;
            case MainActivity.TAB_SETTINGS:
                SettingsTab tabSettings = new SettingsTab();
                return tabSettings;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
