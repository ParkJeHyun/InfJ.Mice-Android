package com.infjay.mice.artifacts;

/**
 * Created by KimJS on 2015-06-01.
 */
public class MessengerInfo {
    private String name;
    private String date;
    private String message;

    public MessengerInfo()
    {
        name = "";
        date = "";
        message = "";
    }

    public void setName(String str) { name = str; }
    public void setDate(String str) { date = str; }
    public void setMessage(String str) { message = str; }

    public String getName() { return name; }
    public String getDate() { return date; }
    public String getMessage() { return message; }
}
