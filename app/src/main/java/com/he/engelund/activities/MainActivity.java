package com.he.engelund.activities;

import android.app.Activity;
import android.content.Intent;
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
import com.he.engelund.R;
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

        googleSignInClient = getSignInClient();
        googleSignInClient.signOut(); //TODO: Remove when sign-out button implemented (right now UserLoggedIn() is redundant)
        if (isUserLoggedIn()) {
            showMainView();
        } else {
            startSignInActivity();
        }
    }

    private void startSignInActivity() {
        signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(signInBinding.getRoot());
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

    private void showMainView() {
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
        viewModel = new ViewModelProvider(this, new ItemListViewModelFactory(GoogleSignIn.getLastSignedInAccount(this))).get(ItemListViewModel.class);
    }

    private GoogleSignInClient getSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .requestIdToken(getString(R.string.serverClientId))
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

            showMainView();


        } catch (ApiException e) {
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

        viewModel.getCompositeDisposable().clear();
    }
}