package com.he.engelund.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ItemActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO:  inflate an XML layout file with LayoutInflater.inflate(), and then set up views in that layout with data
        //This one is just to test
        TextView textView = new TextView(getActivity());
        textView.setText("Items Fragment");
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
