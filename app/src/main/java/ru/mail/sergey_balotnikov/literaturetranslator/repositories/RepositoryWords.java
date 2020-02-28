package ru.mail.sergey_balotnikov.literaturetranslator.repositories;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import ru.mail.sergey_balotnikov.literaturetranslator.repositories.database.WordEntity;

public interface RepositoryWords {
    CompletableFuture <List<WordEntity>> getAllWords();
    CompletableFuture <WordEntity> getWordById(long id);
    CompletableFuture<Void> addWord(WordEntity wordEntity);
    CompletableFuture<Void> deleteWord(WordEntity word);
}
