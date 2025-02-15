package com.example.habittrackerapp.presentation.adapter;

import android.content.Context;
import android.content.ContextWrapper;
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
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.habittrackerapp.data.db.HabitEntity;
import com.example.habittrackerapp.databinding.ItemHabitBinding;
import com.example.habittrackerapp.presentation.fragments.EditHabitDialog;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
    private OnHabitSwipeListener listener;

    private List<HabitEntity> habits = new ArrayList<>() ;

    public void setHabitProgressMap(List<HabitEntity> habits, OnHabitSwipeListener listener) {
        this.habits = habits;
        this.listener = listener;
        notifyDataSetChanged();
    }

    public interface OnHabitSwipeListener {
        void onHabitSwiped(int habitId, int progress, int position);
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHabitBinding binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HabitViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        HabitEntity entry = habits.get(position);
        holder.bind(entry, position);

        holder.itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();

            if (context instanceof AppCompatActivity) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                EditHabitDialog editDialog = new EditHabitDialog(entry.getId());
                editDialog.show(fragmentManager, "EditHabitDialog");
            } else if (context instanceof ContextWrapper && ((ContextWrapper) context).getBaseContext() instanceof AppCompatActivity) {
                FragmentManager fragmentManager = ((AppCompatActivity) ((ContextWrapper) context).getBaseContext()).getSupportFragmentManager();
                EditHabitDialog editDialog = new EditHabitDialog(entry.getId());
                editDialog.show(fragmentManager, "EditHabitDialog");
            }

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {
        private final ItemHabitBinding binding;

        HabitViewHolder(ItemHabitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(HabitEntity habit, int position) {
            binding.tvHabitTitle.setText(habit.getTitle());

            if (!habit.getDetails().isEmpty()) {
                binding.tvHabitDetails.setText(habit.getDetails());
                binding.tvHabitDetails.setVisibility(View.VISIBLE);
            } else {
                binding.tvHabitDetails.setVisibility(View.GONE);
            }

            binding.seekHabitProgress.setProgress(habit.getProgress());
            updateProgressColor(binding.seekHabitProgress, habit.getProgress(), position);
            updateStatusText(habit.getProgress(), position);

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
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int newProgress = seekBar.getProgress();
                        updateProgressColor(seekBar, newProgress, position);
                        updateStatusText(newProgress, position);
                        listener.onHabitSwiped(habit.getId(), newProgress, position);
                }
            });
        }

        private void updateProgressColor(SeekBar seekBar, int progress, int position) {
            int color = (progress < 20) ? Color.RED : (progress < 99.9) ? Color.YELLOW : Color.GREEN;
            seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);}

        private void updateStatusText(int progress, int position) {
            binding.tvProgressStatus.setText(progress < 20 ? "Just Started" :
                    progress < 99.9 ? "In Progress" : "Completed!");
        }
    }
}

