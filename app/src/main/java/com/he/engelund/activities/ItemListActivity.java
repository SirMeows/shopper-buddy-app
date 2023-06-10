package com.he.engelund.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.he.engelund.adapters.ItemListAdapter;
import com.he.engelund.databinding.ActivityItemListBinding; // Assuming your layout file name is activity_item_list.xml
import com.he.engelund.viewmodels.ItemListViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class ItemListActivity extends AppCompatActivity {
    private ItemListViewModel viewModel;
    private ItemListAdapter adapter;
    private ActivityItemListBinding binding; // Define the binding object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the view and set it to this activity
        binding = ActivityItemListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get ViewModel instance
        viewModel = new ViewModelProvider(this).get(ItemListViewModel.class);

        // Set up RecyclerView
        adapter = new ItemListAdapter();
        binding.recyclerView.setAdapter(adapter); // Use binding instead of findViewById()

        // Fetch the data
        viewModel.fetchItemLists();

        // Observe the data
        viewModel.getItemListsObservable()
                .observeOn(AndroidSchedulers.mainThread())  // make sure you observe on the main thread
                .subscribe(
                        itemLists -> {
                            // Update the adapter's data
                            adapter.setItemLists(itemLists);
                        },
                        throwable -> {
                            // Handle the error
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dispose of the subscriptions when the activity is destroyed
        viewModel.getCompositeDisposable().dispose();
    }
}
