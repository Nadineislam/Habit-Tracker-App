package com.example.habittrackerapp.presentation.viewmodel;

import android.annotation.SuppressLint;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HomeHabitTrackerViewModel extends ViewModel {
    private final HabitRepository habitRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> _saveProgressLiveData = new MutableLiveData<>();
    public LiveData<Boolean> saveProgressLiveData = _saveProgressLiveData;

    @Inject
    public HomeHabitTrackerViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public LiveData<List<HabitEntity>> getAllHabitsByDate(String date) {
        return habitRepository.getAllHabitsByDate(date);
    }

    public LiveData<List<HabitEntity>> getCompletedHabitsWithSpecificDate(String date) {
        return habitRepository.getCompletedHabitsByDate(date);
    }

    @SuppressLint("CheckResult")
    public void saveHabitProgress(int habitId, int progress) {
        habitRepository.saveHabitProgress(habitId, progress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> _saveProgressLiveData.setValue(true), throwable -> {
                    _saveProgressLiveData.setValue(false);
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

