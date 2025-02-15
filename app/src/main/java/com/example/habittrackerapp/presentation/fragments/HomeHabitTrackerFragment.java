package com.example.habittrackerapp.presentation.fragments;

import static com.example.habittrackerapp.core.Utils.createDatePickerDialog;
import static com.example.habittrackerapp.core.Utils.getCurrentDate;

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
import android.widget.Toast;

import com.example.habittrackerapp.R;
import com.example.habittrackerapp.core.HabitType;
import com.example.habittrackerapp.core.Utils;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.databinding.DialogCalendarBinding;
import com.example.habittrackerapp.databinding.FragmentHomeHabitTrackerBinding;
import com.example.habittrackerapp.presentation.adapter.HabitTrackerAdapter;
import com.example.habittrackerapp.presentation.listeners.OnHabitSwipeListener;
import com.example.habittrackerapp.presentation.viewmodel.HomeHabitTrackerViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.habittrackerapp.core.OnDateSelectedListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeHabitTrackerFragment extends Fragment implements OnHabitSwipeListener, View.OnClickListener {
    private FragmentHomeHabitTrackerBinding binding;
    private HomeHabitTrackerViewModel habitViewModel;
    private HabitTrackerAdapter habitAdapter;
    private List<HabitEntity> habits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeHabitTrackerBinding.inflate(inflater, container, false);
        habitViewModel = new ViewModelProvider(this).get(HomeHabitTrackerViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        setUpRecyclerView();
        setUpObservers();
        setUpListeners();
    }

    private void initViews() {
        binding.tvDate.setText(getCurrentDate());
    }

    private void setUpRecyclerView() {
        habitAdapter = new HabitTrackerAdapter(requireActivity());
        binding.recyclerViewHabits.setAdapter(habitAdapter);
    }

    private void setUpObservers() {
        setUpProgressObservers();
        getAllHabitByDateObserver();
        getCompleteHabitByDateObserver();
    }

    private void getAllHabitByDateObserver() {
        habitViewModel.getAllHabitsByDate.observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                    break;
                }
                case SUCCESS: {
                    binding.progressBar.setVisibility(View.GONE);

                    if (resource.getData().isEmpty()) {
                        binding.emptyText.setVisibility(View.VISIBLE);
                    }
                    habits = resource.getData();

                    if (resource.getData() != null) {
                        habitAdapter.setHabitProgressMap(resource.getData(), this);
                    }
                    break;
                }
                case ERROR: {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyText.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.error_happened_couldn_t_load_habits, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
    }

    private void getCompleteHabitByDateObserver() {
        habitViewModel.getCompleteHabitsByDate.observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                    break;
                }
                case SUCCESS: {
                    binding.progressBar.setVisibility(View.GONE);

                    if (resource.getData().isEmpty()) {
                        binding.emptyText.setVisibility(View.VISIBLE);
                        binding.emptyText.setText(R.string.no_completed_habits);
                    }
                    habits = resource.getData();

                    if (resource.getData() != null) {
                        habitAdapter.setHabitProgressMap(resource.getData(), this);
                    }
                    break;
                }
                case ERROR: {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyText.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.error_happened_couldn_t_load_habits, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
    }

    private void setUpProgressObservers() {
        habitViewModel.saveProgressLiveData.observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                }
                case SUCCESS: {
                    binding.progressBar.setVisibility(View.GONE);
                    habitViewModel.loadHabitByType(HabitType.INCOMPLETE, binding.tvDate.getText().toString());
                    break;
                }
                case ERROR: {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.couldn_t_save_the_changes_happened_in_the_progress, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
    }

    private void setUpListeners() {
        binding.fabAddHabit.setOnClickListener(this);
        binding.btnCalendar.setOnClickListener(this);
        binding.btnHistory.setOnClickListener(this);
    }

    private void getHabitsWithSpecificDate(HabitType type) {
        AlertDialog dialog = createDatePickerDialog(selectedDate -> {
            binding.tvDate.setText(selectedDate);
            habitViewModel.loadHabitByType(type, selectedDate);
        }, requireContext(), getLayoutInflater());
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onHabitSwiped(int habitId, int progress, int position) {
        HabitEntity oldHabit = new HabitEntity();
        for (HabitEntity habit : habits) {
            if (habit.getId() == habitId) {
                oldHabit = habit;
            }
        }
        if (oldHabit.getProgress() != 100){
            habitViewModel.updateHabitProgress(habitId, progress);
        }
    }

    @Override
    public void onClick(@NonNull View view) {
        if (view.equals(binding.fabAddHabit)) {
            Navigation.findNavController(view).navigate(R.id.action_homeHabitTrackerFragment_to_addHabitFragment);
        } else if (view.equals(binding.btnCalendar)) {
            getHabitsWithSpecificDate(HabitType.INCOMPLETE);
        } else if (view.equals(binding.btnHistory)) {
            getHabitsWithSpecificDate(HabitType.COMPLETE);
        }
    }
}
