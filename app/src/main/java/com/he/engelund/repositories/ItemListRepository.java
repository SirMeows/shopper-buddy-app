package com.he.engelund.repositories;

import com.he.engelund.api.RetrofitService;
import com.he.engelund.entities.ItemList;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ItemListRepository {

    private RetrofitService retrofitService; // TODO: Set up base URL when creating Retrofit instances

    public ItemListRepository(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    public Observable<List<ItemList>> getItemLists() {
        return retrofitService.getItemLists();
    }
}

