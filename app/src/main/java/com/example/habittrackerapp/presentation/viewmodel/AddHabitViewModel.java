package com.example.habittrackerapp.presentation.viewmodel;

import android.annotation.SuppressLint;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import com.example.habittrackerapp.data.db.habitStreak.HabitStreakEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class AddHabitViewModel extends ViewModel {
    private final HabitRepository repository;
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    @Inject
    public AddHabitViewModel(HabitRepository repository) {
        this.repository = repository;
    }

    @SuppressLint("CheckResult")
    public void insertHabit(HabitEntity habit, String date) {
        repository.insertHabit(habit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        habitId -> insertHabitStreak(habitId.intValue(), date),
                        this::handleError
                );
    }

    @SuppressLint("CheckResult")
    private void insertHabitStreak(int habitId, String date) {
        repository.insertHabitStreak(new HabitStreakEntity(habitId, date, 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> {},
                        this::handleError
                );
    }

    private void handleError(Throwable error) {
        _errorMessage.setValue("An error occurred: " + error.getMessage());
    }
}
