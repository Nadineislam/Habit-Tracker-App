package com.example.habittrackerapp.data.db.habitStreak;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import io.reactivex.Single;

@Dao
public interface HabitStreakDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertHabitStreak(HabitStreakEntity habitStreak);
}

