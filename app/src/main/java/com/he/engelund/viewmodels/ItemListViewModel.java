package com.he.engelund.viewmodels;

import androidx.lifecycle.ViewModel;

import com.he.engelund.entities.ItemList;
import com.he.engelund.repositories.ItemListRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class ItemListViewModel extends ViewModel {

    private final BehaviorSubject<List<ItemList>> itemListsSubject = BehaviorSubject.create();
    private final CompositeDisposable compositeDisposable;
    private final ItemListRepository itemListRepository;

    public ItemListViewModel(String idToken){
        this.itemListRepository = new ItemListRepository(idToken);
        this.compositeDisposable = new CompositeDisposable();
    }

    public Observable<List<ItemList>> getItemListsObservable() {
        return itemListsSubject.hide();
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
                itemListRepository.getItemLists()
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                // onNext
                                itemListsSubject::onNext,
                                // onError
                                itemListsSubject::onError
                        )
        );
    }
}
