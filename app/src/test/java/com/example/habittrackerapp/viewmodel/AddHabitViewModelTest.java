package com.example.habittrackerapp.viewmodel;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.example.habittrackerapp.repository.FakeHabitRepository;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import com.example.habittrackerapp.data.db.habitStreak.HabitStreakEntity;
import com.example.habittrackerapp.presentation.viewmodel.AddHabitViewModel;

import java.util.List;


import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class AddHabitViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private FakeHabitRepository fakeRepository;
    private AddHabitViewModel viewModel;

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
        viewModel = new AddHabitViewModel(fakeRepository);
    }

    @Test
    public void whenInsertHabitIsCalled_thenHabitAndStreakShouldBeAdded() {
        HabitEntity habit = new HabitEntity("Exercise", "Run 5km", "2025-02-15", 0);
        String date = "2025-02-15";

        viewModel.insertHabit(habit, date);

        List<HabitEntity> insertedHabits = fakeRepository.getInsertedHabits();
        List<HabitStreakEntity> insertedStreaks = fakeRepository.getInsertedStreaks();

        assertEquals(insertedHabits.get(0).getId(), insertedStreaks.get(0).getHabitId());
    }
}


