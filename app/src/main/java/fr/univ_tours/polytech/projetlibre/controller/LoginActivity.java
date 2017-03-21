package fr.univ_tours.polytech.projetlibre.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.database.UserDB;
import fr.univ_tours.polytech.projetlibre.model.User;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>
{

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private User user = new User();


    public String checkUser(String mail, String password)
    {

        user = UserDB.getInstance().getUserFromId(mail, password);
        if (user != null)
        {
            if (user.mail.equals(mail))
            {
                if (user.password.equals(password))
                {
                    Log.d(toString(), "Valide");
                    return "Valide";
                } else
                {
                    Log.d(toString(), "Password False");
                    return "Failed pass";
                }
            } else
            {
                Log.d(toString(), "mail False");
                return "Failed mail";
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (this.isNetworkAvailable())
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (sharedPref.contains("userMail") && sharedPref.contains("userPassword"))
            {
                String mail = sharedPref.getString("userMail", null);
                String password = sharedPref.getString("userPassword", null);

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);

                myIntent.putExtra("userMail", mail);
                myIntent.putExtra("userPassword", password);

                startActivity(myIntent);
            }
//            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//
//            editor.remove("userMail");
//            editor.remove("userPassword");
//
//            editor.commit();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        mEmailView.setHintTextColor(Color.WHITE);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setHintTextColor(Color.WHITE);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        TextView signupLink = (TextView) findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
                startActivity(intent);
            }

        });
    }

    private void populateAutoComplete()
    {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if (isNetworkAvailable())
        {
            // Reset errors.
            mEmailView.setError(null);
            mPasswordView.setError(null);

            // Store values at the time of the login attempt.
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
            {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(email))
            {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(email))
            {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel)
            {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else
            {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                if (checkUser(email, password) == "Valide")
                {
                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);

                    Log.d(toString(), "USER : " + user);

                    myIntent.putExtra("userMail", user.mail);
                    myIntent.putExtra("userPassword", user.password);

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

                    editor.putString("userMail", user.mail);
                    editor.putString("userPassword", user.password);

                    editor.commit();

                    startActivity(myIntent);
                } else
                {
                    showProgress(false);
                    if (checkUser(email, password) == "Failed mail")
                    {
                        mEmailView.setError(getString(R.string.error_incorrect_password));
                        mEmailView.requestFocus();
                    } else if (checkUser(email, password) == "Failed pass")
                    {
                        mPasswordView.setError(getString(R.string.error_incorrect_mail));
                        mPasswordView.requestFocus();
                    }
                    Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Impossible d'etablir la connexion", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmailValid(String email)
    {
        //TODO: Replace this with your own logic
        return (email.contains("@") && (email.contains(".")));
    }

    private boolean isPasswordValid(String password)
    {
        /*if(password.length()>8)
            if((password.contains("1"))||(password.contains("2"))||(password.contains("3"))||(password.contains("4"))||
                    (password.contains("5"))||(password.contains("6"))||(password.contains("7"))||(password.contains("8")
            )||(password.contains("9"))){
                return true;
            }
        return false;*/

        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    private interface ProfileQuery
    {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


}

