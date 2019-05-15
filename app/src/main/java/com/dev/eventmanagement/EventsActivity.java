package com.dev.eventmanagement;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.dev.eventmanagement.callback.PickerClickListener;
import com.dev.eventmanagement.databinding.ActivityEventBinding;
import com.dev.eventmanagement.model.EventListItem;
import com.dev.eventmanagement.utilities.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EventsActivity extends AppCompatActivity {
    public static final String TAG = EventListingActivity.class.getSimpleName();
    public static final String KEY_ARGS = "args_item";
    private DatabaseReference mListItemRef;
    private ActivityEventBinding binding;
    private EventListItem eventListItem;
    private boolean isEventAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event);
        binding.setActivity(this);
        mListItemRef = FirebaseDatabase.getInstance().getReference().child("events");
        setUpToolbar();
        handleArgs();
        if (null != eventListItem) {
            binding.setViewModel(eventListItem);
            binding.btnEvent.setText(isEventAdd ? getString(R.string.createEvent) : getString(R.string.updateEvent));
        }

    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void handleArgs() {
        if (null != getIntent()) {
            eventListItem = getIntent().getParcelableExtra(KEY_ARGS);
            isEventAdd = eventListItem.getIsForAdd() == 1;
        }
    }


    /**
     * Used this method through data binding '* public required'
     */
    public void addUpdateEvent() {
        String eventName = binding.etEventName.getText().toString();
        String eventDateTime = binding.btnDateTime.getText().toString();
        if (TextUtils.isEmpty(eventName)) {
            Toast.makeText(this, getString(R.string.enter_event), Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(eventDateTime)) {
            Toast.makeText(this, getString(R.string.enter_event_date), Toast.LENGTH_LONG).show();
        } else {
            insertToDatabase(binding.getViewModel());
        }
    }

    public void showDatePicker() {
        AppUtils.showDatePicker(this, 30, new PickerClickListener() {
            @Override
            public void pickerClick(Calendar calendar) {
                showTimePicker(calendar);
            }
        });
    }

    private void showTimePicker(final Calendar dateCalender) {
        AppUtils.showTimePicker(this, new PickerClickListener() {
            @Override
            public void pickerClick(Calendar calendar) {
                String date = AppUtils.dateToString("yyyy-MM-dd", dateCalender.getTime());
                String time = AppUtils.dateToString("HH:mm:ss", calendar.getTime());
                binding.getViewModel().setEventDate(String.format("%s,%s", date, time));
            }
        });
    }

    private void insertToDatabase(EventListItem eventListItem) {
        if (null != mListItemRef) {
            String userId = eventListItem.getKey();
            if (isEventAdd) {
                userId = mListItemRef.push().getKey();
            }
            mListItemRef.child(userId)
                    .setValue(eventListItem)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String message = getString(R.string.eventUpdated);
                                if (isEventAdd) {
                                    message = getString(R.string.eventCreated);
                                }
                                Toast.makeText(EventsActivity.this, message, Toast.LENGTH_LONG).show();
                                finish();
                            }

                        }

                    });
        }
    }


}
