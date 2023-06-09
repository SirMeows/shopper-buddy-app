package com.he.engelund.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.he.engelund.databinding.FragmentItemListBinding;
import com.he.engelund.viewmodels.ItemListViewModel;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;


public class ItemListFragment extends Fragment {

    private FragmentItemListBinding binding;

    private ItemListViewModel itemListViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemListViewModel = new ViewModelProvider(this).get(ItemListViewModel.class);

        Disposable disposable = itemListViewModel.getItemListsObservable()
                .subscribeOn(Schedulers.io()) // subscribeOn(Schedulers.io()) instructs the source Observable to emit its items on a background thread (multi-threading)
                .observeOn(AndroidSchedulers.mainThread()) // But manipulating the UI is done on the main thread.
                .subscribe(
                        // onNext
                        itemList -> {
                            // Update the UI here when the data changes.
                            // Use the binding object to refer to views.
                            // For example, if you have a TextView with an id of text_item_list in your layout, you could do:
                            // binding.textItemList.setText(itemList.getName());
                        },
                        // onError
                        throwable -> {
                            // handle error here
                        }
                );

        itemListViewModel.getCompositeDisposable().add(disposable);

        return view;
    }

    private static class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
        private List<String> data;

        public ItemListAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
