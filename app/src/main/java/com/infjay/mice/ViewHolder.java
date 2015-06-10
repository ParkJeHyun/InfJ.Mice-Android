package com.infjay.mice;

import android.widget.TextView;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.artifacts.MyScheduleInfo;
import com.infjay.mice.artifacts.SponsorInfo;

/**
 * Created by KimJS on 2015-05-24.
 */
public class ViewHolder {

    //Session
    public TextView tvSessionName;
    public TextView tvSessionWriter;
    public TextView tvSessionPresenter;
    public AgendaSessionInfo agendaSessionInfo;

    //Sponsor
    public TextView tvSponsorName;
    public SponsorInfo sponsorInfo;

    //Schedule
    public TextView tvScheduleTitle;
    public MyScheduleInfo myScheduleInfo;

    //My Cardholder
    public TextView tvCardName;
    public TextView tvCardCompany;

    //Memo
    public TextView tvMemoTitle;
    public MemoInfo memoInfo;

    //Indoor Map
    public TextView tvIndoorMapTitle;
    public IndoorMapInfo indoorMapInfo;

    //Messenger
    public TextView tvMessengerName;
    public TextView tvMessengerDate;
    public TextView tvMessengerMessage;

}
