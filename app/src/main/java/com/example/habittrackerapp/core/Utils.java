package com.example.habittrackerapp.core;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.example.habittrackerapp.databinding.DialogCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static String formatDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = format.parse(date.trim());
            assert parsedDate != null;
            return format.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static AlertDialog createDatePickerDialog(OnDateSelectedListener listener, Context context, LayoutInflater inflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogCalendarBinding calendarBinding = DialogCalendarBinding.inflate(inflater);
        builder.setView(calendarBinding.getRoot());
        AlertDialog dialog = builder.create();

        calendarBinding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            listener.onDateSelected(selectedDate);
            dialog.dismiss();
        });
        return dialog;
    }
}
