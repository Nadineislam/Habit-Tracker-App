package com.example.habittrackerapp.data.db;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "habit_streaks",
        foreignKeys = @ForeignKey(entity = HabitEntity.class,
                parentColumns = "id",
                childColumns = "habitId",
                onDelete = CASCADE))
public class HabitStreakEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(index = true)
    public int habitId;

    @NonNull
    public String date;

    public int progress;

    public HabitStreakEntity(int habitId, @NonNull String date, int progress) {
        this.habitId = habitId;
        this.date = date;
        this.progress = progress;
    }
}




