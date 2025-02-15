package com.example.habittrackerapp.presentation.viewmodel;


import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.data.db.HabitStreakEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
@HiltViewModel
public class AddHabitViewModel extends ViewModel {
    private final HabitRepository repository;

    @Inject
    public AddHabitViewModel(HabitRepository repository) {
        this.repository = repository;
    }

    @SuppressLint("CheckResult")
    public void insertHabit(HabitEntity habit, String date) {
        repository.insertHabit(habit)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        repository.insertHabitStreak(new HabitStreakEntity(aLong.intValue(), date, 0))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(id -> Log.d("AddHabit", "Habit Inserted: " + id),
                                        error -> Log.e("AddHabit", "Error inserting", error));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }
}
