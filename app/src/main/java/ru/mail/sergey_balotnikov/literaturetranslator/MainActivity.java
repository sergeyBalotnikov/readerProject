package ru.mail.sergey_balotnikov.literaturetranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.mail.sergey_balotnikov.literaturetranslator.books.Book;
import ru.mail.sergey_balotnikov.literaturetranslator.books.BooksListAdapter;
import ru.mail.sergey_balotnikov.literaturetranslator.books.BooksViewModel;

public class MainActivity extends AppCompatActivity implements BooksListAdapter.OnBookTitleClickListener {

    private static final int REQUEST_PERMISSION = 101;
    private BooksViewModel model;
    private BooksListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rvBooksList);
        model= ViewModelProviders.of(this).get(BooksViewModel.class);
        model.fetchBooksList();
        try {
            model.getBookListLiveData().observe(this, bookList ->
                    setAdapterBookList(bookList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            adapterInit();
        }
    }

    private void adapterInit() {
        adapter = new BooksListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapterBookList(List<Book> bookList) {
        adapter.setBookList(bookList);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            adapterInit();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    adapterInit();
                }
                break;
            }
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        List<Book> books = new ArrayList<>();
        books.add(new Book("aaa","sss"));
        adapter.setBookList(books);
    }*/

    @Override
    public void onTitleClick(String path) {

    }
}
