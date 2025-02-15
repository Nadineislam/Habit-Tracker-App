package com.example.habittrackerapp.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.habittrackerapp.data.db.habit.HabitDao;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import com.example.habittrackerapp.data.db.habitStreak.HabitStreakDao;
import com.example.habittrackerapp.data.db.habitStreak.HabitStreakEntity;

@Database(entities = {HabitEntity.class, HabitStreakEntity.class}, version = 3, exportSchema = false)
public abstract class HabitDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();

    public abstract HabitStreakDao habitStreakDao();
}


