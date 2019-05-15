package com.dev.eventmanagement.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dev.eventmanagement.BR;

public class LoginModel extends BaseObservable {
    private String emailId;
    private boolean isSignUpEnable = true;

    @Bindable
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
        notifyPropertyChanged(BR.emailId);
    }

    public boolean isSignUpEnable() {
        return isSignUpEnable;
    }

    public void setSignUpEnable(boolean signUpEnable) {
        isSignUpEnable = signUpEnable;
    }
}
