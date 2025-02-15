package com.example.habittrackerapp.presentation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.habittrackerapp.R;
import com.example.habittrackerapp.databinding.FragmentEditHabitBinding;
import com.example.habittrackerapp.presentation.viewmodel.EditHabitViewModel;

import dagger.hilt.android.AndroidEntryPoint;

import android.app.Dialog;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

@AndroidEntryPoint
public class EditHabitDialog extends DialogFragment {
    private FragmentEditHabitBinding binding;
    private EditHabitViewModel editHabitViewModel;
    private final int habitId;
    private LifecycleOwner lifeCycleOwner;

    public EditHabitDialog(int habitId, LifecycleOwner lifecycleOwner) {
        this.habitId = habitId;
        this.lifeCycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentEditHabitBinding.inflate(getLayoutInflater());
        editHabitViewModel = new ViewModelProvider(this).get(EditHabitViewModel.class);

        editHabitViewModel.getHabitById(habitId);
        getHabitByIdObserver();

        binding.btnSaveHabit.setOnClickListener(v -> saveHabitChanges());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot());

        return builder.create();
    }

    private void getHabitByIdObserver() {
        editHabitViewModel.getHabitByIdLiveData.observe(lifeCycleOwner, habitResource -> {
            switch (habitResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS: {
                    binding.progressBar.setVisibility(View.GONE);
                    if (habitResource.getData() != null) {
                        binding.etHabitTitle.setText(habitResource.getData().getTitle());
                        binding.etHabitDetails.setText(habitResource.getData().getDetails());
                    }
                    break;
                }

                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), habitResource.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void saveHabitChanges() {
        String updatedTitle = binding.etHabitTitle.getText().toString().trim();
        String updatedDetails = binding.etHabitDetails.getText().toString().trim();

        if (updatedTitle.isEmpty()) {
            Toast.makeText(getContext(), R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        editHabitViewModel.updateHabit(habitId, updatedTitle, updatedDetails);
        Toast.makeText(getContext(), R.string.habit_updated, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}


