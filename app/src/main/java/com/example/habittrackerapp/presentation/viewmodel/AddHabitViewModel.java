package com.example.habittrackerapp.presentation.viewmodel;

import android.annotation.SuppressLint;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.habittrackerapp.data.entity.HabitEntity;
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
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    @Inject
    public AddHabitViewModel(HabitRepository repository) {
        this.repository = repository;
    }

    @SuppressLint("CheckResult")
    public void insertHabit(HabitEntity habit) {
        repository.insertHabit(habit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Long aLong) {

            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }
        });
    }

    private void handleError(Throwable error) {
        _errorMessage.setValue("An error occurred: " + error.getMessage());
    }
}
