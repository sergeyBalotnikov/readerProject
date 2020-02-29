package ru.mail.sergey_balotnikov.literaturetranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import java.util.List;
import ru.mail.sergey_balotnikov.literaturetranslator.books.ActivityReader;
import ru.mail.sergey_balotnikov.literaturetranslator.books.Book;
import ru.mail.sergey_balotnikov.literaturetranslator.books.BooksListAdapter;
import ru.mail.sergey_balotnikov.literaturetranslator.books.BooksViewModel;
import ru.mail.sergey_balotnikov.literaturetranslator.words.ActivityDictionary;

public class MainActivity extends AppCompatActivity
        implements BooksListAdapter.OnBookTitleClickListener {

    public static final String LOG_TAG = "SVB";
    private static final int REQUEST_PERMISSION = 101;
    private BooksViewModel model;
    private BooksListAdapter adapter;
    private RecyclerView recyclerView;
    private ImageButton dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Available Books");
        }
        dictionary=findViewById(R.id.ibDictionary);
        dictionary.setOnClickListener(view -> openDictionary());
        recyclerView = findViewById(R.id.rvBooksList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            adapterInit();
            setModel();
        }

        /*model= ViewModelProviders.of(this).get(BooksViewModel.class);
        try {
            model.getBookListLiveData().observe(this, bookList ->
                    setAdapterBookList(bookList));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
    private void setModel(){
        model= ViewModelProviders.of(this).get(BooksViewModel.class);
        try {
            model.getBookListLiveData().observe(this, bookList ->
                    setAdapterBookList(bookList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDictionary() {
        startActivity(new Intent(this, ActivityDictionary.class));
    }

    private void adapterInit() {
        adapter = new BooksListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapterBookList(List<Book> bookList) {
        adapter.setBookList(bookList);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            adapterInit();
            setModel();
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
                    setModel();
                }
                break;
            }
        }
    }

    @Override
    public void onTitleClick(String path) {
        Log.d(LOG_TAG, path);
        Intent intent = new Intent(this, ActivityReader.class);
        intent.putExtra(ActivityReader.EXTRA_PATH, path);
        startActivity(intent);
    }
}
