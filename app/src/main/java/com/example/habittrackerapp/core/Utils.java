package com.example.habittrackerapp.core;

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


}
