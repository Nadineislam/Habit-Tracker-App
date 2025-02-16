package com.example.habittrackerapp.di;

import android.content.Context;

import androidx.room.Room;

import com.example.habittrackerapp.data.db.HabitDatabase;
import com.example.habittrackerapp.data.db.dao.HabitDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public static HabitDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, HabitDatabase.class, "habit_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public static HabitDao provideHabitDao(HabitDatabase db) {
        return db.habitDao();
    }

}


