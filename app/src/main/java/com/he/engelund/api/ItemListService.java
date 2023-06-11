package com.he.engelund.api;

import com.he.engelund.entities.Item;
import com.he.engelund.entities.ItemList;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ItemListService {

    @GET("api/items/")
    Observable<List<ItemList>> getItemLists();

    @GET("api/items/{id}/items-by-list")
    Observable<List<Item>> getItemsByItemList();
}

