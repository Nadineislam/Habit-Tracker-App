package com.example.habittrackerapp.data.repository;

import androidx.lifecycle.LiveData;

import com.example.habittrackerapp.data.db.dao.HabitDao;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class HabitRepositoryImpl implements HabitRepository {
    private final HabitDao habitDao;
    @Inject
    public HabitRepositoryImpl(HabitDao habitDao) {
        this.habitDao = habitDao;
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
    public Observable<List<HabitEntity>> getCompletedHabitsByDate(String date) {
        return habitDao.getCompletedHabitsByDate(date);
    }

    @Override
    public Observable<List<HabitEntity>> getAllHabitsByDate(String date) {
        return habitDao.getAllHabitsByDate(date);
    }

    @Override
    public Completable updateHabitProgress(int habitId, int progress) {
        return habitDao.updateHabitProgress(habitId, progress);
    }
}

