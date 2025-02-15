package com.example.habittrackerapp.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.habittrackerapp.data.db.HabitDao;
import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.data.db.HabitStreakDao;
import com.example.habittrackerapp.data.db.HabitStreakEntity;
import com.example.habittrackerapp.data.db.HabitWithProgress;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    public LiveData<List<HabitEntity>> getCompletedHabitsForDate(String date) {
        return habitStreakDao.getCompletedHabitsForDate(date);
    }

    @Override
    public LiveData<List<HabitEntity>> getHabitsWithProgressForDate(String date) {
        return habitStreakDao.getHabitsWithProgressForDate(date);
    }
@Override
    public Completable saveHabitProgress(int habitId, int progress) {
        return habitDao.updateHabitProgress(habitId,  progress);


    }
}

