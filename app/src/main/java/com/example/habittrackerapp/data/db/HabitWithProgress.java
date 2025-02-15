package com.example.habittrackerapp.data.db;

import androidx.room.Embedded;

public class HabitWithProgress {
        @Embedded
        public HabitEntity habit;

        public int progress;


        public HabitEntity getHabit() {
                return habit;
        }

        public int getProgress() {
                return progress;
        }
}
