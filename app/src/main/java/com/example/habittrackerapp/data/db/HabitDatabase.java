package com.example.habittrackerapp.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.habittrackerapp.data.db.dao.HabitDao;
import com.example.habittrackerapp.data.entity.HabitEntity;

@Database(entities = {HabitEntity.class}, version = 4, exportSchema = false)
public abstract class HabitDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();
}


