package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.database.UserDB;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.User;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tabFooter;

    private ViewPager viewPager;

    private MapController mMapController = null;

    private ProfileController mProfileController = null;
    private SettingsController mSettingsController = null;

    public static final int TAB_PROFILE = 0;
    public static final int TAB_MAP = 1;
    public static final int TAB_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(toString(), "On create");


        String mail = getIntent().getStringExtra("userMail");
        String userPassword = getIntent().getStringExtra("userPassword");

        User userReceived = UserDB.getInstance().getUserFromId(mail, userPassword);

        GlobalDatas.getInstance().setCurrentUser(userReceived);

        mMapController = new MapController(this);

        mProfileController = new ProfileController(this);
        mSettingsController = new SettingsController();

        //Initializing the tabLayout
        tabFooter = (TabLayout) findViewById(R.id.footer);
        tabFooter.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing the viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Creation of our PageAdapter
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabFooter.getTabCount());

//        FloatingActionButton deconnectionButton = (FloatingActionButton) findViewById(R.id.deconnectionButton);
//        deconnectionButton.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View v)
//            {
//                if (v.getId() == R.id.deconnectionButton)
//                {
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//
//                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//
//                    editor.remove("userMail");
//                    editor.remove("userPassword");
//
//                    editor.commit();
//
//                    startActivity(intent);
//                }
//            }
//        });

        // Adding the adapter to the page
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabFooter));
        tabFooter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                Log.v("OnTabSelected", "Tab numero " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() != TAB_MAP)
                {
                    mMapController.clearMapView();
                } else if (tab.getPosition() == TAB_MAP)
                {
                    // mMapController.tryToReload();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }

    public MapController getmMapController()
    {
        return mMapController;
    }

    public ProfileController getmProfileController()
    {
        return mProfileController;
    }

    public SettingsController getmSettingsController()
    {
        return mSettingsController;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // Used after we're leaving the Unity Scene
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        mMapController.checkIfAnObjectiveWasFound();
    }

    public void updateObjectiveFound(Objective objectiveFound)
    {
        mProfileController.updateObjectiveFound(objectiveFound);
    }

}