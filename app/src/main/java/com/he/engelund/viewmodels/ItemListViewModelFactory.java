package com.he.engelund.viewmodels;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.rxjava3.annotations.NonNull;

public class ItemListViewModelFactory implements ViewModelProvider.Factory {


    private SharedPreferences sharedPreferences;
    private GoogleSignInAccount lastSignedInAccount;

    public ItemListViewModelFactory(SharedPreferences prefs, GoogleSignInAccount lastSignedInAccount) {

        sharedPreferences = prefs;
        this.lastSignedInAccount = lastSignedInAccount;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {


        String idToken = sharedPreferences.getString("idToken", "");

        Log.w("ItemListViewModelFactory", idToken);
        idToken = lastSignedInAccount.getIdToken();
        Log.w("ItemListViewModelFactory", "" +idToken);
        if (modelClass.isAssignableFrom(ItemListViewModel.class)) {
            return (T) new ItemListViewModel(idToken);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
