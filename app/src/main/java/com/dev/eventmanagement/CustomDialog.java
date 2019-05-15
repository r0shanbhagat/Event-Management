package com.dev.eventmanagement;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.dev.eventmanagement.callback.OnSelectDateRange;
import com.dev.eventmanagement.callback.PickerClickListener;
import com.dev.eventmanagement.databinding.DialogSortDateBinding;
import com.dev.eventmanagement.utilities.AppUtils;

import java.util.Calendar;

public class CustomDialog extends Dialog {
    private DialogSortDateBinding binding;
    private String startDate, endDate;
    private OnSelectDateRange callback;

    public CustomDialog(Context mContext, OnSelectDateRange callback) {
        super(mContext);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_sort_date, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        binding.setDialog(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartDate:
                AppUtils.showDatePicker(getContext(), 30, new PickerClickListener() {
                    @Override
                    public void pickerClick(Calendar calendar) {
                        startDate = AppUtils.dateToString("yyyy-MM-dd", calendar.getTime());
                        binding.btnStartDate.setText(startDate);

                    }
                });
                break;
            case R.id.btnEndDate:
                AppUtils.showDatePicker(getContext(), 30, new PickerClickListener() {
                    @Override
                    public void pickerClick(Calendar calendar) {
                        endDate = AppUtils.dateToString("yyyy-MM-dd", calendar.getTime());
                        binding.btnEndDate.setText(endDate);

                    }
                });
                break;

            case R.id.btnDone:
                if (TextUtils.isEmpty(startDate)) {
                    Toast.makeText(getContext(), getContext().getString(R.string.select_start_date), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(startDate)) {
                    Toast.makeText(getContext(), getContext().getString(R.string.select_end_date), Toast.LENGTH_LONG).show();
                } else {
                    if (null != callback) {
                        callback.OnSelectDateRange(startDate, endDate);
                    }
                    dismiss();
                }

                break;
            default:
                break;
        }
        // dismiss();
    }


}