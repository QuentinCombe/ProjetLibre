package fr.univ_tours.polytech.projetlibre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.Container;

import java.io.IOException;


public class SettingsTab extends Fragment {

    private static final int SELECT_PICTURE = 1;
    Button validate;
    TextView updatePicture;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings,container,false);
        updatePicture = (TextView) view.findViewById(R.id.EditProfil);
        System.out.println(updatePicture.getText());
        validate = (Button) view.findViewById(R.id.validate);
        System.out.println(validate.getText());
        updatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        Button validate = (Button)view.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate and send the data to the BDD
                Toast toast = Toast.makeText(view.getContext(),"Settings saved !" , Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), uri);
                ImageView imageView = (ImageView) view.findViewById(R.id.profilePicture);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
