package ru.mail.sergey_balotnikov.literaturetranslator.repositories;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mail.sergey_balotnikov.literaturetranslator.books.Book;

public class RepoStartActivityImpl implements RepositoryStartActivity {

    public static final String LOG_TAG = "SVB";
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() - 1);

    private Book lastOpenBook;
    private List<Book> storageBooks;

    public RepoStartActivityImpl() {
        storageBooks = new ArrayList<>();
    }

    @Override
    public CompletableFuture<Book> getLastOpenBook() {
        return null;
    }

    @Override
    public CompletableFuture<List<Book>> getStorageBooks() {
        Log.d(LOG_TAG, "getStorageBooks");
        return CompletableFuture.supplyAsync(this::getBookListFromStorage,EXECUTOR);
    }

    private List<Book> getBookListFromStorage() {
        setBookList(Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.d(LOG_TAG, "getBookListFromStorage"+storageBooks.size());
        return storageBooks;
    }
    
    private void setBookList(String path){
        File currentFile = new File(path);
        if(!currentFile.isDirectory()){
            if(currentFile.getName().endsWith(".epub")||currentFile.getName().endsWith(".fb2")){
                storageBooks.add(new Book(currentFile.getName(), currentFile.getAbsolutePath()));
                Log.d(LOG_TAG, currentFile.getName());
                return;
            }
        }else {
            File[]files=currentFile.listFiles();
            for(File file:files){
                setBookList(file.getAbsolutePath());
            }
        }
    }
}
