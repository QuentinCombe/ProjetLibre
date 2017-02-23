package fr.univ_tours.polytech.projetlibre;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tabFooter;

    private ViewPager viewPager;

    private MapController mMapController = new MapController();

    public static final int TAB_PROFILE = 0;
    public static final int TAB_MAP = 1;
    public static final int TAB_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapController = new MapController();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.drawable.search);
        setSupportActionBar(toolbar);

        //Initializing the tabLayout
        tabFooter = (TabLayout) findViewById(R.id.footer);
        setupTabLayout(tabFooter);
        tabFooter.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing the viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Creation of our PageAdapter
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabFooter.getTabCount());



        // Adding the adapter to the page
        viewPager.setAdapter(adapter);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabFooter));
        tabFooter.addOnTabSelectedListener (new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                Log.v("OnTabSelected", "Tab numero " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() != TAB_MAP)
                {
                    mMapController.clearMapView();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public void setupTabLayout(TabLayout tabLayout) {
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.getTabAt(TAB_PROFILE).setIcon(R.drawable.user);
        tabLayout.getTabAt(TAB_MAP).setIcon(R.drawable.placeholder);
        tabLayout.getTabAt(TAB_SETTINGS).setIcon(R.drawable.settings);

    }

    public MapController getmMapController()
    {
        return mMapController;
    }
}