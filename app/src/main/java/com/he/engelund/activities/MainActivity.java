package com.he.engelund.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.android.material.tabs.TabLayoutMediator;
import com.he.engelund.adapters.ViewPagerAdapter;
import com.he.engelund.databinding.ActivityMainBinding;
import com.he.engelund.databinding.ActivitySignInBinding;
import com.he.engelund.ui.ItemFragment;
import com.he.engelund.ui.ItemListFragment;
import com.he.engelund.ui.SearchFragment;
import com.he.engelund.viewmodels.ItemListViewModel;
import com.he.engelund.viewmodels.ItemListViewModelFactory;


public class MainActivity extends FragmentActivity {

    private ActivityMainBinding mainBinding;
    private ActivitySignInBinding signInBinding;

    private ItemListViewModel viewModel;

    private static final String TAG = "MainActivity";

    private GoogleSignInClient googleSignInClient;

    ActivityResultLauncher<Intent> signInResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if preference is to be used in various Activities, use getSharedPreferences (but should save a safer storing of the info), otherwise use Preferences
        SharedPreferences sharedPref = getSharedPreferences("com.he.engelund",Context.MODE_PRIVATE);
        String idToken = sharedPref.getString("idToken", "");

        googleSignInClient = getSignInClient();
        googleSignInClient.signOut();
        if (isUserLoggedIn()) {
            // Initialize binding with layout
            mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(mainBinding.getRoot());

            setupViewPager(mainBinding.viewPager);

            new TabLayoutMediator(mainBinding.main, mainBinding.viewPager,
                    (tab, position) -> {
                        switch (position) {
                            case 0:
                                tab.setText("Lists");
                                break;
                            case 1:
                                tab.setText("Items");
                                break;
                            case 2:
                                tab.setText("Search");
                                break;
                        }
                    }
            ).attach();
            viewModel = new ViewModelProvider(this, new ItemListViewModelFactory(sharedPref, GoogleSignIn.getLastSignedInAccount(this))).get(ItemListViewModel.class);

        } else {
            signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
            setContentView(signInBinding.getRoot());
            signInResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            handleSignInResult(task);
                            // need to make sure that whatever credentials and authorization that google returns is used when retrofit is doing queries to the backend.
                        }
                    }
            );

            signInBinding.signInButton.setOnClickListener(v -> signIn());
        }
    }

    private GoogleSignInClient getSignInClient() {
        String serverClientId = "242570886832-t0g21ior6e49tqgdpk90876vta0vb85p.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .requestIdToken(serverClientId)
                .build();

        return GoogleSignIn.getClient(this, gso);
    }

    private boolean isUserLoggedIn() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        return account != null && !account.isExpired() && account.getIdToken() != null;
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInResultLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            // Store the idToken to SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("com.he.engelund",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("idToken", account.getIdToken());
            Log.w("MainActivity",account.getIdToken());
            editor.apply();



            mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(mainBinding.getRoot());

            setupViewPager(mainBinding.viewPager);

            new TabLayoutMediator(mainBinding.main, mainBinding.viewPager,
                    (tab, position) -> {
                        switch (position) {
                            case 0:
                                tab.setText("Lists");
                                break;
                            case 1:
                                tab.setText("Items");
                                break;
                            case 2:
                                tab.setText("Search");
                                break;
                        }
                    }
            ).attach();
            viewModel = new ViewModelProvider(this, new ItemListViewModelFactory(sharedPref, GoogleSignIn.getLastSignedInAccount(this))).get(ItemListViewModel.class);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason
            // GoogleSignInStatusCodes class ref for more info
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new ItemListFragment(), "Lists");
        adapter.addFragment(new ItemFragment(), "Items");
        adapter.addFragment(new SearchFragment(), "Search");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dispose of all the subscriptions when the activity is destroyed
        viewModel.getCompositeDisposable().clear();
    }
}