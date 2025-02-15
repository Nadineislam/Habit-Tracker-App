package com.example.habittrackerapp.data.db.habit;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "habits")
public class HabitEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    public String details;

    private int progress;


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @NonNull
    public String date;

    public HabitEntity(@NonNull String title, String details, @NonNull String date, int progress) {
        this.title = title;
        this.details = details;
        this.progress = progress;
        this.date = formatDate(date).trim();

    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = format.parse(date.trim());
            assert parsedDate != null;
            return format.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date.trim();
        }
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
}



