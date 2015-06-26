package com.infjay.mice.database;

import android.provider.BaseColumns;

/**
 * Created by HJHOME on 2015-06-26.
 */
public class MiceDB {

    /*
    //로그인된 유져 세션을 위한 테이블
    public static final String _SESSION_USER_TABLE_NAME = "session_user";
    public static final String _SESSION_USER_COL_USER_ID = "user_id";
    public static final String _SESSION_USER_COL_PRODUCT_ID = "product_id";
    public static final String _SESSION_USER_COL_REGISTRATION_ID = "registration_id";
    public static final String _SESSION_USER_COL_USER_NAME = "user_name";
    public static final String _SESSION_USER_COL_GENDER_FLAG = "gender_flag";
    public static final String _SESSION_USER_COL_REPRESENTATIVE_FLAG = "representative_flag";
    public static final String _SESSION_USER_COL_IN_HOME_FLAG = "in_home_flag";
    public static final String _SESSION_USER_COL_DEVICE_ID = "device_id";
    public static final String _SESSION_USER_COL_PASSWORD = "password";
    public static final String _SESSION_USER_COL_INOUT_TIME = "inout_time";

    */

    //메모 테이블 컬럼
    public static final String _MEMO_TABLE_NAME = "memo_info";
    public static final String _MEMO_MEMO_SEQ = "memo_seq";
    public static final String _MEMO_CONTENTS = "contents";
    public static final String _MEMO_REG_DATE = "reg_date";
    public static final String _MEMO_MOD_DATE = "mod_date";


    //디비 생성용
    public static final class CreateDB implements BaseColumns {

        public static final String _CREATE_MEMO_TABLE = "create table " +_MEMO_TABLE_NAME
                + " (" + _MEMO_MEMO_SEQ + " integer primary key autoincrement, "
                + _MEMO_CONTENTS + " text, "
                + _MEMO_REG_DATE + " datetime, "
                + _MEMO_MOD_DATE + " datetime "
                + " ); ";

        /*
        //세션 유지를 위해 만든 세션사용자 테이블
        public static final String _CREATE_SESSION_USER_TABLE = "create table " + _SESSION_USER_TABLE_NAME
                + " (" + _SESSION_USER_COL_USER_ID + " text, "
                + _SESSION_USER_COL_PRODUCT_ID + " text, "
                + _SESSION_USER_COL_REGISTRATION_ID + " text, "
                + _SESSION_USER_COL_USER_NAME + " text, "
                + _SESSION_USER_COL_GENDER_FLAG + " text, "
                + _SESSION_USER_COL_REPRESENTATIVE_FLAG + " text, "
                + _SESSION_USER_COL_IN_HOME_FLAG + " text, "
                + _SESSION_USER_COL_DEVICE_ID + " text, "
                + _SESSION_USER_COL_PASSWORD + " text, "
                + _SESSION_USER_COL_INOUT_TIME + " datetime, "
                + " primary key(" + _SESSION_USER_COL_USER_ID + ") ); ";

         */
    }
}
