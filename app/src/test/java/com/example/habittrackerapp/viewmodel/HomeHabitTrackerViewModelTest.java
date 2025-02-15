package com.example.habittrackerapp.viewmodel;

import static org.junit.Assert.assertEquals;

import com.example.habittrackerapp.core.HabitType;
import com.example.habittrackerapp.core.Resource;
import com.example.habittrackerapp.repository.FakeHabitRepository;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.presentation.viewmodel.HomeHabitTrackerViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class HomeHabitTrackerViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private FakeHabitRepository fakeRepository;
    private HomeHabitTrackerViewModel viewModel;
   private HabitEntity habit;


    @Rule
    public TestRule rxJavaRule = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            super.starting(description);
            RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
            RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            RxAndroidPlugins.reset();
            RxJavaPlugins.reset();
        }
    };

    @Before
    public void setUp() {
        fakeRepository = new FakeHabitRepository();
        viewModel = new HomeHabitTrackerViewModel(fakeRepository);
        habit = new HabitEntity("Exercise", "Run 5km", "2025-02-15", 100);
    }

    @Test
    public void whenGetAllHabitsByDateIsCalled_thenCorrectHabitsShouldBeReturned() {
        fakeRepository.insertHabit(habit);
        viewModel.loadHabitByType(HabitType.INCOMPLETE,"2025-02-15");

        LiveData<Resource<List<HabitEntity>>> liveData =  viewModel.getAllHabitsByDate;

        if (liveData.getValue().getStatus() == Resource.Status.SUCCESS) {
            List<HabitEntity> habits = liveData.getValue().getData();
            assert habits != null;
            assertEquals(1, habits.size());
        }
    }
    @Test
    public void whenGetCompletedHabitsByDateIsCalled_thenCorrectHabitsShouldBeReturned() {
        fakeRepository.insertHabit(habit);
        viewModel.loadHabitByType(HabitType.COMPLETE,"2025-02-15");

        LiveData<Resource<List<HabitEntity>>> liveData =  viewModel.getCompleteHabitsByDate;

        if (liveData.getValue().getStatus() == Resource.Status.SUCCESS) {
            List<HabitEntity> habits = liveData.getValue().getData();
            assert habits != null;
            assertEquals(100, habits.get(0).getProgress());
        }
    }

    @Test
    public void whenUpdateHabitProgressIsCalled_thenProgressShouldBeUpdated() {
        fakeRepository.insertHabit(habit);

        viewModel.updateHabitProgress(habit.getId(), 70);

        List<HabitEntity> insertedHabits = fakeRepository.getInsertedHabits();
        assertEquals(70, insertedHabits.get(0).getProgress());
    }
}



