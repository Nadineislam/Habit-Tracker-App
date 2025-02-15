package com.example.habittrackerapp.presentation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittrackerapp.databinding.FragmentEditHabitBinding;
import com.example.habittrackerapp.presentation.viewmodel.EditHabitViewModel;

import dagger.hilt.android.AndroidEntryPoint;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.presentation.viewmodel.EditHabitViewModel;

@AndroidEntryPoint
public class EditHabitDialog extends DialogFragment {
    private FragmentEditHabitBinding binding;
    private EditHabitViewModel editHabitViewModel;
    private int habitId;

    public EditHabitDialog(int habitId) {
        this.habitId = habitId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentEditHabitBinding.inflate(LayoutInflater.from(getContext()));
        editHabitViewModel = new ViewModelProvider(this).get(EditHabitViewModel.class);

        loadHabitDetails();

        binding.btnSaveHabit.setOnClickListener(v -> saveHabitChanges());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot());

        return builder.create();
    }

    private void loadHabitDetails() {
        editHabitViewModel.getHabitById(habitId).observe(this, habit -> {
        if (habit != null) {
                binding.etHabitTitle.setText(habit.getTitle());
                binding.etHabitDetails.setText(habit.getDetails());
            }
        });
    }

    private void saveHabitChanges() {
        String updatedTitle = binding.etHabitTitle.getText().toString().trim();
        String updatedDetails = binding.etHabitDetails.getText().toString().trim();

        if (updatedTitle.isEmpty()) {
            Toast.makeText(getContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        editHabitViewModel.updateHabit(habitId, updatedTitle, updatedDetails);
        Toast.makeText(getContext(), "Habit updated!", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}


