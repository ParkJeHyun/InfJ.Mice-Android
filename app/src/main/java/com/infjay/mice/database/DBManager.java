package com.infjay.mice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.artifacts.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HJHOME on 2015-06-26.
 */
public class DBManager {

    private static DBManager instance;

    private DBHelper dbh;
    private static String TAG = "DBManager";

    private static SimpleDateFormat defaultDateFormat;
    private static SimpleDateFormat printDateFormat;

    // android 생성자
    private DBManager(Context context) {

        dbh = new DBHelper(context);
        dbh.open();
    }

    public static synchronized DBManager getManager(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
            defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            printDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        Log.d(TAG, "instace complete");
        return instance;
    }


    //Conference Info
    //새 세션 추가
    public synchronized void insertConferenceInfo (ConferenceInfo conferenceInfo){
        String sql = "insert into " +
                MiceDB._CONFERENCE_INFO_TABLE_NAME +
                "(" +
                MiceDB._CONFERENCE_START_DATE +
                ", " +
                MiceDB._CONFERENCE_END_DATE +
                ", " +
                MiceDB._CONFERENCE_NAME +
                ", " +
                MiceDB._CONFERENCE_SUMMARY +
                ") " +
                "values " +
                "(" +
                "'" + conferenceInfo.conferenceStartDate + "', " +
                "'" + conferenceInfo.conferenceEndDate + "', " +
                "'" + conferenceInfo.conferenceName + "', " +
                "'" + conferenceInfo.conferenceSummary+ "'" +
                "); ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG,"insertConferenceInfo 완료");
    }
    //Sqlite에 ConferenceInfo가 있는지 확인
    public synchronized int getCountConference(){
        String sql = "select * from " + MiceDB._CONFERENCE_INFO_TABLE_NAME
                + ";";
        Cursor c = dbh.mDB.rawQuery(sql,null);
        int count = c.getCount();
        c.close();

        return count;
    }
    //ConferenceInfo 불러오기
    public synchronized ConferenceInfo getConferenceInfo(){
        ConferenceInfo conferenceInfo = new ConferenceInfo();

        String sql = "select * from " + MiceDB._CONFERENCE_INFO_TABLE_NAME
                + ";";
        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return conferenceInfo; // error?

        int startDateIndex = c.getColumnIndex(MiceDB._CONFERENCE_START_DATE);
        int endDateIndex = c.getColumnIndex(MiceDB._CONFERENCE_END_DATE);
        int nameIndex = c.getColumnIndex(MiceDB._CONFERENCE_NAME);
        int summaryIndex = c.getColumnIndex(MiceDB._CONFERENCE_SUMMARY);

        conferenceInfo.conferenceStartDate = c.getString(startDateIndex);
        conferenceInfo.conferenceEndDate = c.getString(endDateIndex);
        conferenceInfo.conferenceName = c.getString(nameIndex);
        conferenceInfo.conferenceSummary = c.getString(summaryIndex);

        c.close();
        Log.d(TAG,"getConferenceInfo 완료");
        return conferenceInfo;
    }
    //update conferenceInfo
    public synchronized void updateConferenceInfo (ConferenceInfo conferenceInfo){
        String sql = "update " +
                MiceDB._CONFERENCE_INFO_TABLE_NAME +
                " set " +
                MiceDB._CONFERENCE_START_DATE +
                " = '" +
                conferenceInfo.conferenceStartDate +
                "', " +
                MiceDB._CONFERENCE_END_DATE +
                " = '" +
                conferenceInfo.conferenceEndDate +
                "', " +
                MiceDB._CONFERENCE_NAME +
                " = '" +
                conferenceInfo.conferenceName +
                "', " +
                MiceDB._CONFERENCE_SUMMARY +
                " = '" +
                conferenceInfo.conferenceSummary +
                "'; ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "updateConferenceInfo 완료");
    }


