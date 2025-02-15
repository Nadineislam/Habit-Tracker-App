package com.example.habittrackerapp.viewmodel;

import static org.junit.Assert.assertEquals;

import android.annotation.SuppressLint;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.habittrackerapp.repository.FakeHabitRepository;
import com.example.habittrackerapp.data.db.habit.HabitEntity;
import com.example.habittrackerapp.presentation.viewmodel.EditHabitViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class EditHabitViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private FakeHabitRepository fakeRepository;
    private EditHabitViewModel viewModel;

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
        viewModel = new EditHabitViewModel(fakeRepository);
    }

    @SuppressLint("CheckResult")
    @Test
    public void whenUpdateHabitIsCalledWithValidData_thenHabitDetailsShouldBeUpdated() {
        HabitEntity habit = new HabitEntity("Exercise", "Run 5km", "2025-02-15", 0);
        fakeRepository.insertHabit(habit);

        viewModel.updateHabit(habit.getId(), "Updated Exercise", "Run 10km");

        List<HabitEntity> insertedHabits = fakeRepository.getInsertedHabits();
        assertEquals("Updated Exercise", insertedHabits.get(0).getTitle());
    }
}

