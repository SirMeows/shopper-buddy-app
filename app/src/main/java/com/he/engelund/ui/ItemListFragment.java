package com.he.engelund.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.he.engelund.adapters.ItemListAdapter;
import com.he.engelund.databinding.ItemListFragmentBinding;
import com.he.engelund.viewmodels.ItemListViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ItemListFragment extends Fragment {
    private ItemListViewModel viewModel;
    private ItemListAdapter adapter;
    private ItemListFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = ItemListFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemListViewModel.class);

        return setUpRecyclerView();
    }

    @androidx.annotation.NonNull
    private ConstraintLayout setUpRecyclerView() {
        adapter = new ItemListAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.fetchItemLists();

        Disposable disposable = viewModel.getItemListsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        itemLists -> {

                            adapter.setItemLists(itemLists);
                        },
                        throwable -> {
                            Log.e("ItemListFragment", throwable.getMessage());
                        }
                );
        viewModel.getCompositeDisposable().add(disposable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
