package com.he.engelund.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ItemListFragmentBinding binding; // Binding auto generated based on xml name "item_list_fragment"

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = ItemListFragmentBinding.inflate(inflater, container, false);

        // Get ViewModel instance, bringing ItemListViewModel instance to MainActivity scope, so other fragments can observe it
        // the itemListViewModel lives until the MainActivity is destroyed, even if the fragment is replaced or removed
        // requireActivity() throws FragmentNotAttachedException if no fragment is attached to an activity, no need for null check
        viewModel = new ViewModelProvider(requireActivity()).get(ItemListViewModel.class);

        // Set up RecyclerView
        adapter = new ItemListAdapter();
        binding.recyclerView.setAdapter(adapter); // Use binding instead of findViewById()
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch the data
        viewModel.fetchItemLists();

        // Observe the data
        // Instanciating a Disposable (RxJava interface) creates an Observable stream
        Disposable disposable = viewModel.getItemListsObservable()
                .observeOn(AndroidSchedulers.mainThread())  // make sure you observe on the main thread
                .subscribe(
                        itemLists -> {
                            // Update the adapter's data
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
        // Avoid memory leaks by setting the binding to null
        binding = null;
    }
}
