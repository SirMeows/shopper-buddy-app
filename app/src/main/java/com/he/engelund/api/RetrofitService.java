package com.he.engelund.api;

import com.he.engelund.entities.ItemList;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("/")
    Observable<List<ItemList>> getItemLists();
}

