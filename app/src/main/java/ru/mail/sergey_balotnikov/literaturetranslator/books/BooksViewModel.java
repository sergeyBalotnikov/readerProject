package ru.mail.sergey_balotnikov.literaturetranslator.books;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executors;

import ru.mail.sergey_balotnikov.literaturetranslator.repositories.RepoStartActivityImpl;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.RepositoryStartActivity;

public class BooksViewModel extends AndroidViewModel {

    public static final String LOG_TAG = "SVB";
    private RepositoryStartActivity repository;
    private MutableLiveData<List<Book>> bookListLiveData = new MutableLiveData<>();

    public BooksViewModel(@NonNull Application application) {
        super(application);
        repository=new RepoStartActivityImpl();
    }

    public LiveData<List<Book>> getBookListLiveData() {
        Log.d(LOG_TAG, "getBookListLiveData()"+bookListLiveData.getValue().size());
        return bookListLiveData;
    }
    public void fetchBooksList(){
        Log.d(LOG_TAG, "fetchBooksList()");
        repository.getStorageBooks().thenAcceptAsync(books ->
                bookListLiveData.postValue(books), Executors.newSingleThreadExecutor());
    }
}
