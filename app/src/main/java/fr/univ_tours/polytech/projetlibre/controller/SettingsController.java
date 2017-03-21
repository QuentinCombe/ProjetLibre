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
import fr.univ_tours.polytech.projetlibre.database.UserDB;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by quent on 11/03/2017.
 */

public class SettingsController
{
    private View mRootView;
    Button validate;


    public SettingsController()
    {
    }

    public void setRootView(View rootView)
    {
        mRootView = rootView;

        final User user = GlobalDatas.getInstance().mCurrentUser;

        Log.d(toString(),"USER RECIEVED"+user.username);


        final TextView username = (TextView) rootView.findViewById(R.id.NameField);
        username.setText(user.username);

        final TextView email = (TextView) rootView.findViewById(R.id.EmailField);
        email.setText(user.mail);

        final TextView password = (TextView) rootView.findViewById(R.id.PasswordField);
        password.setText(user.password);

        final TextView confirmPass = (TextView) rootView.findViewById(R.id.confirmPass);

        validate = (Button) rootView.findViewById(R.id.validate);

        Button validate = (Button)mRootView.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(toString(),password.getText().toString()+" = ? "+user.password);
                //Validate and send the data to the BDD
                if (password.getText().toString().equals(user.password)){
                    Log.d(toString(),"On change pas le mdp"+password.getText().toString()+" = ? "+user.password);
                    UserDB.getInstance().updateUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), Integer.toString(user.idUser));
                    Toast toast = Toast.makeText(mRootView.getContext(),"Settings saved !" , Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(password.getText().toString().equals(confirmPass.getText().toString())){

                    if(isPasswordValid(password.getText().toString())){
                        UserDB.getInstance().updateUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), Integer.toString(user.idUser));
                        Toast toast = Toast.makeText(mRootView.getContext(),"Settings saved !" , Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        password.setError("Your password is not enough secure");
                    }
                }
                else{
                    Toast toast = Toast.makeText(mRootView.getContext(),"Please confirm the password" , Toast.LENGTH_SHORT);
                    password.setError("Please confirm the password");
                    toast.show();
                }

            }
        });
    }

    private boolean isPasswordValid(String password)
    {
        if(password.length()>8)
            if((password.contains("1"))||(password.contains("2"))||(password.contains("3"))||(password.contains("4"))||
                    (password.contains("5"))||(password.contains("6"))||(password.contains("7"))||(password.contains("8")
            )||(password.contains("9"))){
                return true;
            }
        return false;

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
