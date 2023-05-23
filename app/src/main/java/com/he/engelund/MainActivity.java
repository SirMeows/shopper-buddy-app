package com.he.engelund;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.he.engelund.databinding.ActivityMainBinding;
import com.he.engelund.databinding.ActivitySignInBinding;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding mainBinding;
    private ActivitySignInBinding signInBinding;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;

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

            googleSignInClient = GoogleSignIn.getClient(this, gso);

            signInBinding.signInButton.setOnClickListener(v -> signIn());
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
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            setContentView(R.layout.activity_main);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

}
