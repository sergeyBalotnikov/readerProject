package ru.mail.sergey_balotnikov.literaturetranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.fbreader.book.Book;
import org.fbreader.book.BookLoader;
import org.fbreader.format.BookException;
import org.fbreader.text.FixedPosition;
import org.fbreader.text.view.style.BaseStyle;

import ru.mail.sergey_balotnikov.literaturetranslator.fbreaderSamplesClass.TableOfContentsUtil;
import ru.mail.sergey_balotnikov.literaturetranslator.fbreaderSamplesClass.TextWidgetExt;

public class ActivityReader extends AppCompatActivity {

    public final static String EXTRA_PATH = "EXTRA_PATH";
    public final static int REQUEST_TABLE_OF_CONTENT = 1;
    private TextWidgetExt widget;
    private String filepath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        widget = findViewById(R.id.text_widget);
        View errorView = findViewById(R.id.error_message);
        widget.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        filepath = getIntent().getStringExtra(EXTRA_PATH);
        try {
            widget.setBook(BookLoader.fromFile(filepath, this, 1L));
            Book book = widget.controller().book;
            if (book != null) {
                widget.invalidate();
                widget.post(new Runnable() {
                    @Override public void run() {
                        widget.gotoPage(0);
                        setTitle(book.getTitle());
                    }
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.app, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                widget.searchInText(query);
                menu.findItem(R.id.menu_search).collapseActionView();
                return true;
            }
        });

        menu.findItem(R.id.menu_table_of_contents).setEnabled(TableOfContentsUtil.isAvailable(widget));
        String name = widget.colorProfile().name;
        menu.findItem(R.id.menu_color_profile_light).setChecked("defaultLight".equals(name));
        menu.findItem(R.id.menu_color_profile_dark).setChecked("defaultDark".equals(name));
        menu.findItem(R.id.menu_color_profile_dark_with_bg).setChecked("darkWithBg".equals(name));
        menu.findItem(R.id.menu_color_profile_pink).setChecked("pink".equals(name));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BaseStyle baseStyle = widget.baseStyle();

        switch (item.getItemId()) {
            case R.id.menu_table_of_contents: {
                final Intent intent = TableOfContentsUtil.intent(widget);
                if (intent != null) {
                    startActivityForResult(intent, REQUEST_TABLE_OF_CONTENT);
                }
                break;
            }
            case R.id.menu_zoom_in:
                baseStyle.fontSize.setValue(baseStyle.fontSize.getValue() + 2);
                break;
            case R.id.menu_zoom_out:
                baseStyle.fontSize.setValue(baseStyle.fontSize.getValue() - 2);
                break;
            case R.id.menu_color_profile_light:
                widget.setColorProfileName("defaultLight");
                break;
            case R.id.menu_color_profile_dark:
                widget.setColorProfileName("defaultDark");
                break;
            case R.id.menu_color_profile_dark_with_bg:
                widget.setColorProfileName("darkWithBg");
                break;
            case R.id.menu_color_profile_pink:
                widget.setColorProfileName("pink");
                break;
        }
        widget.clearTextCaches();
        widget.invalidate();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TABLE_OF_CONTENT:
                if (resultCode == RESULT_OK) {
                    int ref = data.getIntExtra(String.valueOf(TableOfContentsUtil.Key.reference), -1);
                    if (widget != null && ref != -1) {
                        widget.jumpTo(new FixedPosition(ref, 0, 0));
                    }
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
