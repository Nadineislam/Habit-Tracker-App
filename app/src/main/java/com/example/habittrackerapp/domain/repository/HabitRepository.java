package com.example.habittrackerapp.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.habittrackerapp.data.entity.HabitEntity;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface HabitRepository {
    Single<Long> insertHabit(HabitEntity habit);
    Observable<List<HabitEntity>> getCompletedHabitsByDate(String date);
    Observable<List<HabitEntity>> getAllHabitsByDate(String date);
    Completable updateHabitProgress(int habitId, int progress);
    LiveData<HabitEntity> getHabitById(int habitId);
    Completable updateHabit(int habitId, String title, String details);
}

