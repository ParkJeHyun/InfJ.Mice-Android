package com.infjay.mice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.artifacts.MessageInfo;
import com.infjay.mice.artifacts.MyScheduleInfo;
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
        Log.d(TAG, "insertConferenceInfo 완료");
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
                + "'" +
                " order by "+
                MiceDB._AGENDA_SESSION_START_TIME +
                ";";

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
        c.close();
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
        c.close();
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
        c.close();
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
        c.close();
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
        c.close();
        return arraySessionInfo;
    }


    //Sponsor
    //새 Sponsor 추가
    public synchronized void insertSponsor(ArrayList<SponsorInfo> sponsorList){
        for(int i=0;i<sponsorList.size();i++) {
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
                    ", " +
                    MiceDB._SPONSOR_ATTACHED +
                    ") " +
                    "values " +
                    "(" +
                    "'" + sponsorList.get(i).sponsorSeq + "', " +
                    "'" + sponsorList.get(i).sponsorName + "', " +
                    "'" + sponsorList.get(i).sponsorExplanation + "', " +
                    "'" + sponsorList.get(i).logoPath + "', " +
                    "'" + sponsorList.get(i).detailImagePath + "', " +
                    "'" + sponsorList.get(i).regDate + "', " +
                    "'" + sponsorList.get(i).modDate + "', " +
                    "'" + sponsorList.get(i).attached +"'" +
                    "); ";

            dbh.mDB.execSQL(sql);
        }
        Log.d(TAG,"insertSponsor 완료");
    }
    //모든 sponsor 불러오기
    public synchronized ArrayList<SponsorInfo> getAllSponsor(){
        ArrayList<SponsorInfo> arraySponsorInfo = new ArrayList<SponsorInfo>();

        String sql = "select * from " + MiceDB._SPONSOR_TABLE_NAME
                + " order by "+
                MiceDB._SPONSOR_SEQ +
                ";";

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
        int attachedIndex = c.getColumnIndex(MiceDB._SPONSOR_ATTACHED);

        while (!c.isAfterLast()) {
            sponsorInfo = new SponsorInfo();

            sponsorInfo.sponsorSeq = c.getString(sponsorSeqIndex);
            sponsorInfo.sponsorName = c.getString(nameIndex);
            sponsorInfo.sponsorExplanation = c.getString(explanationIndex);
            sponsorInfo.logoPath = c.getString(logoIndex);
            sponsorInfo.detailImagePath = c.getString(detailImgIndex);
            sponsorInfo.regDate = c.getString(regDateIndex);
            sponsorInfo.modDate = c.getString(modDateIndex);
            sponsorInfo.attached = c.getString(attachedIndex);

            arraySponsorInfo.add(sponsorInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getAllSponsor 완료");
        c.close();
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
        int attachedIndex = c.getColumnIndex(MiceDB._SPONSOR_ATTACHED);

        sponsorInfo.sponsorSeq = c.getString(sponsorSeqIndex);
        sponsorInfo.sponsorName = c.getString(nameIndex);
        sponsorInfo.sponsorExplanation = c.getString(explanationIndex);
        sponsorInfo.logoPath = c.getString(logoIndex);
        sponsorInfo.detailImagePath = c.getString(detailImgIndex);
        sponsorInfo.regDate = c.getString(regDateIndex);
        sponsorInfo.modDate = c.getString(modDateIndex);
        sponsorInfo.attached = c.getString(attachedIndex);

        c.close();
        Log.d(TAG, "getSponsorBySeq 완료");
        return sponsorInfo;
    }
    //Sponsor 레코드 수 불러오기
    public synchronized int getSponsorCount(){
        String sql = "select * from " + MiceDB._SPONSOR_TABLE_NAME+ " ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0; // error?

        int ret = c.getCount();

        c.close();

        return ret;
    }
    //Seq로 sponsor 하나 지우기
    public synchronized void deleteSponsorBySeq(String sponsorSeq){
        String sql = "delete from " + MiceDB._SPONSOR_TABLE_NAME +
                " where " +
                MiceDB._SPONSOR_SEQ +
                " = '" +
                sponsorSeq + "';" ;

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteSponsor 완료");
    }
    //모든 sponsor 삭제
    public void deleteAllSponsor(){
        String sql = "delete from " + MiceDB._SPONSOR_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteAllSponsor 완료");
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
        Log.d(TAG, "insertCoupon 완료");
    }
    //Coupon 레코드 수 불러오기
    public synchronized int getCouponCount(){
        String sql = "select * from " + MiceDB._COUPON_TABLE_NAME + " ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0; // error?

        int ret = c.getCount();

        c.close();

        return ret;
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
        c.close();
        return arrayCouponInfo;
    }
    //Seq에 해당하는 Coupon 하나 삭제
    public synchronized void deleteCouponBySeq(String couponSeq){
        String sql = "delete from " + MiceDB._COUPON_TABLE_NAME +
                " where " +
                MiceDB._COUPON_SEQ +
                " = '" +
                couponSeq + "';" ;

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteCoupon 완료");
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
                ", " +
                MiceDB._BINDER_SESSION_DATE +
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
                "'" + sessionInfo.modDate + "', " +
                "'" + sessionInfo.sessionDate + "'" +
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
        c.close();
        return arraySessionInfo;
    }
    //date로 Binder에 Session 불러오기
    public synchronized ArrayList<AgendaSessionInfo> getSessionFromBinderBySessionDate(String sessionDate){
        ArrayList<AgendaSessionInfo> sessionInfoList = new ArrayList<AgendaSessionInfo>();

        String sql = "select * from "
                + MiceDB._BINDER_SESSION_TABLE_NAME
                + " where "
                + MiceDB._BINDER_SESSION_DATE
                + " = '"
                + sessionDate
                + "'" +
                " order by "+
                MiceDB._BINDER_SESSION_START_TIME +
                " ;";

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

        Log.i(TAG, "getSessionFromBinderBySessionDate 완료");
        c.close();
        return sessionInfoList;
    }
    //UserSeq랑 sessionSeq로 세션이 있는지 확인하기
    public synchronized int getSessionExistInBinderByUserSeqAndSessionSeq(String userSeq,String sessionSeq){
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
        Log.d(TAG,"getSessionCountByUserSeqAndSessionSeq 완료");

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
        Log.d(TAG, "getMemoByMemoSeq 완료");
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
        c.close();
        return arrayMemoInfo;
    }


    //CardHolder
    //Insert Card
    public synchronized void insertBusinessCard(String userSeq, BusinessCardInfo card){
        String sql = "insert into " +
                MiceDB._MY_CARD_HOLDER_TABLE_NAME +
                "(" +
                MiceDB._MY_CARD_HOLDER_USER_SEQ +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_SEQ +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_ID_FLAG +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_ID +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_NAME +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_COMPANY +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_DUTY +
                ", " +
                MiceDB._MY_CARD__HOLDER_CARD_PICTURE +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_PHONE +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_EMAIL +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_ADDRESS +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_AUTHORITY_KIND +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_PHONE1 +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_PHONE2 +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_CELLPHONE1 +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_CELLPHONE2 +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_CODE +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_SHARE_FLAG +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_NATION_CODE +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_PLATFORM +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_REG_DATE +
                ", " +
                MiceDB._MY_CARD_HOLDER_CARD_MOD_DATE +
                ") " +
                "values " +
                "(" +
                "'" + userSeq + "', " +
                "'" + card.userSeq + "', " +
                "'" + card.idFlag + "', " +
                "'" + card.userId + "', " +
                "'" + card.name +"', " +
                "'" + card.company +"', " +
                "'" + card.duty +"', " +
                "'" + card.picturePath +"', " +
                "'" + card.phone +"', " +
                "'" + card.email +"', " +
                "'" + card.address +"', " +
                "'" + card.authorityKind +"', " +
                "'" + card.phone_1 +"', " +
                "'" + card.phone_2 +"', " +
                "'" + card.cellPhone_1 +"', " +
                "'" + card.cellPhone_2 +"', " +
                "'" + card.businessCardCode +"', " +
                "'" + card.businessCardShareFlag +"', " +
                "'" + card.nationCode +"', " +
                "'" + card.platform +"', " +
                "'" + card.regDate +"', " +
                "'" + card.modDate +"' " +
                "); ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "insertBusinessCard 완료");
    }
    //userSeq로 모든 BusinessCard 불러오기
    public synchronized ArrayList<BusinessCardInfo> getBusinessCardByUserSeq(String userSeq){
        ArrayList<BusinessCardInfo> arrayBusinessCardInfo = new ArrayList<BusinessCardInfo>();

        String sql = "select * from " + MiceDB._MY_CARD_HOLDER_TABLE_NAME
                + " where "
                + MiceDB._MY_CARD_HOLDER_USER_SEQ
                + " = '"
                + userSeq + "'"
                + " order by "+
                MiceDB._MY_CARD_HOLDER_CARD_NAME +
                " desc;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        BusinessCardInfo businessCardInfo;

        int cardSeqIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_SEQ);
        int idFlagIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_ID_FLAG);
        int idIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_ID);
        int nameIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_NAME);
        int companyIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_COMPANY);
        int dutyIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_DUTY);
        int pictureIndex = c.getColumnIndex(MiceDB._MY_CARD__HOLDER_CARD_PICTURE);
        int phoneIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PHONE);
        int emailIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_EMAIL);
        int addressIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_ADDRESS);
        int authorityKindIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_AUTHORITY_KIND);
        int phone1Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PHONE1);
        int phone2Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PHONE2);
        int cellPhone1Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_CELLPHONE1);
        int cellPhone2Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_CELLPHONE2);
        int codeIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_CODE);
        int shareFlagIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_SHARE_FLAG);
        int nationCodeIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_NATION_CODE);
        int platformIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PLATFORM);
        int regDateIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_REG_DATE);
        int modDateIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_MOD_DATE);

        while (!c.isAfterLast()) {
            businessCardInfo = new BusinessCardInfo();

            businessCardInfo.userSeq = c.getString(cardSeqIndex);
            businessCardInfo.idFlag = c.getString(idFlagIndex);
            businessCardInfo.userId = c.getString(idIndex);
            businessCardInfo.name = c.getString(nameIndex);
            businessCardInfo.company = c.getString(companyIndex);
            businessCardInfo.duty = c.getString(dutyIndex);
            businessCardInfo.picturePath = c.getString(pictureIndex);
            businessCardInfo.phone = c.getString(phoneIndex);
            businessCardInfo.email = c.getString(emailIndex);
            businessCardInfo.address = c.getString(addressIndex);
            businessCardInfo.authorityKind = c.getString(authorityKindIndex);
            businessCardInfo.phone_1 = c.getString(phone1Index);
            businessCardInfo.phone_2 = c.getString(phone2Index);
            businessCardInfo.cellPhone_1 = c.getString(cellPhone1Index);
            businessCardInfo.cellPhone_2 = c.getString(cellPhone2Index);
            businessCardInfo.businessCardCode = c.getString(codeIndex);
            businessCardInfo.businessCardShareFlag = c.getString(shareFlagIndex);
            businessCardInfo.nationCode = c.getString(nationCodeIndex);
            businessCardInfo.platform = c.getString(platformIndex);
            businessCardInfo.regDate = c.getString(regDateIndex);
            businessCardInfo.modDate = c.getString(modDateIndex);

            arrayBusinessCardInfo.add(businessCardInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getBusinessCardInfoByUserSeq 완료");
        c.close();
        return arrayBusinessCardInfo;
    }
    //UserSeq와 CardSeq로 BusinessCard 하나 불러오기
    public synchronized BusinessCardInfo getBusinessCardInfoByUserSeqAndCardSeq(String userSeq, String cardSeq){

        String sql = "select * from " + MiceDB._MY_CARD_HOLDER_TABLE_NAME
                + " where "
                + MiceDB._MY_CARD_HOLDER_USER_SEQ
                + " = '"
                + userSeq + "'"
                + " AND "
                + MiceDB._MY_CARD_HOLDER_CARD_SEQ
                + " = '"
                + cardSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        BusinessCardInfo businessCardInfo;

        int cardSeqIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_SEQ);
        int idFlagIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_ID_FLAG);
        int idIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_ID);
        int nameIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_NAME);
        int companyIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_COMPANY);
        int dutyIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_DUTY);
        int pictureIndex = c.getColumnIndex(MiceDB._MY_CARD__HOLDER_CARD_PICTURE);
        int phoneIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PHONE);
        int emailIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_EMAIL);
        int addressIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_ADDRESS);
        int authorityKindIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_AUTHORITY_KIND);
        int phone1Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PHONE1);
        int phone2Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PHONE2);
        int cellPhone1Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_CELLPHONE1);
        int cellPhone2Index = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_CELLPHONE2);
        int codeIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_CODE);
        int shareFlagIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_SHARE_FLAG);
        int nationCodeIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_NATION_CODE);
        int platformIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_PLATFORM);
        int regDateIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_REG_DATE);
        int modDateIndex = c.getColumnIndex(MiceDB._MY_CARD_HOLDER_CARD_MOD_DATE);

        businessCardInfo = new BusinessCardInfo();

        businessCardInfo.userSeq = c.getString(cardSeqIndex);
        businessCardInfo.idFlag = c.getString(idFlagIndex);
        businessCardInfo.userId = c.getString(idIndex);
        businessCardInfo.name = c.getString(nameIndex);
        businessCardInfo.company = c.getString(companyIndex);
        businessCardInfo.duty = c.getString(dutyIndex);
        businessCardInfo.picturePath = c.getString(pictureIndex);
        businessCardInfo.phone = c.getString(phoneIndex);
        businessCardInfo.email = c.getString(emailIndex);
        businessCardInfo.address = c.getString(addressIndex);
        businessCardInfo.authorityKind = c.getString(authorityKindIndex);
        businessCardInfo.phone_1 = c.getString(phone1Index);
        businessCardInfo.phone_2 = c.getString(phone2Index);
        businessCardInfo.cellPhone_1 = c.getString(cellPhone1Index);
        businessCardInfo.cellPhone_2 = c.getString(cellPhone2Index);
        businessCardInfo.businessCardCode = c.getString(codeIndex);
        businessCardInfo.businessCardShareFlag = c.getString(shareFlagIndex);
        businessCardInfo.nationCode = c.getString(nationCodeIndex);
        businessCardInfo.platform = c.getString(platformIndex);
        businessCardInfo.regDate = c.getString(regDateIndex);
        businessCardInfo.modDate = c.getString(modDateIndex);

        Log.i(TAG, "getBusinessCardInfoByUserSeqAndCardSeq 완료");
        c.close();
        return businessCardInfo;
    }
    //userSeq와 cardSeq로 BusinessCardInfo 하나 지우기
    public synchronized void deleteBusinessCardInfoByUserSeqAndCardSeq(String userSeq, String cardSeq){
        String sql = "delete from " +
                MiceDB._MY_CARD_HOLDER_TABLE_NAME +
                " where " +
                MiceDB._MY_CARD_HOLDER_USER_SEQ +
                " = '" +
                userSeq +
                "' AND " +
                MiceDB._MY_CARD_HOLDER_CARD_SEQ +
                " = '" +
                cardSeq + "';";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteBusinessCardInfoByUserSeqAndCardSeq 완료");
    }
    //MyCardHolder 테이블 비우기
    public synchronized  void deleteCardHolder(){
        String sql = "delete from " +
                MiceDB._MY_CARD_HOLDER_TABLE_NAME +
                ";";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteCardHolder 완료");
    }


    //Message
    //Insert Message
    public synchronized String insertMessageInfo(MessageInfo message){
        int count = getMessageCountBySeq(message.messageSeq);
        if(count!=0){
            return "This messageSeq is Aleady Exist!";
        }
        String sql = "insert into " +
                MiceDB._MESSAGE_TABLE_NAME +
                "(" +
                MiceDB._MESSAGE_SEQ +
                ", " +
                MiceDB._MESSAGE_SEND_USER_SEQ +
                ", " +
                MiceDB._MESSAGE_RECEIVE_USER_SEQ +
                ", " +
                MiceDB._MESSAGE_TEXT +
                ", " +
                MiceDB._MESSAGE_SEND_TIME +
                ", " +
                MiceDB._MESSAGE_SENDER_NAME +
                ", " +
                MiceDB._MESSAGE_RECEIVER_NAME +
                ") " +
                "values " +
                "(" +
                "'" + message.messageSeq + "', " +
                "'" + message.senderUserSeq + "', " +
                "'" + message.receiverUserSeq + "', " +
                "'" + message.messageText + "', " +
                "'" + message.sendTime + "', " +
                "'" + message.senderName + "', " +
                "'" + message.receiverName + "'" +
                "); ";

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "insertMessage 완료");
        return "MessageInsert Succeess";
    }
    //messageSeq로 DB에 데이터가 있는지 Count불러오기
    public synchronized int getMessageCountBySeq(String messageSeq){
        String sql = "select * from " + MiceDB._MESSAGE_TABLE_NAME +
                    " where " + MiceDB._MESSAGE_SEQ +
                    " = '" + messageSeq + "' ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0; // error?

        int ret = c.getCount();

        c.close();

        return ret;
    }
    //senderSeq로 message모두 불러오기
    public synchronized ArrayList<MessageInfo> getMessageBySender(String senderSeq){
        ArrayList<MessageInfo> arrayMessageInfo = new ArrayList<MessageInfo>();

        String sql = "select * from " + MiceDB._MESSAGE_TABLE_NAME
                + " where "
                + MiceDB._MESSAGE_SEND_USER_SEQ
                + " = '"
                + senderSeq + "'"
                + " order by "+
                MiceDB._MESSAGE_SEND_TIME +
                " desc;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        MessageInfo messageInfo;

        int messageSeqIndex = c.getColumnIndex(MiceDB._MESSAGE_SEQ);
        int senderIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_USER_SEQ);
        int receiverIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVE_USER_SEQ);
        int textIndex = c.getColumnIndex(MiceDB._MESSAGE_TEXT);
        int sendTimeIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_TIME);
        int sendNameIndex = c.getColumnIndex(MiceDB._MESSAGE_SENDER_NAME);
        int receiverNameIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVER_NAME);

        while (!c.isAfterLast()) {
            messageInfo = new MessageInfo();

            messageInfo.messageSeq = c.getString(messageSeqIndex);
            messageInfo.senderUserSeq = c.getString(senderIndex);
            messageInfo.receiverUserSeq = c.getString(receiverIndex);
            messageInfo.messageText = c.getString(textIndex);
            messageInfo.sendTime = c.getString(sendTimeIndex);
            messageInfo.senderName = c.getString(sendNameIndex);
            messageInfo.receiverName = c.getString(receiverNameIndex);

            arrayMessageInfo.add(messageInfo);
            c.moveToNext();

        }

        Log.i(TAG, "geMessageBySenderSeq 완료");
        c.close();
        return arrayMessageInfo;
    }
    //receiverSeq로 message모두 불러오기
    public synchronized ArrayList<MessageInfo> getMessageByReceiver (String receiverSeq){
        ArrayList<MessageInfo> arrayMessageInfo = new ArrayList<MessageInfo>();

        String sql = "select * from " + MiceDB._MESSAGE_TABLE_NAME
                + " where "
                + MiceDB._MESSAGE_RECEIVE_USER_SEQ
                + " = '"
                + receiverSeq + "'"
                + " order by "+
                MiceDB._MESSAGE_SEND_TIME +
                " desc;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        MessageInfo messageInfo;

        int messageSeqIndex = c.getColumnIndex(MiceDB._MESSAGE_SEQ);
        int senderIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_USER_SEQ);
        int receiverIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVE_USER_SEQ);
        int textIndex = c.getColumnIndex(MiceDB._MESSAGE_TEXT);
        int sendTimeIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_TIME);
        int sendNameIndex = c.getColumnIndex(MiceDB._MESSAGE_SENDER_NAME);
        int receiverNameIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVER_NAME);

        while (!c.isAfterLast()) {
            messageInfo = new MessageInfo();

            messageInfo.messageSeq = c.getString(messageSeqIndex);
            messageInfo.senderUserSeq = c.getString(senderIndex);
            messageInfo.receiverUserSeq = c.getString(receiverIndex);
            messageInfo.messageText = c.getString(textIndex);
            messageInfo.sendTime = c.getString(sendTimeIndex);
            messageInfo.senderName = c.getString(sendNameIndex);
            messageInfo.receiverName = c.getString(receiverNameIndex);

            arrayMessageInfo.add(messageInfo);
            c.moveToNext();
        }

        Log.i(TAG, "getMessageByReceiverSeq 완료");
        c.close();
        return arrayMessageInfo;
    }
    //messageSeq로 message하나 불러오기
    public synchronized MessageInfo getMeesageBySeq(String messageSeq){
        MessageInfo messageInfo;

        String sql = "select * from " + MiceDB._MESSAGE_TABLE_NAME
                + " where "
                + MiceDB._MESSAGE_SEQ
                + " = '"
                + messageSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        messageInfo = new MessageInfo();

        int messageSeqIndex = c.getColumnIndex(MiceDB._MESSAGE_SEQ);
        int senderIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_USER_SEQ);
        int receiverIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVE_USER_SEQ);
        int textIndex = c.getColumnIndex(MiceDB._MESSAGE_TEXT);
        int sendTimeIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_TIME);
        int sendNameIndex = c.getColumnIndex(MiceDB._MESSAGE_SENDER_NAME);
        int receiverNameIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVER_NAME);

        messageInfo.messageSeq = c.getString(messageSeqIndex);
        messageInfo.senderUserSeq = c.getString(senderIndex);
        messageInfo.receiverUserSeq = c.getString(receiverIndex);
        messageInfo.messageText = c.getString(textIndex);
        messageInfo.sendTime = c.getString(sendTimeIndex);
        messageInfo.senderName = c.getString(sendNameIndex);
        messageInfo.receiverName = c.getString(receiverNameIndex);

        Log.i(TAG, "getMeesageBySeq 완료");
        c.close();
        return messageInfo;
    }
    //senderSeq,RecevierSeq 모두 사용해서 MessageList부르기
    public synchronized ArrayList<MessageInfo> getMessageByTwoUser(String user1,String user2){
        ArrayList<MessageInfo> arrayMessageInfo = new ArrayList<MessageInfo>();

        String sql = "select * from " + MiceDB._MESSAGE_TABLE_NAME
                + " where ("
                + MiceDB._MESSAGE_SEND_USER_SEQ
                + " = '"
                + user1 + "'"
                + " AND "
                + MiceDB._MESSAGE_RECEIVE_USER_SEQ
                + " = '"
                + user2 + "')"
                + " OR ("
                + MiceDB._MESSAGE_SEND_USER_SEQ
                + " = '"
                + user2 + "'"
                + " AND "
                + MiceDB._MESSAGE_RECEIVE_USER_SEQ
                + " = '"
                + user1 + "')"
                + " order by "+
                MiceDB._MESSAGE_SEND_TIME +
                " ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        MessageInfo messageInfo;

        int messageSeqIndex = c.getColumnIndex(MiceDB._MESSAGE_SEQ);
        int senderIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_USER_SEQ);
        int receiverIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVE_USER_SEQ);
        int textIndex = c.getColumnIndex(MiceDB._MESSAGE_TEXT);
        int sendTimeIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_TIME);
        int sendNameIndex = c.getColumnIndex(MiceDB._MESSAGE_SENDER_NAME);
        int receiverNameIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVER_NAME);


        while (!c.isAfterLast()) {
            messageInfo = new MessageInfo();

            messageInfo.messageSeq = c.getString(messageSeqIndex);
            messageInfo.senderUserSeq = c.getString(senderIndex);
            messageInfo.receiverUserSeq = c.getString(receiverIndex);
            messageInfo.messageText = c.getString(textIndex);
            messageInfo.sendTime = c.getString(sendTimeIndex);
            messageInfo.senderName = c.getString(sendNameIndex);
            messageInfo.receiverName = c.getString(receiverNameIndex);

            arrayMessageInfo.add(messageInfo);
            c.moveToNext();

        }

        c.close();
        Log.i(TAG, "getMessageByReceiverSeq 완료");
        return arrayMessageInfo;
    }
    //user1,user2와의 대화에서 가장 최신의 시간 불러오기
    public synchronized String getRecentTime(String user1,String user2){
        String sql = "select * from " + MiceDB._MESSAGE_TABLE_NAME
                + " where ("
                + MiceDB._MESSAGE_SEND_USER_SEQ
                + " = '"
                + user1 + "'"
                + " AND "
                + MiceDB._MESSAGE_RECEIVE_USER_SEQ
                + " = '"
                + user2 + "')"
                + " OR ("
                + MiceDB._MESSAGE_SEND_USER_SEQ
                + " = '"
                + user2 + "'"
                + " AND "
                + MiceDB._MESSAGE_RECEIVE_USER_SEQ
                + " = '"
                + user1 + "')"
                + " order by "+
                MiceDB._MESSAGE_SEND_TIME +
                " desc limit 1;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        MessageInfo messageInfo;



        int messageSeqIndex = c.getColumnIndex(MiceDB._MESSAGE_SEQ);
        int senderIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_USER_SEQ);
        int receiverIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVE_USER_SEQ);
        int textIndex = c.getColumnIndex(MiceDB._MESSAGE_TEXT);
        int sendTimeIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_TIME);
        int sendNameIndex = c.getColumnIndex(MiceDB._MESSAGE_SENDER_NAME);
        int receiverNameIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVER_NAME);

        if(c.getCount() == 0)
        {
            return "NO_MESSAGE";
        }


        messageInfo = new MessageInfo();

        messageInfo.messageSeq = c.getString(messageSeqIndex);
        messageInfo.senderUserSeq = c.getString(senderIndex);
        messageInfo.receiverUserSeq = c.getString(receiverIndex);
        messageInfo.messageText = c.getString(textIndex);
        messageInfo.sendTime = c.getString(sendTimeIndex);
        messageInfo.senderName = c.getString(sendNameIndex);
        messageInfo.receiverName = c.getString(receiverNameIndex);

        Log.i(TAG, "getRecentTime 완료");
        c.close();
        return messageInfo.sendTime;

    }
    //가장 최신의 대화목록들 불러오기
    public synchronized ArrayList<MessageInfo> getRecentMessageList(String mySeq){
        ArrayList<MessageInfo> messageList = new ArrayList<MessageInfo>();

        String sql = "select *"
                    +" from " + MiceDB._MESSAGE_TABLE_NAME + " A, (select people, max("+MiceDB._MESSAGE_SEQ+") as max_seq"
                                                                    + " from ("
                                                                        + " select " + MiceDB._MESSAGE_RECEIVE_USER_SEQ +" as people, "
                                                                                      + MiceDB._MESSAGE_SEQ
                                                                        + " from " + MiceDB._MESSAGE_TABLE_NAME
                                                                        + " where " + MiceDB._MESSAGE_RECEIVE_USER_SEQ +" != '" +mySeq +"'"
                                                                        +" union "
                                                                        + " select " + MiceDB._MESSAGE_SEND_USER_SEQ +" as people, "
                                                                                      + MiceDB._MESSAGE_SEQ
                                                                        + " from " + MiceDB._MESSAGE_TABLE_NAME
                                                                        + " where " + MiceDB._MESSAGE_SEND_USER_SEQ +" != '" +mySeq +"'"
                                                                    + " ) " + "group by people ) B"
                    + " where A." + MiceDB._MESSAGE_SEQ + " = B.max_seq;";

        Cursor c = dbh.mDB.rawQuery(sql, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        MessageInfo messageInfo;

        int messageSeqIndex = c.getColumnIndex(MiceDB._MESSAGE_SEQ);
        int senderIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_USER_SEQ);
        int receiverIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVE_USER_SEQ);
        int textIndex = c.getColumnIndex(MiceDB._MESSAGE_TEXT);
        int sendTimeIndex = c.getColumnIndex(MiceDB._MESSAGE_SEND_TIME);
        int sendNameIndex = c.getColumnIndex(MiceDB._MESSAGE_SENDER_NAME);
        int receiverNameIndex = c.getColumnIndex(MiceDB._MESSAGE_RECEIVER_NAME);


        while (!c.isAfterLast()) {
            messageInfo = new MessageInfo();

            messageInfo.messageSeq = c.getString(messageSeqIndex);
            messageInfo.senderUserSeq = c.getString(senderIndex);
            messageInfo.receiverUserSeq = c.getString(receiverIndex);
            messageInfo.messageText = c.getString(textIndex);
            messageInfo.sendTime = c.getString(sendTimeIndex);
            messageInfo.senderName = c.getString(sendNameIndex);
            messageInfo.receiverName = c.getString(receiverNameIndex);

            messageList.add(messageInfo);
            c.moveToNext();

        }

        Log.i(TAG, "getRecentMessageList 완료");

        c.close();
        return messageList;
    }
    //messageTable 비우기
    public synchronized void deleteMessageInfo() {
        String sql = "delete from " + MiceDB._MESSAGE_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteUserInfo 완료");
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
        values.put(MiceDB._USER_REG_ID, userInfo.regId);


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
        int userRegIdIndex = c.getColumnIndex(MiceDB._USER_REG_ID);


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
        userInfo.regId = c.getString(userRegIdIndex);


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


    //Indoor Map
    public synchronized void insertIndoorMapInfo(ArrayList<IndoorMapInfo> indoorMapInfo){
        for(int i=0;i<indoorMapInfo.size();i++) {
            String sql = "insert into " +
                    MiceDB._INDOOR_MAP_TABLE_NAME +
                    "(" +
                    MiceDB._INDOOR_MAP_SEQ +
                    ", " +
                    MiceDB._INDOOR_MAP_ORDER +
                    ", " +
                    MiceDB._INDOOR_MAP_TITLE +
                    ", " +
                    MiceDB._INDOOR_MAP_IMAGE +
                    ") " +
                    "values " +
                    "(" +
                    "'" + indoorMapInfo.get(i).indoorMapSeq + "', " +
                    "'" + indoorMapInfo.get(i).order + "', " +
                    "'" + indoorMapInfo.get(i).title + "', " +
                    "'" + indoorMapInfo.get(i).imagePath + "' " +
                    "); ";

            dbh.mDB.execSQL(sql);
        }
        Log.d(TAG, "insertIndoorMap 완료");

    }
    //IndoorMap레코드 개수 불러오기
    public synchronized int getIndoorMapCount(){
        String sql = "select * from " + MiceDB._INDOOR_MAP_TABLE_NAME+ " ;";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0; // error?

        int ret = c.getCount();

        c.close();

        return ret;
    }
    //MapSeq로 Map하나 받아오기
    public synchronized IndoorMapInfo getIndoorMapByMapSeq(String mapSeq){
        IndoorMapInfo indoorMapInfo;

        String sql = "select * from " + MiceDB._INDOOR_MAP_TABLE_NAME
                + " where "
                + MiceDB._INDOOR_MAP_SEQ
                + " = '"
                + mapSeq
                + "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        indoorMapInfo = new IndoorMapInfo();

        int mapSeqIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_SEQ);
        int orderIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_ORDER);
        int titleIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_TITLE);
        int imageIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_IMAGE);

        indoorMapInfo.indoorMapSeq = c.getString(mapSeqIndex);
        indoorMapInfo.order = c.getString(orderIndex);
        indoorMapInfo.title = c.getString(titleIndex);
        indoorMapInfo.imagePath = c.getString(imageIndex);

        Log.i(TAG, "getIndoorMapByMapSeq 완료");
        c.close();
        return indoorMapInfo;
    }
    //IndoorMap 싹다 불러오기
    public synchronized ArrayList<IndoorMapInfo> getAllIndoorMap(){
        ArrayList<IndoorMapInfo>arrayIndoorMap = new ArrayList<IndoorMapInfo>();
        IndoorMapInfo indoorMapInfo;
        String sql = "select * from " + MiceDB._INDOOR_MAP_TABLE_NAME;

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        int mapSeqIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_SEQ);
        int orderIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_ORDER);
        int titleIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_TITLE);
        int imageIndex = c.getColumnIndex(MiceDB._INDOOR_MAP_IMAGE);

        while (!c.isAfterLast()) {
            indoorMapInfo = new IndoorMapInfo();

            indoorMapInfo.indoorMapSeq = c.getString(mapSeqIndex);
            indoorMapInfo.order = c.getString(orderIndex);
            indoorMapInfo.title = c.getString(titleIndex);
            indoorMapInfo.imagePath = c.getString(imageIndex);

            arrayIndoorMap.add(indoorMapInfo);

            c.moveToNext();
        }

        Log.i(TAG, "getAllIndoorMap 완료");
        c.close();
        return arrayIndoorMap;
    }
    //Seq에 해당하는 IndoorMap 하나 삭제
    public synchronized void deleteIndoorMapInfoBySeq(String mapSeq){
        String sql = "delete from " + MiceDB._INDOOR_MAP_TABLE_NAME +
                " where " +
                MiceDB._INDOOR_MAP_SEQ +
                " = '" +
                mapSeq + "';" ;

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteIndoorMap 완료");
    }
    //IndoorMap Table 비우기
    public synchronized void deleteIndoorMapInfo() {
        String sql = "delete from " + MiceDB._INDOOR_MAP_TABLE_NAME
                + " ;";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"deleteIndoorMapInfo 완료");
    }

    //Schedule 관련
    //Insert schedule
    public synchronized void insertSchedule(MyScheduleInfo scheduleInfo){
        String sql = "insert into " + MiceDB._SCHEDULE_TABLE_NAME +
                "(" +
                MiceDB._SCHEDULE_PARTNER_NAME +
                ", " +
                MiceDB._SCHEDULE_COMMENTS +
                ", " +
                MiceDB._SCHEDULE_TIME +
                ") " +
                "values " +
                "(" +
                "'" + scheduleInfo.parterName + "', " +
                "'" + scheduleInfo.comment + "', " +
                "'" + scheduleInfo.time + "' " +
                "); ";
        dbh.mDB.execSQL(sql);
        Log.d(TAG,"insertSchedule 완료");
    }
    //모든 schedule 불러오기
    public synchronized ArrayList<MyScheduleInfo> getAllScheduleInfo(){
        ArrayList<MyScheduleInfo> scheduleInfoArrayList = new ArrayList<MyScheduleInfo>();
        MyScheduleInfo scheduleInfo;

        String sql = "select * from " + MiceDB._SCHEDULE_TABLE_NAME + ";";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        int seqIndex = c.getColumnIndex(MiceDB._SCHEDULE_SEQ);
        int parterIndex = c.getColumnIndex(MiceDB._SCHEDULE_PARTNER_NAME);
        int commentsIndex = c.getColumnIndex(MiceDB._SCHEDULE_COMMENTS);
        int timeIndex = c.getColumnIndex(MiceDB._SCHEDULE_TIME);

        while (!c.isAfterLast()) {
            scheduleInfo = new MyScheduleInfo();

            scheduleInfo.scheduleSeq = c.getInt(seqIndex);
            scheduleInfo.parterName = c.getString(parterIndex);
            scheduleInfo.comment = c.getString(commentsIndex);
            scheduleInfo.time = c.getString(timeIndex);

            scheduleInfoArrayList.add(scheduleInfo);

            c.moveToNext();
        }
        c.close();
        Log.i(TAG, "getAllScheduleInfo 완료");
        return scheduleInfoArrayList;
    }
    //ScheduleSeq 로 Schedule하나 불러오기
    public synchronized MyScheduleInfo getScheduleBySeq(int scheduleSeq){
        MyScheduleInfo scheduleInfo;

        String sql = "select * from " + MiceDB._SCHEDULE_TABLE_NAME +
                " where " + MiceDB._SCHEDULE_SEQ +
                " = '" + String.valueOf(scheduleSeq) +
                "' limit 1 ; ";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        scheduleInfo = new MyScheduleInfo();

        int seqIndex = c.getColumnIndex(MiceDB._SCHEDULE_SEQ);
        int parterIndex = c.getColumnIndex(MiceDB._SCHEDULE_PARTNER_NAME);
        int commentsIndex = c.getColumnIndex(MiceDB._SCHEDULE_COMMENTS);
        int timeIndex = c.getColumnIndex(MiceDB._SCHEDULE_TIME);


        scheduleInfo.scheduleSeq = c.getInt(seqIndex);
        scheduleInfo.parterName = c.getString(parterIndex);
        scheduleInfo.comment = c.getString(commentsIndex);
        scheduleInfo.time = c.getString(timeIndex);

        Log.i(TAG, "getScheduleBySeq 완료");
        c.close();
        return scheduleInfo;
    }
    //Schedule Record 수 불러오기
    public synchronized int getCountSchedule(){
        String sql = "select * from " + MiceDB._SCHEDULE_TABLE_NAME + ";";

        Cursor c = dbh.mDB.rawQuery(sql, null);

        if (c != null && c.getCount() != 0)
            c.moveToFirst();

        int count = c.getCount();
        c.close();
        Log.i(TAG, "getCountSchedule 완료");
        return count;
    }
    //ScheduleSeq로 Schedule 하나 지우기
    public synchronized void deleteScheduleBySeq(int scheduleSeq){
        String sql = "delete from " + MiceDB._SCHEDULE_TABLE_NAME +
                " where " +
                MiceDB._SCHEDULE_SEQ +
                " = '" +
                String.valueOf(scheduleSeq) + "';" ;

        dbh.mDB.execSQL(sql);
        Log.d(TAG, "deleteScheduleBySeq 완료");
        return;
    }
    //Schedule 테이블 모두 비우기
    public synchronized void delecteSchedule(){
        String sql = "delete from " + MiceDB._SCHEDULE_TABLE_NAME
                + " ;";

        dbh.mDB.execSQL(sql);
        Log.d(TAG,"delecteSchedule 완료");
        return;
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
