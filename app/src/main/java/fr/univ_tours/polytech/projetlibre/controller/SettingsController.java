package fr.univ_tours.polytech.projetlibre.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by quent on 11/03/2017.
 */

public class SettingsController {
    private View mRootView;
    private User user;
    Button validate;


    public SettingsController(User user){
        this.user = user;
    }

    public void setRootView(View rootView)
    {
        mRootView = rootView;
        Log.d(toString(),"USER RECIEVED"+user.username);


        TextView username = (TextView) rootView.findViewById(R.id.NameField);
        username.setText(user.username);

        TextView email = (TextView) rootView.findViewById(R.id.EmailField);
        email.setText(user.mail);

        TextView password = (TextView) rootView.findViewById(R.id.PasswordField);
        password.setText(user.password);

        validate = (Button) rootView.findViewById(R.id.validate);

        Button validate = (Button)mRootView.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate and send the data to the BDD
                Toast toast = Toast.makeText(mRootView.getContext(),"Settings saved !" , Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mRootView.getContext().getContentResolver(), uri);
                ImageView imageView = (ImageView) mRootView.findViewById(R.id.profilePicture);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
