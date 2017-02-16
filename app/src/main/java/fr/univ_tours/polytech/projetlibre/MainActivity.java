package fr.univ_tours.polytech.projetlibre;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tabFooter;

    private ViewPager viewPager;

    private MapController mMapController = new MapController();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                System.out.println("ICI"+tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mMapController = new MapController();
    }

    public void setupTabLayout(TabLayout tabLayout) {
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.getTabAt(0).setIcon(R.drawable.user);
        tabLayout.getTabAt(1).setIcon(R.drawable.placeholder);
        tabLayout.getTabAt(2).setIcon(R.drawable.settings);

    }

    public MapController getmMapController()
    {
        return mMapController;
    }
}
