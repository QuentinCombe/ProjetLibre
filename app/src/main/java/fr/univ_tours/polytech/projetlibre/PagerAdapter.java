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
            case 0:
                ProfileTab tabProfile = new ProfileTab();
                System.out.println("LALALALALA");
                return tabProfile;
            case 1:
                MapTab mapTab = new MapTab();
                return mapTab;
            case 2:
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
