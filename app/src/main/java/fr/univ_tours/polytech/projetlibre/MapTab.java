package fr.univ_tours.polytech.projetlibre;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class MapTab extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private View mRootView;

    private MapController mMapController = null;

    private ArrayList<Circle> mCircles = new ArrayList<>();
    private Circle mCircledSelected = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        MainActivity mainActivity = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapController = mainActivity.getmMapController();
        mMapController.setRootView(mRootView);

        // In MapController.java ??
        MapView mapView = (MapView) mRootView.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this);


        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return mRootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.v("MapTab", "onMapReady");

        mMap = googleMap;

        MapsInitializer.initialize(getActivity());

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

                if (mLastLocation != null)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));
                }


            }
        });*/

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        //if (mLastLocation != null)
        //{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));
        //}

        // We are connected, so we can create of a few circles
        if (mCircles.size() == 0)
        {
            ArrayList<CircleOptions> circleOptions = mMapController.constructCircles();

            for (CircleOptions circleOption : circleOptions)
            {
                mCircles.add(mMap.addCircle(circleOption));
            }
        }
    }

    @Override
    public void onMapClick(LatLng position)
    {
        int idCircledClicked = mMapController.handleOnMapClick(position);

        if (mCircledSelected != null)
        {
            mCircledSelected.setStrokeWidth(0.0f);
            mCircledSelected = null;
        }

        if (idCircledClicked != -1)
        {
            mCircledSelected = mCircles.get(idCircledClicked);

            mCircledSelected.setStrokeWidth(8.0f);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    public void showClue(View view)
    {
        mMapController.showClue();
    }

    public void onStart()
    {
        mGoogleApiClient.connect();

        super.onStart();
    }

    public void onStop()
    {
        mGoogleApiClient.disconnect();

        super.onStop();
    }



    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }


}
