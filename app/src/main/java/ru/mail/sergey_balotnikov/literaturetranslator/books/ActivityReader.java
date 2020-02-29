package ru.mail.sergey_balotnikov.literaturetranslator.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import org.fbreader.book.Book;
import org.fbreader.book.BookLoader;
import org.fbreader.format.BookException;
import ru.mail.sergey_balotnikov.literaturetranslator.R;
import ru.mail.sergey_balotnikov.literaturetranslator.fbreaderSamplesClass.TextWidgetExt;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;
import ru.mail.sergey_balotnikov.literaturetranslator.words.DictionaryViewModel;

public class ActivityReader extends AppCompatActivity implements TextWidgetExt.OnAddWordClickListener {

    public final static String EXTRA_PATH = "EXTRA_PATH";
    private TextWidgetExt widget;
    private String filepath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        widget = findViewById(R.id.text_widget);
        widget.setOnAddWordClickListener(this);
        View errorView = findViewById(R.id.error_message);
        widget.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        filepath = getIntent().getStringExtra(EXTRA_PATH);
        try {
            widget.setBook(BookLoader.fromFile(filepath, this, 1L));
            Book book = widget.controller().book;
            if (book != null) {
                widget.invalidate();
                widget.post(() -> {
                    widget.gotoPage(0);
                    setTitle(book.getTitle());
                });
            } else {
                errorView.setVisibility(View.VISIBLE);
            }
        } catch (BookException e) {
            e.printStackTrace();
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAddWordClick(WordEntity wordEntity) {
        Toast.makeText(this, "\""+wordEntity.getOriginal()+"\" added to dictionary", Toast.LENGTH_SHORT).show();
        DictionaryViewModel model = ViewModelProviders.of(this).get(DictionaryViewModel.class);
        model.addWord(wordEntity);
    }
}
