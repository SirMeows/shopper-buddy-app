package com.he.engelund.repositories;

import com.he.engelund.api.ItemListService;
import com.he.engelund.entities.ItemList;
import com.he.engelund.utils.Constants;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemListRepository {

    private ItemListService itemListService;

    public ItemListRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_LOCAL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();


        itemListService = retrofit.create(ItemListService.class);
    }

    public Observable<List<ItemList>> getItemLists() {
        return itemListService.getItemLists();
    }
}

