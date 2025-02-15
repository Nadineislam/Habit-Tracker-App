package com.example.habittrackerapp.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.habittrackerapp.core.Resource;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.domain.repository.HabitRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class EditHabitViewModel extends ViewModel {
    private final HabitRepository habitRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<Resource<HabitEntity>> _getHabitByIdLiveData = new MutableLiveData<>();
    public LiveData<Resource<HabitEntity>> getHabitByIdLiveData = _getHabitByIdLiveData;

    @Inject
    public EditHabitViewModel(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

        public void getHabitById(int habitId) {
            _getHabitByIdLiveData.postValue(Resource.loading());
            habitRepository.getHabitById(habitId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HabitEntity>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HabitEntity habitEntity) {
                    _getHabitByIdLiveData.postValue(Resource.success(habitEntity));
                }

                @Override
                public void onError(Throwable e) {
                    _getHabitByIdLiveData.postValue(Resource.error(e.getMessage(),null));
                }

                @Override
                public void onComplete() {

                }
            });
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

