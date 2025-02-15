package com.example.habittrackerapp.presentation.fragments;

import static com.example.habittrackerapp.core.Utils.getCurrentDate;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.habittrackerapp.R;
import com.example.habittrackerapp.core.Utils;
import com.example.habittrackerapp.databinding.DialogCalendarBinding;
import com.example.habittrackerapp.databinding.FragmentHomeHabitTrackerBinding;
import com.example.habittrackerapp.presentation.adapter.HabitTrackerAdapter;
import com.example.habittrackerapp.presentation.listeners.OnHabitSwipeListener;
import com.example.habittrackerapp.presentation.viewmodel.HomeHabitTrackerViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.habittrackerapp.core.OnDateSelectedListener;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeHabitTrackerFragment extends Fragment implements OnHabitSwipeListener {
    private FragmentHomeHabitTrackerBinding binding;
    private HomeHabitTrackerViewModel habitViewModel;
    private HabitTrackerAdapter habitAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeHabitTrackerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        habitViewModel = new ViewModelProvider(this).get(HomeHabitTrackerViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvDate.setText(getCurrentDate());
        setUpRecyclerView();
        setUpProgressObservers();
        setUpListeners();
        loadHabitsWithSpecificDate(getCurrentDate());

    }

    private void setUpRecyclerView() {
        habitAdapter = new HabitTrackerAdapter(requireActivity());
        binding.recyclerViewHabits.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHabits.setAdapter(habitAdapter);
    }

    private void setUpProgressObservers() {
        habitViewModel.saveProgressLiveData.observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()){
                case LOADING: {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                }
                case SUCCESS: {
                    binding.progressBar.setVisibility(View.GONE);
                    loadHabitsWithSpecificDate(binding.tvDate.getText().toString());
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
        binding.fabAddHabit.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeHabitTrackerFragment_to_addHabitFragment)
        );

        binding.btnCalendar.setOnClickListener(v -> selectDateForHabits());

        binding.btnHistory.setOnClickListener(v -> getCompletedHabitsWithSpecificDate());
    }

    private void loadHabitsWithSpecificDate(String date) {
        habitViewModel.getAllHabitsByDate(date).observe(getViewLifecycleOwner(), habitsWithProgress -> {
            if (habitsWithProgress != null) {
                habitAdapter.setHabitProgressMap(habitsWithProgress, this);
            }
        });
    }

    private void selectDateForHabits() {
        AlertDialog dialog = createDatePickerDialog(selectedDate -> {
            binding.tvDate.setText(selectedDate);
            loadHabitsWithSpecificDate(selectedDate);
        });
        dialog.show();
    }

    private void getCompletedHabitsWithSpecificDate() {
        AlertDialog dialog = createDatePickerDialog(selectedDate -> {
            binding.tvDate.setText(selectedDate);
            habitViewModel.getCompletedHabitsWithSpecificDate(selectedDate)
                    .observe(getViewLifecycleOwner(), completedHabits -> habitAdapter.setHabitProgressMap(completedHabits, this));
        });
        dialog.show();
    }

    private AlertDialog createDatePickerDialog(OnDateSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogCalendarBinding calendarBinding = DialogCalendarBinding.inflate(getLayoutInflater());
        builder.setView(calendarBinding.getRoot());
        AlertDialog dialog = builder.create();

        calendarBinding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            listener.onDateSelected(selectedDate);
            dialog.dismiss();
        });

        return dialog;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onHabitSwiped(int habitId, int progress, int position) {
        if (progress > 0) {
            habitViewModel.updateHabitProgress(habitId, progress);
        }
    }
}
