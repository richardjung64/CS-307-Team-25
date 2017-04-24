package com.styln;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shanu on 4/23/17.
 */

public abstract class Notifications {
    private UUID uuid;
    private String title;
    private Date date;
    private boolean isCrimeSolved;

    public Notifications() {
        uuid = UUID.randomUUID();
        date = new Date();
    }

    //getter for Date
    public Date getDate() {
        return date;
    }

    //setter for Date
    public void setDate(Date date) {
        this.date = date;
    }

    //Getter for UUID and title
    public UUID getUuid() {
        return uuid;
    }

    public abstract String getTitle();

    //Setter for title

    public String toString() {
        return title;
    }
   public abstract void notify_user(String userName);
}
