package com.example.habittrackerapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class FakeHabitRepository implements HabitRepository {
    private final List<HabitEntity> habits = new ArrayList<>();

    private final boolean failInsertHabit = false;
    private final boolean failInsertStreak = false;
    private final boolean failUpdateHabit = false;
    private boolean failSaveProgress = false;

    public void setFailSaveProgress(boolean fail) {
        this.failSaveProgress = fail;
    }

    @Override
    public Observable<HabitEntity> getHabitById(int habitId) {
        MutableLiveData<HabitEntity> liveData = new MutableLiveData<>();
        for (HabitEntity habit : habits) {
            if (habit.getId() == habitId) {
                liveData.setValue(habit);
                return Observable.just(liveData.getValue());
            }
        }
        liveData.setValue(null);
        return Observable.just(liveData.getValue());
    }

    @Override
    public Completable updateHabit(int habitId, String title, String details) {
        if (failUpdateHabit) {
            return Completable.error(new Throwable("Simulated update habit failure"));
        }
        for (HabitEntity habit : habits) {
            if (habit.getId() == habitId) {
                habit.setTitle(title);
                habit.setDetails(details);
                return Completable.complete();
            }
        }
        return Completable.error(new Throwable("Habit not found"));
    }

    @Override
    public Single<Long> insertHabit(HabitEntity habit) {
        if (failInsertHabit) {
            return Single.error(new Throwable("Simulated insert habit failure"));
        }
        habit.setId(habits.size() + 1);
        habits.add(habit);
        return Single.just((long) habit.getId());
    }

    @Override
    public Observable<List<HabitEntity>> getCompletedHabitsByDate(String date) {
        List<HabitEntity> habitsByDate = new ArrayList<>();
        for (HabitEntity habit : habits) {
            if (habit.getDate().equals(date) && habit.getProgress() == 100) {
                habitsByDate.add(habit);
            }
        }
        return Observable.just(habitsByDate);
    }

    @Override
    public Observable<List<HabitEntity>> getAllHabitsByDate(String date) {
        List<HabitEntity> habitsByDate = new ArrayList<>();
        for (HabitEntity habit : habits) {
            if (habit.getDate().equals(date)) {
                habitsByDate.add(habit);
            }
        }
        return Observable.just(habitsByDate);
    }

    @Override
    public Completable updateHabitProgress(int habitId, int progress) {
        if (failSaveProgress) {
            return Completable.error(new Throwable("Simulated save progress failure"));
        }
        for (HabitEntity habit : habits) {
            if (habit.getId() == habitId) {
                habit.setProgress(progress);
                return Completable.complete();
            }
        }
        return Completable.error(new Throwable("Habit not found"));
    }

    public List<HabitEntity> getInsertedHabits() {
        return new ArrayList<>(habits);
    }
}
