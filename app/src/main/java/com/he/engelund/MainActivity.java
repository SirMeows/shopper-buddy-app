package com.he.engelund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;
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
import com.he.engelund.ui.ItemListFragment;
import com.he.engelund.ui.ItemFragment;
import com.he.engelund.ui.SearchFragment;


public class MainActivity extends FragmentActivity {

    private ActivityMainBinding mainBinding;
    private ActivitySignInBinding signInBinding;

    private static final String TAG = "MainActivity";

    private GoogleSignInClient googleSignInClient;

    ActivityResultLauncher<Intent> signInResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        } else {
            signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
            setContentView(signInBinding.getRoot());

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(this, gso);

            signInResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            handleSignInResult(task);
                        }
                    }
            );

            signInBinding.signInButton.setOnClickListener(v -> signIn());
        }
    }

    private boolean isUserLoggedIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null;
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInResultLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
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
}