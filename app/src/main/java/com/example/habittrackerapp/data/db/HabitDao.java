package com.example.habittrackerapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertHabit(HabitEntity habit);

    @Query("UPDATE habits SET progress = :progress WHERE id = :habitId")
    Completable updateHabitProgress(int habitId, int progress);

    @Query("SELECT * FROM habits WHERE id = :habitId")
    LiveData<HabitEntity> getHabitById(int habitId);

    @Query("UPDATE habits SET title = :title, details = :details WHERE id = :habitId")
    Completable updateHabit(int habitId, String title, String details);


}

