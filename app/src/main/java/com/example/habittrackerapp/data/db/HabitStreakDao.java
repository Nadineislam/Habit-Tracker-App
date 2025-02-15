package com.example.habittrackerapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface HabitStreakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertHabitStreak(HabitStreakEntity habitStreak);

    @Query("SELECT * FROM habits where date = :date AND progress = 100")
    LiveData<List<HabitEntity>> getCompletedHabitsForDate(String date);

    @Query("SELECT * From habits where date = :date")
    LiveData<List<HabitEntity>> getHabitsWithProgressForDate(String date);


}