    //Agenda 세션관련
    //새 세션 추가
    public synchronized void insertSessionToAgenda (ArrayList<AgendaSessionInfo> sessionInfoList){
        for(int i=0;i<sessionInfoList.size();i++) {
            String sql = "insert into " +
                    MiceDB._AGENDA_SESSION_TABLE_NAME +
                    "(" +
                    MiceDB._AGENDA_SESSION_SEQ +
                    ", " +
                    MiceDB._AGENDA_SESSION_TITLE +
                    ", " +
                    MiceDB._AGENDA_SESSION_CONTENTS +
                    ", " +
                    MiceDB._AGENDA_SESSION_SUMMARY +
                    ", " +
                    MiceDB._AGENDA_SESSION_WRITER_SEQ +
                    ", " +
                    MiceDB._AGENDA_SESSION_PRESENTER_SEQ +
                    ", " +
                    MiceDB._AGENDA_SESSION_START_TIME +
                    ", " +
                    MiceDB._AGENDA_SESSION_END_TIME +
                    ", " +
                    MiceDB._AGENDA_SESSION_ATTACHED +
                    ", " +
                    MiceDB._AGENDA_SESSION_REG_TIME +
                    ", " +
                    MiceDB._AGENDA_SESSION_MOD_TIME +
                    ", " +
                    MiceDB._AGENDA_SESSION_DATE +
                    ") " +
                    "values " +
                    "(" +
                    "'" + sessionInfoList.get(i).agendaSessionSeq + "', " +
                    "'" + sessionInfoList.get(i).sessionTitle + "', " +
                    "'" + sessionInfoList.get(i).sessionContents + "', " +
                    "'" + sessionInfoList.get(i).sessionSumarry + "', " +
                    "'" + sessionInfoList.get(i).sessionWriterUserSeq + "', " +
                    "'" + sessionInfoList.get(i).sessionPresenterUserSeq + "', " +
                    "'" + sessionInfoList.get(i).sessionStartTime + "', " +
                    "'" + sessionInfoList.get(i).sessionEndTime + "', " +
                    "'" + sessionInfoList.get(i).sessionAttached + "', " +
                    "'" + sessionInfoList.get(i).regDate + "', " +
                    "'" + sessionInfoList.get(i).modDate + "', " +
                    "'" + sessionInfoList.get(i).sessionDate + "'" +
                    "); ";
            dbh.mDB.execSQL(sql);
        }
        Log.d(TAG,"insertAgendaSession 완료");
    }
    //모든 session 삭제
    public synchronized void deleteAgendaSession(){
        String sql = "delete from " + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteAgendaSession 완료");
    }
    //Date로 session 불러오기
    public synchronized ArrayList<AgendaSessionInfo> getSessionFromAgendaBySessionDate(String sessionDate){
        ArrayList<AgendaSessionInfo> sessionInfoList = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from "
                + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " where "
                + MiceDB._AGENDA_SESSION_DATE
                + " = '"
                + sessionDate
                + "' ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        AgendaSessionInfo sessionInfo;

        int sessionSeqIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_MOD_TIME);
        int DateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_DATE);


        while (!c.isAfterLast()) {
            sessionInfo = new AgendaSessionInfo();

            sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
            sessionInfo.sessionTitle = c.getString(titleIndex);
            sessionInfo.sessionContents = c.getString(contentIndex);
            sessionInfo.sessionSumarry = c.getString(summaryIndex);
            sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
            sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
            sessionInfo.sessionStartTime = c.getString(startTimeIndex);
            sessionInfo.sessionEndTime = c.getString(endTimeIndex);
            sessionInfo.sessionAttached = c.getString(attachIndex);
            sessionInfo.regDate = c.getString(regDateIndex);
            sessionInfo.modDate = c.getString(modDateIndex);
            sessionInfo.sessionDate = c.getString(DateIndex);

            sessionInfoList.add(sessionInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getTitleSession 완료");
        return sessionInfoList;
    }
    //sessionTitle로 session 불러오기
    public synchronized ArrayList<AgendaSessionInfo> getSessionFromAgendaBySessionTitle(String sessionTitle){
        ArrayList<AgendaSessionInfo> sessionInfoList = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from "
                + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " where "
                + MiceDB._AGENDA_SESSION_TITLE
                + " = '"
                + sessionTitle
                + "' ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        AgendaSessionInfo sessionInfo;

        int sessionSeqIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_MOD_TIME);
        int DateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_DATE);


        while (!c.isAfterLast()) {
            sessionInfo = new AgendaSessionInfo();

            sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
            sessionInfo.sessionTitle = c.getString(titleIndex);
            sessionInfo.sessionContents = c.getString(contentIndex);
            sessionInfo.sessionSumarry = c.getString(summaryIndex);
            sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
            sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
            sessionInfo.sessionStartTime = c.getString(startTimeIndex);
            sessionInfo.sessionEndTime = c.getString(endTimeIndex);
            sessionInfo.sessionAttached = c.getString(attachIndex);
            sessionInfo.regDate = c.getString(regDateIndex);
            sessionInfo.modDate = c.getString(modDateIndex);
            sessionInfo.sessionDate = c.getString(DateIndex);

            sessionInfoList.add(sessionInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getTitleSession 완료");
        return sessionInfoList;
    }
    //writer로 Agenda에서 session 불러오기
    public synchronized ArrayList<AgendaSessionInfo> getSessionFromAgendaBySessionWriter(String sessionWriter){
        ArrayList<AgendaSessionInfo> sessionInfoList = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from "
                + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " where "
                + MiceDB._AGENDA_SESSION_WRITER_SEQ
                + " = '"
                + sessionWriter
                + "' ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        AgendaSessionInfo sessionInfo;

        int sessionSeqIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_MOD_TIME);
        int DateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_DATE);

        while (!c.isAfterLast()) {
            sessionInfo = new AgendaSessionInfo();

            sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
            sessionInfo.sessionTitle = c.getString(titleIndex);
            sessionInfo.sessionContents = c.getString(contentIndex);
            sessionInfo.sessionSumarry = c.getString(summaryIndex);
            sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
            sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
            sessionInfo.sessionStartTime = c.getString(startTimeIndex);
            sessionInfo.sessionEndTime = c.getString(endTimeIndex);
            sessionInfo.sessionAttached = c.getString(attachIndex);
            sessionInfo.regDate = c.getString(regDateIndex);
            sessionInfo.modDate = c.getString(modDateIndex);
            sessionInfo.sessionDate = c.getString(DateIndex);

            sessionInfoList.add(sessionInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getWriterSession 완료");
        return sessionInfoList;
    }
    //presenter로 Agenda에서 session 불러오기
    public synchronized ArrayList<AgendaSessionInfo> getSessionFromAgendaBySessionPresenter(String sessionPresenter){
        ArrayList<AgendaSessionInfo> sessionInfoList = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from "
                + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " where "
                + MiceDB._AGENDA_SESSION_PRESENTER_SEQ
                + " = '"
                + sessionPresenter
                + "' ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        AgendaSessionInfo sessionInfo;

        int sessionSeqIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_MOD_TIME);
        int DateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_DATE);

        while (!c.isAfterLast()) {
            sessionInfo = new AgendaSessionInfo();

            sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
            sessionInfo.sessionTitle = c.getString(titleIndex);
            sessionInfo.sessionContents = c.getString(contentIndex);
            sessionInfo.sessionSumarry = c.getString(summaryIndex);
            sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
            sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
            sessionInfo.sessionStartTime = c.getString(startTimeIndex);
            sessionInfo.sessionEndTime = c.getString(endTimeIndex);
            sessionInfo.sessionAttached = c.getString(attachIndex);
            sessionInfo.regDate = c.getString(regDateIndex);
            sessionInfo.modDate = c.getString(modDateIndex);
            sessionInfo.sessionDate = c.getString(DateIndex);

            sessionInfoList.add(sessionInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getPresenterSession 완료");
        return sessionInfoList;
    }
    //sessionSeq로 Agenda에서 session 불러오기
    public synchronized AgendaSessionInfo getSessionFromAgendaBySessionSeq(String sessionSeq){
        AgendaSessionInfo sessionInfo = new AgendaSessionInfo();

        String sql = "select * from "
                + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " where "
                + MiceDB._AGENDA_SESSION_SEQ
                + " = '"
                + sessionSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return sessionInfo; // error?

        int sessionSeqIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_MOD_TIME);
        int DateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_DATE);

        sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
        sessionInfo.sessionTitle = c.getString(titleIndex);
        sessionInfo.sessionContents = c.getString(contentIndex);
        sessionInfo.sessionSumarry = c.getString(summaryIndex);
        sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
        sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
        sessionInfo.sessionStartTime = c.getString(startTimeIndex);
        sessionInfo.sessionEndTime = c.getString(endTimeIndex);
        sessionInfo.sessionAttached = c.getString(attachIndex);
        sessionInfo.regDate = c.getString(regDateIndex);
        sessionInfo.modDate = c.getString(modDateIndex);
        sessionInfo.sessionDate = c.getString(DateIndex);

        c.close();
        Log.d(TAG,"getSessionBySessionSeq 완료");
        return sessionInfo;
    }
    //모든 Session 정보 받아오기
    public synchronized ArrayList<AgendaSessionInfo> getAllSessionFromAgenda() {
        ArrayList<AgendaSessionInfo> arraySessionInfo = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from " + MiceDB._AGENDA_SESSION_TABLE_NAME
                + " order by "+
                MiceDB._AGENDA_SESSION_START_TIME +
                " desc;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        AgendaSessionInfo sessionInfo;

        int sessionSeqIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_MOD_TIME);
        int DateIndex = c.getColumnIndex(MiceDB._AGENDA_SESSION_DATE);

        while (!c.isAfterLast()) {
            sessionInfo = new AgendaSessionInfo();

            sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
            sessionInfo.sessionTitle = c.getString(titleIndex);
            sessionInfo.sessionContents = c.getString(contentIndex);
            sessionInfo.sessionSumarry = c.getString(summaryIndex);
            sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
            sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
            sessionInfo.sessionStartTime = c.getString(startTimeIndex);
            sessionInfo.sessionEndTime = c.getString(endTimeIndex);
            sessionInfo.sessionAttached = c.getString(attachIndex);
            sessionInfo.regDate = c.getString(regDateIndex);
            sessionInfo.modDate = c.getString(modDateIndex);
            sessionInfo.sessionDate = c.getString(DateIndex);

            arraySessionInfo.add(sessionInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getAllSession 완료");
        return arraySessionInfo;
    }


    //Sponsor
    //새 Sponsor 추가
    public synchronized void insertSponsor(SponsorInfo sponsorInfo){
        String sql = "insert into " +
                MiceDB._SPONSOR_TABLE_NAME +
                "(" +
                MiceDB._SPONSOR_SEQ +
                ", " +
                MiceDB._SPONSOR_NAME +
                ", " +
                MiceDB._SPONSOR_EXPLANATION +
                ", " +
                MiceDB._SPONSOR_LOGO +
                ", " +
                MiceDB._SPONSOR_DETAIL_IMG +
                ", " +
                MiceDB._SPONSOR_REG_DATE +
                ", " +
                MiceDB._SPONSOR_MOD_DATE +
                ") " +
                "values " +
                "(" +
                "'" + sponsorInfo.sponsorSeq + "', " +
                "'" + sponsorInfo.sponsorName + "', " +
                "'" + sponsorInfo.sponsorExplanation + "', " +
                "'" + sponsorInfo.logoPath + "'. " +
                "'" + sponsorInfo.detailImagePath + "', " +
                "'" + sponsorInfo.regDate +"', " +
                "'" + sponsorInfo.modDate +"'" +
                "); ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG,"insertSponsor 완료");
    }
    //모든 sponsor 불러오기
    public synchronized ArrayList<SponsorInfo> getAllSponsor(){
        ArrayList<SponsorInfo> arraySponsorInfo = new ArrayList<SponsorInfo>();

        String sql = "select * from " + MiceDB._SPONSOR_TABLE_NAME
                + ";";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        SponsorInfo sponsorInfo;

        int sponsorSeqIndex = c.getColumnIndex(MiceDB._SPONSOR_SEQ);
        int nameIndex = c.getColumnIndex(MiceDB._SPONSOR_NAME);
        int explanationIndex = c.getColumnIndex(MiceDB._SPONSOR_EXPLANATION);
        int logoIndex = c.getColumnIndex(MiceDB._SPONSOR_LOGO);
        int detailImgIndex = c.getColumnIndex(MiceDB._SPONSOR_DETAIL_IMG);
        int regDateIndex = c.getColumnIndex(MiceDB._SPONSOR_REG_DATE);
        int modDateIndex = c.getColumnIndex(MiceDB._SPONSOR_MOD_DATE);


        while (!c.isAfterLast()) {
            sponsorInfo = new SponsorInfo();

            sponsorInfo.sponsorSeq = c.getString(sponsorSeqIndex);
            sponsorInfo.sponsorName = c.getString(nameIndex);
            sponsorInfo.sponsorExplanation = c.getString(explanationIndex);
            sponsorInfo.logoPath = c.getString(logoIndex);
            sponsorInfo.detailImagePath = c.getString(detailImgIndex);
            sponsorInfo.regDate = c.getString(regDateIndex);
            sponsorInfo.modDate = c.getString(modDateIndex);

            arraySponsorInfo.add(sponsorInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getAllSponsor 완료");
        return arraySponsorInfo;
    }
    //seq를 이용해서 Sponsor 불러오기
    public synchronized SponsorInfo getSponsorBySeq(String sponsorSeq){
        SponsorInfo sponsorInfo  = new SponsorInfo();

        String sql = "select * from "
                + MiceDB._SPONSOR_TABLE_NAME
                + " where "
                + MiceDB._SPONSOR_SEQ
                + " = '"
                + sponsorSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return sponsorInfo; // error?

        int sponsorSeqIndex = c.getColumnIndex(MiceDB._SPONSOR_SEQ);
        int nameIndex = c.getColumnIndex(MiceDB._SPONSOR_NAME);
        int explanationIndex = c.getColumnIndex(MiceDB._SPONSOR_EXPLANATION);
        int logoIndex = c.getColumnIndex(MiceDB._SPONSOR_LOGO);
        int detailImgIndex = c.getColumnIndex(MiceDB._SPONSOR_DETAIL_IMG);
        int regDateIndex = c.getColumnIndex(MiceDB._SPONSOR_REG_DATE);
        int modDateIndex = c.getColumnIndex(MiceDB._SPONSOR_MOD_DATE);

        sponsorInfo.sponsorSeq = c.getString(sponsorSeqIndex);
        sponsorInfo.sponsorName = c.getString(nameIndex);
        sponsorInfo.sponsorExplanation = c.getString(explanationIndex);
        sponsorInfo.logoPath = c.getString(logoIndex);
        sponsorInfo.detailImagePath = c.getString(detailImgIndex);
        sponsorInfo.regDate = c.getString(regDateIndex);
        sponsorInfo.modDate = c.getString(modDateIndex);

        c.close();
        Log.d(TAG, "getMemoByMemoSeq 완료");
        return sponsorInfo;
    }
    //모든 sponsor 삭제
    public void deleteAllSponsor(){
        String sql = "delete from " + MiceDB._SPONSOR_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteSponsor 완료");
    }


    //Coupon
    //새 쿠폰 추가
    public synchronized void insertCoupon(ArrayList<CouponInfo> couponList){
        for(int i=0;i<couponList.size();i++) {
            String sql = "insert into " +
                    MiceDB._COUPON_TABLE_NAME +
                    "(" +
                    MiceDB._COUPON_SEQ +
                    ", " +
                    MiceDB._COUPON_NAME +
                    ", " +
                    MiceDB._COUPON_EXPLANATION +
                    ", " +
                    MiceDB._COUPON_SERIAL +
                    ", " +
                    MiceDB._COUPON_IMG +
                    ", " +
                    MiceDB._COUPON_REG_DATE +
                    ") " +
                    "values " +
                    "(" +
                    "'" + couponList.get(i).couponSeq + "', " +
                    "'" + couponList.get(i).couponName + "', " +
                    "'" + couponList.get(i).couponExplanation + "', " +
                    "'" + couponList.get(i).couponSerial + "', " +
                    "'" + couponList.get(i).couponImg + "', " +
                    "'" + couponList.get(i).regDate + "'" +
                    "); ";

            dbh.mDB.execSQL(sql);
        }
        Log.d(TAG,"insertCoupon 완료");
    }
    //seq를 이용해서 쿠폰 검색
    public synchronized CouponInfo getCouponBySeq(String couponSeq){
        CouponInfo couponInfo  = new CouponInfo();

        String sql = "select * from "
                + MiceDB._COUPON_TABLE_NAME
                + " where "
                + MiceDB._COUPON_SEQ
                + " = '"
                + couponSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return couponInfo; // error?

        int couponSeqIndex = c.getColumnIndex(MiceDB._COUPON_SEQ);
        int nameIndex = c.getColumnIndex(MiceDB._COUPON_NAME);
        int regDateIndex = c.getColumnIndex(MiceDB._COUPON_REG_DATE);
        int serialIndex = c.getColumnIndex(MiceDB._COUPON_SERIAL);
        int explanationIndex = c.getColumnIndex(MiceDB._COUPON_EXPLANATION);
        int imgIndex = c.getColumnIndex(MiceDB._COUPON_IMG);

        couponInfo.couponSeq = c.getString(couponSeqIndex);
        couponInfo.couponName = c.getString(nameIndex);
        couponInfo.regDate = c.getString(regDateIndex);
        couponInfo.couponSerial = c.getString(serialIndex);
        couponInfo.couponExplanation = c.getString(explanationIndex);
        couponInfo.couponImg = c.getString(imgIndex);

        c.close();
        Log.d(TAG, "getCouponBySeq 완료");
        return couponInfo;
    }
    //모든 쿠폰 불러오기
    public synchronized ArrayList<CouponInfo> getAllCoupon(){
        ArrayList<CouponInfo> arrayCouponInfo = new ArrayList<CouponInfo>();

        String sql = "select * from " + MiceDB._COUPON_TABLE_NAME
                + ";";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        CouponInfo couponInfo;

        int couponSeqIndex = c.getColumnIndex(MiceDB._COUPON_SEQ);
        int nameIndex = c.getColumnIndex(MiceDB._COUPON_NAME);
        int regDateIndex = c.getColumnIndex(MiceDB._COUPON_REG_DATE);
        int serialIndex = c.getColumnIndex(MiceDB._COUPON_SERIAL);
        int explanationIndex = c.getColumnIndex(MiceDB._COUPON_EXPLANATION);
        int imgIndex = c.getColumnIndex(MiceDB._COUPON_IMG);

        while (!c.isAfterLast()) {
            couponInfo = new CouponInfo();

            couponInfo.couponSeq = c.getString(couponSeqIndex);
            couponInfo.couponName = c.getString(nameIndex);
            couponInfo.regDate = c.getString(regDateIndex);
            couponInfo.couponSerial = c.getString(serialIndex);
            couponInfo.couponExplanation = c.getString(explanationIndex);
            couponInfo.couponImg = c.getString(imgIndex);

            arrayCouponInfo.add(couponInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getAllCoupon 완료");
        return arrayCouponInfo;
    }
    //모든 coupon 삭제
    public synchronized void deleteAllCoupon(){
        String sql = "delete from " + MiceDB._COUPON_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteAllCoupon 완료");
    }



    //바인더관련
    //새 세션 추가
    public synchronized void insertSessionTobinder (String userSeq,AgendaSessionInfo sessionInfo){
        String sql = "insert into " +
                MiceDB._BINDER_SESSION_TABLE_NAME +
                "(" +
                MiceDB._BINDER_USER_SEQ +
                ", " +
                MiceDB._BINDER_SESSION_SEQ +
                ", " +
                MiceDB._BINDER_SESSION_TITLE +
                ", " +
                MiceDB._BINDER_SESSION_CONTENTS +
                ", " +
                MiceDB._BINDER_SESSION_SUMMARY +
                ", " +
                MiceDB._BINDER_SESSION_WRITER_SEQ +
                ", " +
                MiceDB._BINDER_SESSION_PRESENTER_SEQ +
                ", " +
                MiceDB._BINDER_SESSION_START_TIME +
                ", " +
                MiceDB._BINDER_SESSION_END_TIME +
                ", " +
                MiceDB._BINDER_SESSION_ATTACHED +
                ", " +
                MiceDB._BINDER_SESSION_REG_TIME +
                ", " +
                MiceDB._BINDER_SESSION_MOD_TIME +
                ") " +
                "values " +
                "(" +
                "'" + userSeq + "', " +
                "'" + sessionInfo.agendaSessionSeq +"', " +
                "'" + sessionInfo.sessionTitle + "', " +
                "'" + sessionInfo.sessionContents + "', " +
                "'" + sessionInfo.sessionSumarry + "', " +
                "'" + sessionInfo.sessionWriterUserSeq + "', " +
                "'" + sessionInfo.sessionPresenterUserSeq + "', " +
                "'" + sessionInfo.sessionStartTime + "', " +
                "'" + sessionInfo.sessionEndTime + "', " +
                "'" + sessionInfo.sessionAttached + "', " +
                "'" + sessionInfo.regDate + "', " +
                "'" + sessionInfo.modDate + "'" +
                "); ";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"insertBinderSession 완료");
    }
    //해당 userSeq로 Binder에 저장된 session다 불러오기
    public synchronized ArrayList<AgendaSessionInfo> getAllSessionFromBinder(String userSeq) {
        ArrayList<AgendaSessionInfo> arraySessionInfo = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from " + MiceDB._BINDER_SESSION_TABLE_NAME
                + " where "
                + MiceDB._BINDER_USER_SEQ
                + " = '"
                + userSeq + "'"
                + " order by "+
                MiceDB._BINDER_SESSION_START_TIME +
                " desc;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        AgendaSessionInfo sessionInfo;

        int sessionSeqIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_MOD_TIME);


        while (!c.isAfterLast()) {
            sessionInfo = new AgendaSessionInfo();

            sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
            sessionInfo.sessionTitle = c.getString(titleIndex);
            sessionInfo.sessionContents = c.getString(contentIndex);
            sessionInfo.sessionSumarry = c.getString(summaryIndex);
            sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
            sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
            sessionInfo.sessionStartTime = c.getString(startTimeIndex);
            sessionInfo.sessionEndTime = c.getString(endTimeIndex);
            sessionInfo.sessionAttached = c.getString(attachIndex);
            sessionInfo.regDate = c.getString(regDateIndex);
            sessionInfo.modDate = c.getString(modDateIndex);


            arraySessionInfo.add(sessionInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getAllBinderSession 완료");
        return arraySessionInfo;
    }
    //UserSeq랑 sessionSeq로 세션이 있는지 확인하기
    public synchronized  int getSessionExistInBinderByUserSeqAndSessionSeq(String userSeq,String sessionSeq){
        String sql = "select * from "
                + MiceDB._BINDER_SESSION_TABLE_NAME
                + " where "
                + MiceDB._BINDER_USER_SEQ
                + " = '"
                + userSeq
                + "' AND "
                + MiceDB._BINDER_SESSION_SEQ
                + " = '"
                + sessionSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        int count = c.getCount();

        c.close();
        Log.d(TAG,"getSessionBySessionSeq 완료");

        return count;
    }

    //seq를 이용해서 세션 불러오기
    public synchronized  AgendaSessionInfo getSessionInBinderBySessionSeq(String sessionSeq){
        AgendaSessionInfo sessionInfo = new AgendaSessionInfo();

        String sql = "select * from "
                + MiceDB._BINDER_SESSION_TABLE_NAME
                + " where "
                + MiceDB._BINDER_SESSION_SEQ
                + " = '"
                + sessionSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return sessionInfo; // error?

        int sessionSeqIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_CONTENTS);
        int titleIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_TITLE);
        int summaryIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_SUMMARY);
        int writerIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_WRITER_SEQ);
        int presenterIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_PRESENTER_SEQ);
        int startTimeIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_START_TIME);
        int endTimeIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_END_TIME);
        int attachIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_ATTACHED);
        int regDateIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_REG_TIME);
        int modDateIndex = c.getColumnIndex(MiceDB._BINDER_SESSION_MOD_TIME);

        sessionInfo.agendaSessionSeq = c.getString(sessionSeqIndex);
        sessionInfo.sessionTitle = c.getString(titleIndex);
        sessionInfo.sessionContents = c.getString(contentIndex);
        sessionInfo.sessionSumarry = c.getString(summaryIndex);
        sessionInfo.sessionWriterUserSeq = c.getString(writerIndex);
        sessionInfo.sessionPresenterUserSeq = c.getString(presenterIndex);
        sessionInfo.sessionStartTime = c.getString(startTimeIndex);
        sessionInfo.sessionEndTime = c.getString(endTimeIndex);
        sessionInfo.sessionAttached = c.getString(attachIndex);
        sessionInfo.regDate = c.getString(regDateIndex);
        sessionInfo.modDate = c.getString(modDateIndex);

        c.close();
        Log.d(TAG,"getSessionBySessionSeq 완료");

        return sessionInfo;
    }
    //session 삭제
    public synchronized void deleteSessionInBinder (AgendaSessionInfo sessionInfo){
        String sql = "delete from " +
                MiceDB._BINDER_SESSION_TABLE_NAME +
                " where " +
                MiceDB._BINDER_SESSION_SEQ +
                " = '" +
                sessionInfo.agendaSessionSeq +
                "'; ";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteBinderSession 완료");
    }


    //메모관련
    //새 메모 추가
    public synchronized void insertMemoInfo (MemoInfo memoInfo){
        String sql = "insert into " +
                MiceDB._MEMO_TABLE_NAME +
                "(" +
                MiceDB._MEMO_CONTENTS +
                ", " +
                MiceDB._MEMO_REG_DATE +
                ", " +
                MiceDB._MEMO_MOD_DATE +
                ") " +
                "values " +
                "(" +
                "'" + memoInfo.contents + "', " +
                "'" + memoInfo.regDate + "', " +
                "'" + memoInfo.modDate + "' " +
                "); ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "insertMemoInfo 완료");
    }


    //메모 수정
    public synchronized void updateMemoInfo (MemoInfo memoInfo){
        String sql = "update " +
                MiceDB._MEMO_TABLE_NAME +
                " set " +
                MiceDB._MEMO_CONTENTS +
                " = '" +
                memoInfo.contents +
                "', " +
                MiceDB._MEMO_MOD_DATE +
                " = '" +
                memoInfo.modDate +
                "' " +
                "where  " +
                MiceDB._MEMO_MEMO_SEQ +
                " = '" +
                memoInfo.memoSeq +
                "'; ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "updateMemoInfo 완료");
    }

    //메모 삭제
    public synchronized void deleteMemoInfo (MemoInfo memoInfo) {
        String sql = "delete from " +
                MiceDB._MEMO_TABLE_NAME +
                " where  " +
                MiceDB._MEMO_MEMO_SEQ +
                " = '" +
                memoInfo.memoSeq +
                "'; ";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteMemoInfo 완료");
    }

    //seq로 메모 1개 받아오기
    public synchronized MemoInfo getMemoByMemoSeq(String memoSeq){
        MemoInfo memoInfo = new MemoInfo();

        String sql = "select * from "
                + MiceDB._MEMO_TABLE_NAME
                + " where "
                + MiceDB._MEMO_MEMO_SEQ
                + " = '"
                + memoSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return memoInfo; // error?

        int memoSeqIndex = c.getColumnIndex(MiceDB._MEMO_MEMO_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._MEMO_CONTENTS);
        int regDateIndex = c.getColumnIndex(MiceDB._MEMO_REG_DATE);
        int modDateIndex = c.getColumnIndex(MiceDB._MEMO_MOD_DATE);

        memoInfo.memoSeq = c.getString(memoSeqIndex);
        memoInfo.contents = c.getString(contentIndex);
        memoInfo.regDate = c.getString(regDateIndex);
        memoInfo.modDate = c.getString(modDateIndex);

        c.close();
        Log.d(TAG,"getMemoByMemoSeq 완료");
        return memoInfo;
    }

    //메모 다 받아오기
    public synchronized ArrayList<MemoInfo> getAllMemo() {
        ArrayList<MemoInfo> arrayMemoInfo = new ArrayList<MemoInfo>();

        String sql = "select * from " + MiceDB._MEMO_TABLE_NAME
                + " order by "+
                MiceDB._MEMO_MOD_DATE +
                " desc;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        MemoInfo memoInfo;

        int memoSeqIndex = c.getColumnIndex(MiceDB._MEMO_MEMO_SEQ);
        int contentIndex = c.getColumnIndex(MiceDB._MEMO_CONTENTS);
        int regDateIndex = c.getColumnIndex(MiceDB._MEMO_REG_DATE);
        int modDateIndex = c.getColumnIndex(MiceDB._MEMO_MOD_DATE);


        while (!c.isAfterLast()) {
            memoInfo = new MemoInfo();

            memoInfo.memoSeq = c.getString(memoSeqIndex);
            memoInfo.contents = c.getString(contentIndex);
            memoInfo.regDate = c.getString(regDateIndex);
            memoInfo.modDate = c.getString(modDateIndex);

            arrayMemoInfo.add(memoInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getAllMemo 완료");
        return arrayMemoInfo;
    }


    //세션 갖고있는 사용자 정보
    //세션 정보 테이블에 저장하기
    public synchronized void insertUserInfo(UserInfo userInfo) {

        ContentValues values;

        values = new ContentValues();
        Date date = new Date();

        values.put(MiceDB._USER_USER_SEQ, userInfo.userSeq);
        values.put(MiceDB._USER_ID_FLAG, userInfo.idFlag);
        values.put(MiceDB._USER_USER_ID, userInfo.userId);
        values.put(MiceDB._USER_PASSWORD, userInfo.password);
        values.put(MiceDB._USER_NAME, userInfo.name);
        values.put(MiceDB._USER_COMPANY, userInfo.company);
        values.put(MiceDB._USER_PICTURE, userInfo.picture);
        values.put(MiceDB._USER_PHONE, userInfo.phone);
        values.put(MiceDB._USER_EMAIL, userInfo.email);
        values.put(MiceDB._USER_ADDRESS, userInfo.address);
        values.put(MiceDB._USER_AUTHORITY_KIND, userInfo.authorityKind);
        values.put(MiceDB._USER_PHONE_1, userInfo.phone_1);
        values.put(MiceDB._USER_PHONE_2, userInfo.phone_2);
        values.put(MiceDB._USER_CELL_PHONE_1, userInfo.cellPhone_1);
        values.put(MiceDB._USER_CELL_PHONE_2, userInfo.cellPhone_2);
        values.put(MiceDB._USER_BUSINESS_CARD_CODE, userInfo.businessCardCode);
        values.put(MiceDB._USER_BUSINESS_CARD_SHARE_FLAG, userInfo.businessCardShareFlag);
        values.put(MiceDB._USER_NATION_CODE, userInfo.nationCode);
        values.put(MiceDB._USER_PLATFORM, userInfo.platform);
        values.put(MiceDB._USER_REG_DATE, userInfo.regDate);
        values.put(MiceDB._USER_MOD_DATE, userInfo.modDate);
        values.put(MiceDB._USER_DUTY, userInfo.duty);


        dbh.mDB.insert(MiceDB._USER_INFO_TABLE_NAME, null, values);
        Log.d(TAG,"insertUserInfo 완료");
    }
    //세션에 있는 유저 불러오기
    //현재 세션테이블에 있는 애새끼 불러오기
    public synchronized UserInfo getUserInfo(){
        UserInfo userInfo = new UserInfo();

        String sql = "select * from " + MiceDB._USER_INFO_TABLE_NAME + " limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return userInfo; // error?

        int userSeqIndex = c.getColumnIndex(MiceDB._USER_USER_SEQ);
        int userIdFlagIndex = c.getColumnIndex(MiceDB._USER_ID_FLAG);
        int userUserIdIndex = c.getColumnIndex(MiceDB._USER_USER_ID);
        int userPasswordIndex = c.getColumnIndex(MiceDB._USER_PASSWORD);
        int userNameIndex = c.getColumnIndex(MiceDB._USER_NAME);
        int userCompanyIndex = c.getColumnIndex(MiceDB._USER_COMPANY);
        int userPictureIndex = c.getColumnIndex(MiceDB._USER_PICTURE);
        int userPhoneIndex = c.getColumnIndex(MiceDB._USER_PHONE);
        int userEmailIndex = c.getColumnIndex(MiceDB._USER_EMAIL);
        int userAddressIndex = c.getColumnIndex(MiceDB._USER_ADDRESS);
        int userAuthorityKindIndex = c.getColumnIndex(MiceDB._USER_AUTHORITY_KIND);
        int userPhone1Index = c.getColumnIndex(MiceDB._USER_PHONE_1);
        int userPhone2Index = c.getColumnIndex(MiceDB._USER_PHONE_2);
        int userCellPhone1Index = c.getColumnIndex(MiceDB._USER_CELL_PHONE_1);
        int userCellPhone2Index = c.getColumnIndex(MiceDB._USER_CELL_PHONE_2);
        int userBusinessCardCodeIndex = c.getColumnIndex(MiceDB._USER_BUSINESS_CARD_CODE);
        int userBusinessCardShareFlagIndex = c.getColumnIndex(MiceDB._USER_BUSINESS_CARD_SHARE_FLAG);
        int userNationCodeIndex = c.getColumnIndex(MiceDB._USER_NATION_CODE);
        int userPlatformIndex = c.getColumnIndex(MiceDB._USER_PLATFORM);
        int userRegDateIndex = c.getColumnIndex(MiceDB._USER_REG_DATE);
        int userModDateIndex = c.getColumnIndex(MiceDB._USER_MOD_DATE);
        int userDutyIndex = c.getColumnIndex(MiceDB._USER_DUTY);


        userInfo.userSeq = c.getString(userSeqIndex);
        userInfo.idFlag = c.getString(userIdFlagIndex);
        userInfo.userId = c.getString(userUserIdIndex);
        userInfo.password = c.getString(userPasswordIndex);
        userInfo.name = c.getString(userNameIndex);
        userInfo.company = c.getString(userCompanyIndex);
        userInfo.picture = c.getString(userPictureIndex);
        userInfo.phone = c.getString(userPhoneIndex);
        userInfo.email = c.getString(userEmailIndex);
        userInfo.address = c.getString(userAddressIndex);
        userInfo.authorityKind = c.getString(userAuthorityKindIndex);
        userInfo.phone_1 = c.getString(userPhone1Index);
        userInfo.phone_2 = c.getString(userPhone2Index);
        userInfo.cellPhone_1 = c.getString(userCellPhone1Index);
        userInfo.cellPhone_2 = c.getString(userCellPhone2Index);
        userInfo.businessCardCode = c.getString(userBusinessCardCodeIndex);
        userInfo.businessCardShareFlag = c.getString(userBusinessCardShareFlagIndex);
        userInfo.nationCode = c.getString(userNationCodeIndex);
        userInfo.platform = c.getString(userPlatformIndex);
        userInfo.regDate = c.getString(userRegDateIndex);
        userInfo.modDate = c.getString(userModDateIndex);
        userInfo.duty = c.getString(userDutyIndex);


        Log.d(TAG,"getUserInfo 완료");
        return userInfo;
    }
    //세션 끊기면 테이블 아예 비워버려
    public synchronized void deleteUserInfo() {
        String sql = "delete from " + MiceDB._USER_INFO_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteUserInfo 완료");
    }
    //세션 테이블 레코드 개수 받아오기
    public synchronized int getUserInfoCount(){
        String sql = "select * from " + MiceDB._USER_INFO_TABLE_NAME+ " ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0; // error?

        int ret = c.getCount();

        c.close();

        return ret;
    }


    /*
   //SQLITE 접근 SAMPLE
    //스케쥴 받아오기 관련 쿼리


    //INSERT SAMPLE
    // 스케쥴 객체를 schedule 테이블에 insert하는 함수
    public synchronized void setScheduleDB(ScheduleInfo scheduleInfo) {

        ContentValues values;

        values = new ContentValues();
        Date date = new Date();

        values.put(MynahDB._SCHEDULE_COL_SUMMARY, scheduleInfo.scheduleName);
        values.put(MynahDB._SCHEDULE_COL_SCHEDULE_DATE, scheduleInfo.scheduleDate);
        values.put(MynahDB._SCHEDULE_COL_SCHEDULE_TIME, scheduleInfo.scheduleTime);
        values.put(MynahDB._SCHEDULE_COL_CREATED_DATE, scheduleInfo.scheduleCreatedDate);

        dbh.mDB.insert(MynahDB._SCHEDULE_TABLE_NAME, null, values);
        //Log.d(TAG,"setScheduleDB 완료");
    }

    //INSERT 여러개 SAMPLE
    //여러개의 스케쥴 한번에 때려박
    public synchronized void setSchedulesOnDateDB(SchedulesOnDateInfo schedulesOnDateInfo){
        for(ScheduleInfo sInfo : schedulesOnDateInfo.scheduleList){
            setScheduleDB(sInfo);
        }
        Log.d(TAG,"setSchedulesOnDateDB 완료");
    }


    //DELETE SAMPLE
    //스케쥴에서 created date 찾아서 딜리트 하는거
    public synchronized  void deleteSchedulesByCreatedDate(String createdDate){
        String sql = "delete from " + MynahDB._SCHEDULE_TABLE_NAME + " where "
                + MynahDB._SCHEDULE_COL_CREATED_DATE + "= '" + createdDate.trim()
                + "' ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteSchedulesByCreatedDate 완료");
    }


    //DELETE SAMPLE
    //스케쥴에 날짜 찾아서 싸그리 딜리트 하는거
    public synchronized void deleteSchedulesByDate(String date) {
        String sql = "delete from " + MynahDB._SCHEDULE_TABLE_NAME + " where "
                + MynahDB._SCHEDULE_COL_SCHEDULE_DATE + "= '" + date.trim()
                + "' ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteSchedulesByDate 완료");
    }

    //DELETE SAMPLE
    //스케쥴 테이블 비우기
    public synchronized void deleteSchedulesAll() {
        String sql = "delete from " + MynahDB._SCHEDULE_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteSchedulesAll 완료");
    }


    //SELECT COUNT SAMPLE
    //스케줄 테이블의 레코드 개수
    public synchronized int getSchedulesCount(){
        String sql = "select * from " + MynahDB._SCHEDULE_TABLE_NAME+ " ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0; // error?

        return c.getCount();
    }


    //날짜로 스케쥴 조나 받아오기
    public synchronized SchedulesOnDateInfo getSchedulesByDateTimeDB(String date) {
        SchedulesOnDateInfo schedulesOnDateInfo = new SchedulesOnDateInfo();
        ScheduleInfo scheduleInfo;

        String sql = "select * from " + MynahDB._SCHEDULE_TABLE_NAME + " where "
                + MynahDB._SCHEDULE_COL_SCHEDULE_DATE + " = "
                + "'" + date.trim()+ "'"
                + " order by "
                + MynahDB._SCHEDULE_COL_SCHEDULE_TIME
                + " ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return schedulesOnDateInfo; // error?

        int date_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_SCHEDULE_DATE);
        int time_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_SCHEDULE_TIME);
        int summary_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_SUMMARY);
        int created_date_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_CREATED_DATE);


        while (!c.isAfterLast()) {
            scheduleInfo = new ScheduleInfo();
            scheduleInfo.scheduleName = c.getString(summary_index);
            scheduleInfo.scheduleDate = c.getString(date_index);
            scheduleInfo.scheduleTime = c.getString(time_index);
            scheduleInfo.scheduleCreatedDate = c.getString(created_date_index);

            schedulesOnDateInfo.scheduleList.add(scheduleInfo);

            c.moveToNext();
        }

        Log.d(TAG,"getSchedulesByDateTimeDB 완료");
        return schedulesOnDateInfo;
    }


    //세션 갖는 유져를 세션테이블이 저장
    public synchronized void setSessionUserDB(SessionUserInfo suInfo) {

        ContentValues values;

        values = new ContentValues();
        Date date = new Date();

        values.put(MynahDB._SESSION_USER_COL_USER_ID, suInfo.userId);
        values.put(MynahDB._SESSION_USER_COL_PRODUCT_ID, suInfo.productId);
        values.put(MynahDB._SESSION_USER_COL_REGISTRATION_ID, suInfo.registrationId);
        values.put(MynahDB._SESSION_USER_COL_USER_NAME, suInfo.userName);
        values.put(MynahDB._SESSION_USER_COL_GENDER_FLAG, suInfo.genderFlag);
        values.put(MynahDB._SESSION_USER_COL_REPRESENTATIVE_FLAG, suInfo.representativeFlag);
        values.put(MynahDB._SESSION_USER_COL_IN_HOME_FLAG, suInfo.inHomeFlag);
        values.put(MynahDB._SESSION_USER_COL_DEVICE_ID, suInfo.deviceId);
        values.put(MynahDB._SESSION_USER_COL_PASSWORD, suInfo.password);
        values.put(MynahDB._SESSION_USER_COL_INOUT_TIME, suInfo.inoutTime);

        dbh.mDB.insert(MynahDB._SESSION_USER_TABLE_NAME, null, values);
        Log.d(TAG,"setSessionUserDB 완료");
    }


    //현재 세션테이블에 있는 애새끼 불러오기
    public synchronized SessionUserInfo getSessionUserDB(){
        SessionUserInfo sessionUserInfo = new SessionUserInfo();

        String sql = "select * from " + MynahDB._SESSION_USER_TABLE_NAME + " limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return sessionUserInfo; // error?

        int date_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_SCHEDULE_DATE);
        int time_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_SCHEDULE_TIME);
        int summary_index = c.getColumnIndex(MynahDB._SCHEDULE_COL_SUMMARY);

        int user_id_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_USER_ID);
        int product_id_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_PRODUCT_ID);
        int registration_id_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_REGISTRATION_ID);
        int user_name_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_USER_NAME);
        int gender_flag_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_GENDER_FLAG);
        int representative_flag_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_REPRESENTATIVE_FLAG);
        int in_home_flag_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_IN_HOME_FLAG);
        int device_id_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_DEVICE_ID);
        int password_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_PASSWORD);
        int inout_time_index = c.getColumnIndex(MynahDB._SESSION_USER_COL_INOUT_TIME);

        sessionUserInfo.userId = c.getString(user_id_index);
        sessionUserInfo.productId = c.getString(product_id_index);
        sessionUserInfo.registrationId = c.getString(registration_id_index);
        sessionUserInfo.userName = c.getString(user_name_index);
        sessionUserInfo.genderFlag = c.getString(gender_flag_index);
        sessionUserInfo.representativeFlag = c.getString(representative_flag_index);
        sessionUserInfo.inHomeFlag = c.getString(in_home_flag_index);
        sessionUserInfo.deviceId = c.getString(device_id_index);
        sessionUserInfo.password = c.getString(password_index);
        sessionUserInfo.inoutTime = c.getString(inout_time_index);

        Log.d(TAG,"getSessionUserDB 완료");
        return sessionUserInfo;
    }


    //세션 끊기면 테이블 아예 비워버려
    public synchronized void deleteSessionUser() {
        String sql = "delete from " + MynahDB._SESSION_USER_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteSessionUser 완료");
    }

    */
}
