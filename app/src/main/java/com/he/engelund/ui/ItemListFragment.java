package com.he.engelund.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.he.engelund.adapters.ItemListAdapter;
import com.he.engelund.databinding.FragmentItemListBinding;
import com.he.engelund.viewmodels.ItemListViewModel;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ItemListFragment extends Fragment {

    private FragmentItemListBinding binding;
    private ItemListAdapter itemListAdapter;
    private ItemListViewModel itemListViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Create the adapter
        itemListAdapter = new ItemListAdapter();

        // Assign the adapter to the RecyclerView
        binding.recyclerView.setAdapter(itemListAdapter);

        // Use LinearLayoutManager as the layout manager for the RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ViewModel
        itemListViewModel = new ViewModelProvider(this).get(ItemListViewModel.class);

        // Subscribe to the data from ViewModel and update the RecyclerView's adapter when the data arrives
        compositeDisposable.add(
                itemListViewModel.getItemListsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                items -> {
                                    itemListAdapter.setItemLists(items); // assuming your adapter has a method to set the list of items
                                    itemListAdapter.notifyDataSetChanged();
                                },
                                throwable -> {
                                    // handle error here
                                }
                        )
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear(); // clear the compositeDisposable when the view is destroyed
        binding = null;
    }
}
