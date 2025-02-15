package com.example.habittrackerapp.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface HabitRepository {
    Single<Long> insertHabit(HabitEntity habit);
    LiveData<List<HabitEntity>> getCompletedHabitsByDate(String date);
    LiveData<List<HabitEntity>> getAllHabitsByDate(String date);
    Completable updateHabitProgress(int habitId, int progress);

    LiveData<HabitEntity> getHabitById(int habitId);
    Completable updateHabit(int habitId, String title, String details);
}

