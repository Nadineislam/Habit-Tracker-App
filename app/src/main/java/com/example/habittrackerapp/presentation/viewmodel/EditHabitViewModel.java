package com.example.habittrackerapp.presentation.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class EditHabitViewModel extends ViewModel {
    private final HabitRepository habitRepository;
    private final MutableLiveData<Boolean> isHabitUpdatedLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public EditHabitViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public LiveData<Boolean> getIsHabitUpdatedLiveData() {
        return isHabitUpdatedLiveData;
    }
        public LiveData<HabitEntity> getHabitById(int habitId) {
        return habitRepository.getHabitById(habitId);
    }

    public void updateHabit(int habitId, String newTitle, String newDetails) {
        disposable.add(habitRepository.updateHabit(habitId, newTitle, newDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("EditHabit", "Habit updated successfully");
                    isHabitUpdatedLiveData.setValue(true); // ✅ Notify UI
                }, throwable -> Log.e("EditHabit", "Failed to update habit", throwable))
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}


//@HiltViewModel
//public class EditHabitViewModel extends ViewModel {
//
//    private final HabitRepository habitRepository;
//
//    @Inject
//    public EditHabitViewModel(HabitRepository habitRepository) {
//        this.habitRepository = habitRepository;
//    }
////define is habit updated
//    private final SingleLiveEvent<Boolean> isHabitUpdatedLiveData = new SingleLiveEvent<>();
//    /**
//     * Get a habit by its ID.
//     * @param habitId The ID of the habit to fetch.
//     * @return A LiveData object containing the habit details.
//     */
//    public LiveData<HabitEntity> getHabitById(int habitId) {
//        return habitRepository.getHabitById(habitId);
//    }
//
//    /**
//     * Update an existing habit.
//     * @param habit The habit with updated data.
//     */
//    public void updateHabit(int habitId, String newTitle, String newDetails) {
//        habitRepository.updateHabit(habitId, newTitle, newDetails)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> {
//                    Log.d("EditHabit", "Habit updated successfully");
//                    isHabitUpdatedLiveData.setValue(true); // ✅ Notify UI
//                }, throwable -> Log.e("EditHabit", "Failed to update habit", throwable));
//    }
//
//}

