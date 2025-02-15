package com.example.habittrackerapp.data.repository;

import androidx.lifecycle.LiveData;

import com.example.habittrackerapp.data.db.habit.HabitDao;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import com.example.habittrackerapp.data.db.habitStreak.HabitStreakDao;
import com.example.habittrackerapp.data.db.habitStreak.HabitStreakEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class HabitRepositoryImpl implements HabitRepository {
    private final HabitDao habitDao;
    private final HabitStreakDao habitStreakDao;

    @Inject
    public HabitRepositoryImpl(HabitDao habitDao, HabitStreakDao habitStreakDao) {
        this.habitDao = habitDao;
        this.habitStreakDao = habitStreakDao;
    }

    @Override
    public LiveData<HabitEntity> getHabitById(int habitId) {
        return habitDao.getHabitById(habitId);
    }

    @Override
    public Completable updateHabit(int habitId, String title, String details) {
        return habitDao.updateHabit(habitId, title, details);
    }


    @Override
    public Single<Long> insertHabit(HabitEntity habit) {
        return habitDao.insertHabit(habit);
    }

    @Override
    public Single<Long> insertHabitStreak(HabitStreakEntity habitStreak) {
        return habitStreakDao.insertHabitStreak(habitStreak);
    }

    @Override
    public LiveData<List<HabitEntity>> getCompletedHabitsByDate(String date) {
        return habitDao.getCompletedHabitsByDate(date);
    }

    @Override
    public LiveData<List<HabitEntity>> getAllHabitsByDate(String date) {
        return habitDao.getAllHabitsByDate(date);
    }

    @Override
    public Completable saveHabitProgress(int habitId, int progress) {
        return habitDao.updateHabitProgress(habitId, progress);
    }
}

