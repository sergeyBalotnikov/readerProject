package ru.mail.sergey_balotnikov.literaturetranslator.repositories.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addWord(WordEntity word);

    @Query("SELECT * FROM word_entity")
    List<WordEntity> getAllWord();

    @Query("SELECT * FROM word_entity WHERE id=:id")
    WordEntity getWordById(long id);

    @Delete
    void delete(WordEntity word);

}
