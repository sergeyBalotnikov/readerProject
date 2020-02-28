package ru.mail.sergey_balotnikov.literaturetranslator.repositories;

import android.app.Application;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordDao;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordDatabase;
import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;

public class RepositoryWordImpl implements RepositoryWords {

    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()-1
    );

    private WordDao wordDao;

    public RepositoryWordImpl(Application application) {
        wordDao= WordDatabase.getInstance(application).wordDao();
    }

    @Override
    public CompletableFuture<List<WordEntity>> getAllWords() {
        return CompletableFuture.supplyAsync(()->wordDao.getAllWord(), EXECUTOR);
    }

    @Override
    public CompletableFuture<WordEntity> getWordById(long id) {
        return CompletableFuture.supplyAsync(()->wordDao.getWordById(id), EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> addWord(WordEntity wordEntity) {
        return CompletableFuture.supplyAsync(()->{
            wordDao.addWord(wordEntity);
            return null;
        }, EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> deleteWord(WordEntity word) {
        return CompletableFuture.supplyAsync(()->{
            wordDao.delete(word);
            return null;
        }, EXECUTOR);
    }
}
