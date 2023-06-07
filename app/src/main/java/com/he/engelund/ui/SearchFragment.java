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

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create a TextView as an example
        TextView textView = new TextView(getActivity());
        textView.setText("Lists Fragment");
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

        //TODO:  inflate an XML layout file with LayoutInflater.inflate(), and then set up views in that layout with data
        return textView;
    }
}
