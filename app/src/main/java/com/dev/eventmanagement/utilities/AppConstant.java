package com.dev.eventmanagement.utilities;

public interface AppConstant {

    String COLUMN_NAME_EMAIL = "emailId";
    String COLUMN_NAME_DATE = "eventDate";

    interface EventItemClick {
        int REDIRECT_DETAIL = 0;
        int ADD = 1;
        int REMOVE_ITEM = 2;
        int EDIT_ITEM = 3;
    }


}