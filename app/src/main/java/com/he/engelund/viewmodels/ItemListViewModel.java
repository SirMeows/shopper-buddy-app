package com.he.engelund.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.he.engelund.entities.ItemList;
import com.he.engelund.repositories.ItemListRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ItemListViewModel extends ViewModel {

    private LiveData<List<ItemList>> itemList;
    private ItemListRepository itemListRepository;

    public ItemListViewModel(){
        itemListRepository = new ItemListRepository();
        Observable<List<ItemList>> itemListObservable = itemListRepository.getItemLists();
        itemList = LiveDataReactiveStreams.fromPublisher(itemListObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    public LiveData<List<ItemList>> getItemList(){
        return itemList;
    }
}

