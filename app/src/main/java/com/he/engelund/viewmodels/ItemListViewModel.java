package com.he.engelund.viewmodels;

import androidx.lifecycle.ViewModel;

import com.he.engelund.entities.ItemList;
import com.he.engelund.repositories.ItemListRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ItemListViewModel extends ViewModel {

    private Observable<List<ItemList>> itemListsObservable;

    private CompositeDisposable compositeDisposable;


    public ItemListViewModel(ItemListRepository itemListRepository){


        itemListsObservable = itemListRepository.getItemLists();
        compositeDisposable = new CompositeDisposable();
    }

    public Observable<List<ItemList>> getItemListsObservable() {
        return itemListsObservable;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear(); // clear disposables when ViewModel is cleared
    }

    public void fetchItemLists() {
        compositeDisposable.add(
                itemListsObservable.subscribe(
                        // onNext
                        itemLists -> {
                            // process the itemLists here
                        },
                        // onError
                        throwable -> {
                            // handle error here
                        }
                )
        );
    }
}

