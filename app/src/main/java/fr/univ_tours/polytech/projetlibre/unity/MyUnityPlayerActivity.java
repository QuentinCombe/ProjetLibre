package fr.univ_tours.polytech.projetlibre.unity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import fr.univ_tours.polytech.projetlibre.controller.MainActivity;

/**
 * Created by Alkpo on 05/03/2017.
 */

public class MyUnityPlayerActivity extends UnityPlayerActivity
{
    @Override
    protected void onCreate(Bundle savedBundleInstance)
    {
        super.onCreate(savedBundleInstance);

    }

    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("tab", MainActivity.TAB_MAP);
        setResult(0, intent);

        finish();
    }
}
