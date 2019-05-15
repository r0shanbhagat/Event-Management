package com.dev.eventmanagement.utilities;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.dev.eventmanagement.callback.PickerClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The type App utils.
 */
public final class AppUtils {


    /**
     * Is email valid boolean.
     *
     * @param value the value
     * @return boolean boolean
     */
    public static boolean isEmailValid(@NonNull String value) {
        return !TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    /**
     * Show log. You can disable Logs by setting "isShowLog" value to False
     *
     * @param tagName the tag name
     * @param message the message
     */
    public static void showLog(String tagName, String message) {
        if (!TextUtils.isEmpty(message)) {
            int maxLogSize = 1000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.v(tagName, message.substring(start, end));
            }
        }

    }

    public static void showDatePicker(Context mContext, int maxDay, final PickerClickListener listener) {
        final Calendar mCurrentDate = Calendar.getInstance();
        final Calendar myCalendar = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                myCalendar.set(Calendar.YEAR, selectedYear);
                myCalendar.set(Calendar.MONTH, selectedMonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                listener.pickerClick(myCalendar);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mCurrentDate.add(Calendar.DATE, maxDay);
        datePickerDialog.getDatePicker().setMaxDate(mCurrentDate.getTimeInMillis());

       /* String futureDate;
        try {
            futureDate = DateTimeUtility.addDaysInDate(DateTimeUtility.getCurrentDateTime("yyyy-MM-dd"), maxDay, "yyyy-MM-dd");
            datePickerDialog.getDatePicker().setMaxDate(DateTimeUtility.getTimeInMilliSecondFromDate("yyyy-MM-dd", futureDate));


        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        datePickerDialog.show();
    }

    /**
     * @param mContext
     * @param listener
     */
    public static void showTimePicker(Context mContext, final PickerClickListener listener) {
        final Calendar mCurrentTime = Calendar.getInstance();
        final Calendar myCalendar = Calendar.getInstance();
        int mHourOfDay = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int mMinute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                //myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                listener.pickerClick(myCalendar);
            }
        }, mHourOfDay, mMinute, false);
        timePickerDialog.show();
    }

    /**
     * @param format
     * @param date
     * @return
     * @throws ParseException
     */
    public static String dateToString(@NonNull String format, @NonNull Date date) {
        return new SimpleDateFormat(format, Locale.US).format(date);
    }
}
