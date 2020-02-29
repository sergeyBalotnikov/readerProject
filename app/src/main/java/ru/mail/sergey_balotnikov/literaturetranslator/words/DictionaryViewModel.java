package ru.mail.sergey_balotnikov.literaturetranslator.words;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.RepositoryWordImpl;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.RepositoryWords;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;

public class DictionaryViewModel extends AndroidViewModel {

    private RepositoryWords repository;
    private MutableLiveData<List<WordEntity>> liveData = new MutableLiveData<>();

    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        repository=new RepositoryWordImpl(application);
        fetchWordList();
    }

    public LiveData<List<WordEntity>> getLiveData() {
        return liveData;
    }

    private void fetchWordList(){
        repository.getAllWords().thenAccept(list->liveData.postValue(list));
    }

    public void addWord(WordEntity word){
        repository.addWord(word).thenAcceptAsync(aVoid -> fetchWordList());
    }

    public void deleteWord(WordEntity word){
        repository.deleteWord(word).thenAcceptAsync(aVoid -> fetchWordList());
    }

}
