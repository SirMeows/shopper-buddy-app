package com.he.engelund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.he.engelund.databinding.ActivityMainBinding;
import com.he.engelund.databinding.ActivitySignInBinding;

public class MainActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private ActivityMainBinding mainBinding;
    private ActivitySignInBinding signInBinding;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "entering onCreate() in MainActivity");


        // Check if the user is already authenticated with Google Identity Service
        if (isUserLoggedIn()) {
            // Initialize binding with layout
            mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(mainBinding.getRoot());
            // Use bindings here...
        } else {
            // Initialize binding with layout
            signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
            setContentView(signInBinding.getRoot());
            // Rest of the code for non-authenticated users...
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signInButton = signInBinding.loginWithGoogleButton; // use the generated binding to find view

            signInButton.setOnClickListener(v -> signIn());
        }
    }

    private boolean isUserLoggedIn() {
        // Implement your logic to check if the user is logged in
        // For example, you can check if there is an active session or a saved token
        // Return true if the user is logged in, false otherwise
        // Replace this with your actual implementation
        return false;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            // Use the account information as needed (e.g., send it to the server)
            // After successful login, update the UI or navigate to the main activity
            setContentView(R.layout.activity_main);
            // Rest of the code for authenticated users...
        } else {
            // Handle sign-in failure
            Log.e(TAG, "Sign-in failed. Error code: " + result.getStatus().getStatusCode());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }
}
