package com.example.habittrackerapp.presentation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittrackerapp.R;
import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.data.db.HabitStreakEntity;
import com.example.habittrackerapp.data.db.HabitWithProgress;
import com.example.habittrackerapp.databinding.DialogCalendarBinding;
import com.example.habittrackerapp.databinding.FragmentHomeHabitTrackerBinding;
import com.example.habittrackerapp.presentation.adapter.HabitAdapter;
import com.example.habittrackerapp.presentation.viewmodel.HabitViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeHabitTrackerFragment extends Fragment implements HabitAdapter.OnHabitSwipeListener {
    private FragmentHomeHabitTrackerBinding binding;
    private HabitViewModel habitViewModel;
    private HabitAdapter habitAdapter;

    private int position;
    private int newProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeHabitTrackerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        habitAdapter = new HabitAdapter();
        binding.recyclerViewHabits.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHabits.setAdapter(habitAdapter);

        String todayDate = getCurrentDate();
        binding.tvDate.setText(todayDate);
        loadHabitsForDate(todayDate);

        binding.fabAddHabit.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeHabitTrackerFragment_to_addHabitFragment)
        );

        binding.btnCalendar.setOnClickListener(v -> showCalendarDialog());

        binding.btnHistory.setOnClickListener(v -> showHistoryDialog());

        observe();


        return view;
    }

    private void observe() {
        habitViewModel.getSaveProgressLiveData().observe(getViewLifecycleOwner(), isSaved -> {
            if (isSaved) {
                loadHabitsForDate(binding.tvDate.getText().toString());
            } else {
                Log.e("HabitTracker", "Error saving progress");
            }
        });
    }

    private void loadHabitsForDate(String date) {
        habitViewModel.getHabitsWithProgressForDate(date).observe(getViewLifecycleOwner(), habitsWithProgress -> {
            if (habitsWithProgress != null) {
                habitAdapter.setHabitProgressMap(habitsWithProgress, this);
            }
        });
    }


    private void showCalendarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogCalendarBinding calendarBinding = DialogCalendarBinding.inflate(getLayoutInflater());
        builder.setView(calendarBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        calendarBinding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            binding.tvDate.setText(selectedDate);
            loadHabitsForDate(selectedDate);
            dialog.dismiss();
        });
    }

    private void showHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogCalendarBinding calendarBinding = DialogCalendarBinding.inflate(getLayoutInflater());
        builder.setView(calendarBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        calendarBinding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);


            habitViewModel.getCompletedHabitsForDate(selectedDate).observe(getViewLifecycleOwner(), completedHabits -> {
                habitAdapter.setHabitProgressMap(completedHabits, this);
            });

            dialog.dismiss();
        });
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onHabitSwiped(int habitId, int progress, int position) {
        if (progress > 0) {
            this.position = position;
            newProgress = progress;
            habitViewModel.saveHabitProgress(habitId, progress);
        }
    }
}
