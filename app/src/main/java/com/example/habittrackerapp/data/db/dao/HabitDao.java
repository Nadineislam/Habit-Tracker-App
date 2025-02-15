package com.example.habittrackerapp.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.habittrackerapp.data.entity.HabitEntity;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;
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
    @Query("SELECT * FROM habits where date = :date AND progress = 100")
    LiveData<List<HabitEntity>> getCompletedHabitsByDate(String date);

    @Query("SELECT * From habits where date = :date")
    Observable<List<HabitEntity>> getAllHabitsByDate(String date);

}

