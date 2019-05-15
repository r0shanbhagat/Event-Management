package com.dev.eventmanagement;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dev.eventmanagement.adapter.ListItemsAdapter;
import com.dev.eventmanagement.databinding.ActivityEventListBinding;
import com.dev.eventmanagement.model.EventListItem;
import com.dev.eventmanagement.utilities.AppConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventListingActivity extends AppCompatActivity {
    public static final String TAG = EventListingActivity.class.getSimpleName();
    public static final String KEY_ARGS = "args_item";
    private DatabaseReference mListItemRef;
    private ActivityEventListBinding binding;
    private List<EventListItem> myListItems;
    private ListItemsAdapter mAdapter;
    private String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_list);
        binding.setActivity(this);
        myListItems = new ArrayList<>();
        mListItemRef = FirebaseDatabase.getInstance().getReference().child("events");
        setUpToolbar();
        handleArgs();
        setUpList();
        fetchListFromServer();
    }


    private void handleArgs() {
        if (null != getIntent()) {
            emailId = getIntent().getStringExtra(KEY_ARGS);
        }
    }


    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.sort)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        new CustomDialog(EventListingActivity.this, (startDate, endDate) -> fetchListFromServerByRange(startDate, endDate)).show();
                        return true;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpList() {
        mAdapter = new ListItemsAdapter(myListItems);
        mAdapter.setOnItemClickListener((view, viewModel, requestCode) -> {
            if (requestCode == AppConstant.EventItemClick.EDIT_ITEM) {
                onAddEditEvent(viewModel);
            } else if (requestCode == AppConstant.EventItemClick.REMOVE_ITEM) {
                removeEvent(viewModel.getKey());
            }
        });
        binding.listItemRecyclerView.setAdapter(mAdapter);

    }

    private void removeEvent(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.warning_message));
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.cancel();
            removeFromDatabase(key);
        });

        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private void removeFromDatabase(String key) {
        //remove from db and update List
        if (null != mListItemRef) {
            mListItemRef.child(key).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.eventDeleted), Toast.LENGTH_LONG).show();
                        }

                    });
        }
    }


    private void fetchListFromServer() {
        mListItemRef
                .orderByChild(AppConstant.COLUMN_NAME_EMAIL).equalTo(emailId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            myListItems.clear();
                            for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                                EventListItem eventListItem = adSnapshot.getValue(EventListItem.class);
                                eventListItem.setKey(adSnapshot.getKey());
                                myListItems.add(eventListItem);
                            }
                            if (null != mAdapter) {
                                mAdapter.notifyDataSetChanged();
                                showDataAvailable(true);
                            }
                        } else {
                            //new Email id
                            showDataAvailable(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Error trying to get classified ads for " + emailId +
                                " " + databaseError);
                        Toast.makeText(EventListingActivity.this,
                                "Error trying to get classified ads for " + emailId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fetchListFromServerByRange(String startDate, String endDate) {
        mListItemRef
                .orderByChild(AppConstant.COLUMN_NAME_DATE)
                .startAt(startDate)
                .endAt(endDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(EventListingActivity.this, getString(R.string.filter_applied), Toast.LENGTH_LONG).show();
                            myListItems.clear();
                            for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                                EventListItem eventListItem = adSnapshot.getValue(EventListItem.class);
                                eventListItem.setKey(adSnapshot.getKey());
                                myListItems.add(eventListItem);
                            }
                            if (null != mAdapter) {
                                mAdapter.notifyDataSetChanged();
                                showDataAvailable(true);
                            }
                        } else {
                            Toast.makeText(EventListingActivity.this, getString(R.string.no_record_found), Toast.LENGTH_LONG).show();
                            //new Email id
                            showDataAvailable(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Error trying to get classified ads for " + emailId +
                                " " + databaseError);
                        Toast.makeText(EventListingActivity.this,
                                "Error trying to get classified ads for " + emailId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onAddEditEvent(EventListItem viewModel) {
        Intent intent = new Intent(this, EventsActivity.class);
        if (null == viewModel) {
            viewModel = new EventListItem();
            viewModel.setEmailId(emailId);
            viewModel.setIsForAdd(1);
        }
        intent.putExtra(EventsActivity.KEY_ARGS, viewModel);
        startActivity(intent);
    }

    private void showDataAvailable(boolean isDataAvailable) {
        binding.incNoData.rlyNoData.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
        binding.listItemRecyclerView.setVisibility(isDataAvailable ? View.VISIBLE : View.GONE);
    }
}
