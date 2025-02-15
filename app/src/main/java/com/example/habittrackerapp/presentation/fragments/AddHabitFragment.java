package com.example.habittrackerapp.presentation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.habittrackerapp.R;
import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.databinding.FragmentAddHabitBinding;
import com.example.habittrackerapp.presentation.viewmodel.AddHabitViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddHabitFragment extends Fragment {
    private FragmentAddHabitBinding binding;
    private AddHabitViewModel viewModel;
    private String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddHabitBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddHabitViewModel.class);

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth
        );

        binding.btnAddHabit.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString().trim();
            String details = binding.etDetails.getText().toString().trim();

            if (!title.isEmpty() && !selectedDate.isEmpty()) {
                viewModel.insertHabit(new HabitEntity(title, details, selectedDate, 0), selectedDate);
                Navigation.findNavController(v).popBackStack();
            } else {
                Toast.makeText(requireContext(), "Title and Date required!", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

}