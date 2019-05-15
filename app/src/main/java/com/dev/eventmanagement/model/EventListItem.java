package com.dev.eventmanagement.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.eventmanagement.BR;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EventListItem extends BaseObservable implements Parcelable {

    public static final Creator<EventListItem> CREATOR = new Creator<EventListItem>() {
        @Override
        public EventListItem createFromParcel(Parcel in) {
            return new EventListItem(in);
        }

        @Override
        public EventListItem[] newArray(int size) {
            return new EventListItem[size];
        }
    };
    public String eventTitle;
    public String emailId;
    public String eventDate;
    private String key;
    private int isForAdd;

    public EventListItem(Parcel in) {
        this.emailId = in.readString();
        this.eventTitle = in.readString();
        this.eventDate = in.readString();
        this.key = in.readString();
        this.isForAdd = in.readInt();

    }

    public EventListItem() {
        // Default constructor required for calls to DataSnapshot.getValue(EventListItem.class)
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailId);
        dest.writeString(eventTitle);
        dest.writeString(eventDate);
        dest.writeString(key);
        dest.writeInt(isForAdd);
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    @Bindable
    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
        notifyPropertyChanged(BR.eventDate);
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public int getIsForAdd() {
        return isForAdd;
    }

    public void setIsForAdd(int isForAdd) {
        this.isForAdd = isForAdd;
    }

    @Override
    public String toString() {
        return this.eventTitle + "\n" + this.eventDate;
    }


}