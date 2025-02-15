package com.example.habittrackerapp.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class EditHabitViewModel extends ViewModel {
    private final HabitRepository habitRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public EditHabitViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

        public LiveData<HabitEntity> getHabitById(int habitId) {
        return habitRepository.getHabitById(habitId);
    }

    public void updateHabit(int habitId, String newTitle, String newDetails) {
        disposable.add(habitRepository.updateHabit(habitId, newTitle, newDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> {

                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

