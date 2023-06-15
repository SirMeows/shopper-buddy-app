package com.he.engelund.repositories;

import android.util.Log;

import com.he.engelund.api.ItemListService;
import com.he.engelund.entities.ItemList;
import com.he.engelund.utils.Constants;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemListRepository {

    private ItemListService itemListService;

    public ItemListRepository(String idToken) {
        Log.w("ItemListRepository","" +idToken);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder builder =
                            originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + idToken);

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_LOCAL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        itemListService = retrofit.create(ItemListService.class);
    }

    public Observable<List<ItemList>> getItemLists() {
        return itemListService.getItemLists();
    }
}

