package ru.mail.sergey_balotnikov.literaturetranslator.repositories.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WordEntity.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

    private static volatile WordDatabase instance;
    public static WordDatabase getInstance(final Application application){
        if(instance==null){
            synchronized (WordDatabase.class){
                if(instance==null){
                    instance= Room.databaseBuilder(application,
                            WordDatabase.class,
                            "db_words.db")
                            .build();
                }
            }
        }
        return instance;
    }
    public abstract WordDao wordDao();
}
