package com.he.engelund.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.he.engelund.databinding.ItemListRowBinding;
import com.he.engelund.entities.ItemList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {

    private List<ItemList> itemLists = new ArrayList<>();

    static class ItemListViewHolder extends RecyclerView.ViewHolder {
        ItemListRowBinding binding;

        ItemListViewHolder(ItemListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ItemList itemList) {
            binding.setItemList(itemList);
            binding.executePendingBindings();
        }
    }

    public void setItemLists(List<ItemList> itemLists) {
        this.itemLists = itemLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemListAdapter.ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemListRowBinding binding = ItemListRowBinding.inflate(layoutInflater, parent, false);

        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ItemListViewHolder holder, int position) {
        // Bind the data
        holder.bind(itemLists.get(position));
    }

    @Override
    public int getItemCount() { // Item here is an element of collection, not a ref to ItemList
        return itemLists.size();
    }
}
