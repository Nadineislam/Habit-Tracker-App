package com.example.habittrackerapp.domain.repository;


import androidx.lifecycle.LiveData;

import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.data.db.HabitStreakEntity;
import com.example.habittrackerapp.data.db.HabitWithProgress;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface HabitRepository {
    Single<Long> insertHabit(HabitEntity habit);
    Single<Long> insertHabitStreak(HabitStreakEntity habitStreak);
    LiveData<List<HabitEntity>> getCompletedHabitsForDate(String date);

    LiveData<List<HabitEntity>> getHabitsWithProgressForDate(String date);
    Completable saveHabitProgress(int habitId, int progress);

    LiveData<HabitEntity> getHabitById(int habitId);
    Completable updateHabit(int habitId, String title, String details);
}

