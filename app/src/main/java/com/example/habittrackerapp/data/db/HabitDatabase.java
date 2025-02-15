package com.example.habittrackerapp.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HabitEntity.class, HabitStreakEntity.class}, version = 3, exportSchema = false)
public abstract class HabitDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();
    public abstract HabitStreakDao habitStreakDao();
}


