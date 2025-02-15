package com.example.habittrackerapp.presentation.viewmodel;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.habittrackerapp.core.Resource;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HomeHabitTrackerViewModel extends ViewModel {
    private final HabitRepository habitRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<Resource<Boolean>> _saveProgressLiveData = new MutableLiveData<>();
    public LiveData<Resource<Boolean>> saveProgressLiveData = _saveProgressLiveData;

    private final MutableLiveData<Resource<List<HabitEntity>>> _getAllHabitsByDate = new MutableLiveData<>();
    public LiveData<Resource<List<HabitEntity>>> getAllHabitsByDate = _getAllHabitsByDate;

    @Inject
    public HomeHabitTrackerViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void getAllHabitsByDate(String date) {
        _getAllHabitsByDate.postValue(Resource.loading());
        habitRepository.getAllHabitsByDate(date).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<HabitEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<HabitEntity> habitEntities) {
                _getAllHabitsByDate.postValue(Resource.success(habitEntities));
            }

            @Override
            public void onError(Throwable e) {
                _getAllHabitsByDate.postValue(Resource.error(e.getMessage(), null));
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public LiveData<List<HabitEntity>> getCompletedHabitsWithSpecificDate(String date) {
        return habitRepository.getCompletedHabitsByDate(date);
    }

    @SuppressLint("CheckResult")
    public void updateHabitProgress(int habitId, int progress) {
        _saveProgressLiveData.postValue(Resource.loading());
        habitRepository.updateHabitProgress(habitId, progress).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                _saveProgressLiveData.postValue(Resource.success(true));
            }

            @Override
            public void onError(Throwable throwable) {
                _saveProgressLiveData.postValue(Resource.error(throwable.getMessage(), false));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

