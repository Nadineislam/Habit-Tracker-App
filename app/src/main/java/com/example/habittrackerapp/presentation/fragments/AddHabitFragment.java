package com.example.habittrackerapp.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.habittrackerapp.R;
import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.databinding.FragmentAddHabitBinding;
import com.example.habittrackerapp.presentation.viewmodel.AddHabitViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddHabitFragment extends Fragment {
    private FragmentAddHabitBinding binding;
    private AddHabitViewModel viewModel;
    private String selectedDate = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddHabitBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddHabitViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
        setUpErrorObserver();
    }

    private void setupListeners() {
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth
        );
        binding.btnAddHabit.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString().trim();
            String details = binding.etDetails.getText().toString().trim();

            if (!title.isEmpty() && !selectedDate.isEmpty()) {
                viewModel.insertHabit(new HabitEntity(title, details, selectedDate, 0));
                Toast.makeText(requireContext(), R.string.habit_is_created_successfully, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).popBackStack();
            } else {
                Toast.makeText(requireContext(), R.string.title_and_date_required, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpErrorObserver() {
        viewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}