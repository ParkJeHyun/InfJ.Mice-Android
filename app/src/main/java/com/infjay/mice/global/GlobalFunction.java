package com.infjay.mice.global;

import com.infjay.mice.artifacts.ConferenceInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KimJS on 2015-06-30.
 */
public class GlobalFunction {

    public static ArrayList<String> getConferenceDates(ConferenceInfo conferenceInfo)
    {
        ArrayList<String> conferenceDates = new ArrayList<String>();
        String startDate = conferenceInfo.conferenceStartDate;
        String endDate = conferenceInfo.conferenceEndDate;

        String cursorDate = startDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        while(!cursorDate.equals(endDate))
        {
            conferenceDates.add(cursorDate);
            Date date = new Date();
            try
            {
                date = sdf.parse(cursorDate);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            long ms = date.getTime() + (1000 * 60 * 60 * 24);
            date = new Date(ms);
            cursorDate = sdf.format(date);
        }
        conferenceDates.add(cursorDate);

        return conferenceDates;
    }
}
