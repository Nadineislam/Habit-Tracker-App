package com.example.habittrackerapp.presentation.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittrackerapp.data.entity.HabitEntity;
import com.example.habittrackerapp.databinding.ItemHabitBinding;
import com.example.habittrackerapp.presentation.fragments.EditHabitDialog;
import com.example.habittrackerapp.presentation.listeners.OnHabitSwipeListener;

import java.util.ArrayList;
import java.util.List;

public class HabitTrackerAdapter extends RecyclerView.Adapter<HabitViewHolder> {
    private OnHabitSwipeListener listener;
    private List<HabitEntity> habits = new ArrayList<>();
    private final Activity activity;
    private final LifecycleOwner owner;

    public HabitTrackerAdapter(Activity activity, LifecycleOwner owner) {
        this.activity = activity;
        this.owner = owner;
    }

    public void setHabitProgressMap(List<HabitEntity> habits, OnHabitSwipeListener listener) {
        this.habits = habits;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHabitBinding binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HabitViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        HabitEntity entry = habits.get(position);
        holder.bind(entry, position, listener);

        holder.itemView.setOnLongClickListener(v -> {
            if (activity != null) {
                FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                new EditHabitDialog(entry.getId(), owner ).show(fragmentManager, "EditHabitDialog");
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}

class HabitViewHolder extends RecyclerView.ViewHolder {
    private final ItemHabitBinding binding;
    public OnHabitSwipeListener listener;

    HabitViewHolder(ItemHabitBinding binding, OnHabitSwipeListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    void bind(HabitEntity habit, int position, OnHabitSwipeListener listener) {
        binding.tvHabitTitle.setText(habit.getTitle());

        if (!habit.getDetails().isEmpty()) {
            binding.tvHabitDetails.setText(habit.getDetails());
            binding.tvHabitDetails.setVisibility(View.VISIBLE);
        } else {
            binding.tvHabitDetails.setVisibility(View.GONE);
        }

        binding.seekHabitProgress.setProgress(habit.getProgress());
        updateProgressColor(binding.seekHabitProgress, habit.getProgress());
        updateStatusText(habit.getProgress());

        binding.seekHabitProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int newProgress, boolean fromUser) {
                if (fromUser) {
                    if (newProgress < habit.getProgress()) {
                        binding.seekHabitProgress.setProgress(habit.getProgress());
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newProgress = seekBar.getProgress();
                updateProgressColor(seekBar, newProgress);
                updateStatusText(newProgress);
                listener.onHabitSwiped(habit.getId(), newProgress, position);
            }
        });
    }

    private void updateProgressColor(SeekBar seekBar, int progress) {
        int color = (progress < 20) ? Color.RED : (progress < 99.9) ? Color.YELLOW : Color.GREEN;
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void updateStatusText(int progress) {
        binding.tvProgressStatus.setText(progress < 20 ? "Just Started" :
                progress < 99.9 ? "In Progress" : "Completed!");
    }
}