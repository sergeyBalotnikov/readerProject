package ru.mail.sergey_balotnikov.literaturetranslator.words;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import ru.mail.sergey_balotnikov.literaturetranslator.R;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;
import ru.mail.sergey_balotnikov.literaturetranslator.utils.Interpreter;

public class ActivityDictionary extends AppCompatActivity implements WordListAdapter.OnWordRemoveListener {

    private RecyclerView recyclerView;
    private DictionaryViewModel model;
    private WordListAdapter adapter;
    private Button newWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Dictionary");
        }
        newWord=findViewById(R.id.btnAddWordToDictionary);
        newWord.setOnClickListener((view)->addNewWord());
        recyclerView = findViewById(R.id.rvWordList);
        adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        model = ViewModelProviders.of(this).get(DictionaryViewModel.class);

        try {
            model.getLiveData().observe(this, list ->setAdapterList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewWord() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_word_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final EditText editText =view.findViewById(R.id.etWord);
        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            String word = editText.getText().toString();
            if(!word.equals("")){
                CompletableFuture.supplyAsync(()-> Interpreter.translatedText(word))
                        .thenAcceptAsync(s->{
                            WordEntity wordEntity = new WordEntity();
                            wordEntity.setTranslate(s);
                            wordEntity.setOriginal(word);
                            model.addWord(wordEntity);
                        }, ContextCompat.getMainExecutor(this));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setAdapterList(List<WordEntity> list){
        adapter.setWordEntityList(list);
    }

    @Override
    public void onWordRemove(WordEntity wordEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove this word?");
        builder.setMessage("\""+wordEntity.original+"\"");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            model.deleteWord(wordEntity);
        }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
