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

    //유저 정보 테이블 컬럼
    public static final String _USER_INFO_TABLE_NAME = "user_info";
    public static final String _USER_USER_SEQ = "user_seq";
    public static final String _USER_ID_FLAG = "id_flag";
    public static final String _USER_USER_ID = "user_id";
    public static final String _USER_PASSWORD = "password";
    public static final String _USER_NAME = "name";
    public static final String _USER_COMPANY = "company";
    public static final String _USER_PICTURE = "picture";
    public static final String _USER_PHONE = "phone";
    public static final String _USER_EMAIL = "email";
    public static final String _USER_ADDRESS = "address";
    public static final String _USER_AUTHORITY_KIND = "authority_kind";
    public static final String _USER_PHONE_1 = "phone_1";
    public static final String _USER_PHONE_2 = "phone_2";
    public static final String _USER_CELL_PHONE_1 = "cell_phone_1";
    public static final String _USER_CELL_PHONE_2 = "cell_phone_2";
    public static final String _USER_BUSINESS_CARD_CODE = "business_card_code";
    public static final String _USER_BUSINESS_CARD_SHARE_FLAG = "business_card_share_flag";
    public static final String _USER_NATION_CODE = "nation_code";
    public static final String _USER_PLATFORM = "platform";
    public static final String _USER_REG_DATE = "reg_date";
    public static final String _USER_MOD_DATE = "mod_date";
    public static final String _USER_DUTY = "duty";

   //디비 생성용
    public static final class CreateDB implements BaseColumns {

        //메모관련 테이블
        public static final String _CREATE_MEMO_TABLE = "create table " +_MEMO_TABLE_NAME
                + " (" + _MEMO_MEMO_SEQ + " integer primary key autoincrement, "
                + _MEMO_CONTENTS + " text, "
                + _MEMO_REG_DATE + " datetime, "
                + _MEMO_MOD_DATE + " datetime "
<<<<<<< HEAD
                + " ); ";

        //유저정보 관련 테이블
        public static final String _CREATE_USER_INFO_TABLE = "create table " +_USER_INFO_TABLE_NAME
                + " (" + _USER_USER_SEQ + " text, "
                + _USER_ID_FLAG + " text, "
                + _USER_USER_ID + " text, "
                + _USER_PASSWORD + " text, "
                + _USER_NAME + " text, "
                + _USER_COMPANY + " text, "
                + _USER_PICTURE + " text, "
                + _USER_PHONE + " text, "
                + _USER_EMAIL + " text, "
                + _USER_ADDRESS + " text, "
                + _USER_AUTHORITY_KIND + " text, "
                + _USER_PHONE_1 + " text, "
                + _USER_PHONE_2 + " text, "
                + _USER_CELL_PHONE_1 + " text, "
                + _USER_CELL_PHONE_2 + " text, "
                + _USER_BUSINESS_CARD_CODE + " text, "
                + _USER_BUSINESS_CARD_SHARE_FLAG + " text, "
                + _USER_NATION_CODE + " text, "
                + _USER_PLATFORM + " text, "
                + _USER_REG_DATE + " datetime, "
                + _USER_MOD_DATE + " datetime, "
                + _USER_DUTY + " text"
=======
>>>>>>> 5af52599da4b256364fb2e4404afe31e25a88259
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
