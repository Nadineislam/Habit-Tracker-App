package com.example.habittrackerapp.presentation.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HabitViewModel extends ViewModel {
    private final HabitRepository habitRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> saveProgressLiveData = new MutableLiveData<>();


    @Inject
    public HabitViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public LiveData<List<HabitEntity>> getHabitsWithProgressForDate(String date) {
        return habitRepository.getHabitsWithProgressForDate(date);
    }


    public LiveData<List<HabitEntity>> getCompletedHabitsForDate(String date) {
        return habitRepository.getCompletedHabitsForDate(date);
    }
    public LiveData<Boolean> getSaveProgressLiveData() {
        return saveProgressLiveData;
    }

    public void saveHabitProgress(int habitId, int progress) {
        habitRepository.saveHabitProgress(habitId, progress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("HabitTracker", "Progress saved successfully");
                    saveProgressLiveData.setValue(true); // ✅ Notify success
                }, throwable -> {
                    Log.e("HabitTracker", "Error saving progress", throwable);
                    saveProgressLiveData.setValue(false); // ❌ Notify failure
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

