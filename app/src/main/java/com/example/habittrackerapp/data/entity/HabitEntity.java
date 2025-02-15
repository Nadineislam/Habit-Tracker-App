package com.example.habittrackerapp.data.entity;

import static com.example.habittrackerapp.core.Utils.formatDate;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class HabitEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String title;
    private String details;
    private int progress;


    @NonNull
    public String date;

    public HabitEntity(@NonNull String title, String details, @NonNull String date, int progress) {
        this.title = title;
        this.details = details;
        this.progress = progress;
        this.date = formatDate(date).trim();
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
