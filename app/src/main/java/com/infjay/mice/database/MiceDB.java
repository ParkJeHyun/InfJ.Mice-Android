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

    //ConferenceInfo 테이블 컬림
    public static final String _CONFERENCE_INFO_TABLE_NAME = "conference_info";
    public static final String _CONFERENCE_START_DATE = "start_date";
    public static final String _CONFERENCE_END_DATE = "end_date";
    public static final String _CONFERENCE_SUMMARY = "summary";
    public static final String _CONFERENCE_NAME = "name";


    //AGENDA SESSION TABLE 컬림
    public static final String _AGENDA_SESSION_TABLE_NAME = "agenda_session";
    public static final String _AGENDA_SESSION_SEQ = "session_seq";
    public static final String _AGENDA_SESSION_TITLE = "title";
    public static final String _AGENDA_SESSION_CONTENTS = "contents";
    public static final String _AGENDA_SESSION_SUMMARY = "summary";
    public static final String _AGENDA_SESSION_WRITER_SEQ = "writer_seq";
    public static final String _AGENDA_SESSION_PRESENTER_SEQ = "presenter_seq";
    public static final String _AGENDA_SESSION_START_TIME = "start_time";
    public static final String _AGENDA_SESSION_END_TIME = "end_time";
    public static final String _AGENDA_SESSION_REG_TIME = "reg_time";
    public static final String _AGENDA_SESSION_MOD_TIME = "mod_time";
    public static final String _AGENDA_SESSION_ATTACHED = "attached";
    public static final String _AGENDA_SESSION_DATE = "date";

    //SPONSOR TABLE 컬럼
    public static final String _SPONSOR_TABLE_NAME = "sponsor_info";
    public static final String _SPONSOR_SEQ = "sponsor_seq";
    public static final String _SPONSOR_NAME = "name";
    public static final String _SPONSOR_EXPLANATION = "explanation";
    public static final String _SPONSOR_LOGO = "logo";
    public static final String _SPONSOR_DETAIL_IMG = "detail_img";
    public static final String _SPONSOR_REG_DATE = "reg_date";
    public static final String _SPONSOR_MOD_DATE = "mod_date";
    public static final String _SPONSOR_ATTACHED = "attached";

    //MyCardHolder 테이블 컬럼
    public static final String _MY_CARD_HOLDER_TABLE_NAME = "my_card_holder";
    public static final String _MY_CARD_HOLDER_USER_SEQ = "user_seq";
    public static final String _MY_CARD_HOLDER_CARD_SEQ = "card_seq";
    public static final String _MY_CARD_HOLDER_CARD_ID = "id";
    public static final String _MY_CARD_HOLDER_CARD_ID_FLAG = "id_flag";
    public static final String _MY_CARD_HOLDER_CARD_NAME = "name";
    public static final String _MY_CARD_HOLDER_CARD_COMPANY = "company";
    public static final String _MY_CARD_HOLDER_CARD_DUTY = "duty";
    public static final String _MY_CARD__HOLDER_CARD_PICTURE = "picture";
    public static final String _MY_CARD_HOLDER_CARD_PHONE = "phone";
    public static final String _MY_CARD_HOLDER_CARD_EMAIL = "email";
    public static final String _MY_CARD_HOLDER_CARD_ADDRESS = "address";
    public static final String _MY_CARD_HOLDER_CARD_AUTHORITY_KIND = "authority_kind";
    public static final String _MY_CARD_HOLDER_CARD_PHONE1 = "phone1";
    public static final String _MY_CARD_HOLDER_CARD_PHONE2 = "phone2";
    public static final String _MY_CARD_HOLDER_CARD_CELLPHONE1 = "cell_phone1";
    public static final String _MY_CARD_HOLDER_CARD_CELLPHONE2 = "cell_phone2";
    public static final String _MY_CARD_HOLDER_CARD_CODE = "code";
    public static final String _MY_CARD_HOLDER_CARD_SHARE_FLAG = "share_flag";
    public static final String _MY_CARD_HOLDER_CARD_NATION_CODE = "nation_code";
    public static final String _MY_CARD_HOLDER_CARD_PLATFORM = "platform";
    public static final String _MY_CARD_HOLDER_CARD_REG_DATE = "reg_date";
    public static final String _MY_CARD_HOLDER_CARD_MOD_DATE = "mod_date";

    //BINDER 세션 테이블 컬럼
    public static final String _BINDER_SESSION_TABLE_NAME = "binder_session";
    public static final String _BINDER_USER_SEQ = "user_seq";
    public static final String _BINDER_SESSION_SEQ = "session_seq";
    public static final String _BINDER_SESSION_TITLE = "title";
    public static final String _BINDER_SESSION_CONTENTS = "contents";
    public static final String _BINDER_SESSION_SUMMARY = "summary";
    public static final String _BINDER_SESSION_WRITER_SEQ = "writer_seq";
    public static final String _BINDER_SESSION_PRESENTER_SEQ = "presenter_seq";
    public static final String _BINDER_SESSION_START_TIME = "start_time";
    public static final String _BINDER_SESSION_END_TIME = "end_time";
    public static final String _BINDER_SESSION_REG_TIME = "reg_time";
    public static final String _BINDER_SESSION_MOD_TIME = "mod_time";
    public static final String _BINDER_SESSION_ATTACHED = "attached";
    public static final String _BINDER_SESSION_DATE = "date";


    //쿠폰 테이블 컬럼
    public static final String _COUPON_TABLE_NAME = "coupon_info";
    public static final String _COUPON_SEQ = "coupon_seq";
    public static final String _COUPON_NAME = "name";
    public static final String _COUPON_EXPLANATION = "explanation";
    public static final String _COUPON_SERIAL = "serial";
    public static final String _COUPON_IMG = "img";
    public static final String _COUPON_REG_DATE = "reg_date";

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
    public static final String _USER_REG_ID = "reg_id";

    //Message테이블 컬럼
    public static final String _MESSAGE_TABLE_NAME = "message_info";
    public static final String _MESSAGE_SEQ = "message_seq";
    public static final String _MESSAGE_SEND_USER_SEQ = "send_user_seq";
    public static final String _MESSAGE_RECEIVE_USER_SEQ = "receive_user_seq";
    public static final String _MESSAGE_TEXT = "message_text";
    public static final String _MESSAGE_SEND_TIME = "send_time";
    public static final String _MESSAGE_SENDER_NAME = "sender_name";
    public static final String _MESSAGE_RECEIVER_NAME = "receiver_name";

    //IndoorMap테이블 컬럼
    public static final String _INDOOR_MAP_TABLE_NAME = "indoor_map_info";
    public static final String _INDOOR_MAP_SEQ = "indoor_map_seq";
    public static final String _INDOOR_MAP_ORDER = "map_order";
    public static final String _INDOOR_MAP_TITLE = "title";
    public static final String _INDOOR_MAP_IMAGE = "image";

    //Schedule테이블 컬럼
    public static final String _SCHEDULE_TABLE_NAME = "schedule_info";
    public static final String _SCHEDULE_SEQ = "schedule_seq";
    public static final String _SCHEDULE_PARTNER_NAME = "partenr_name";
    public static final String _SCHEDULE_COMMENTS = "comments";
    public static final String _SCHEDULE_TIME = "time";


    //디비 생성용
    public static final class CreateDB implements BaseColumns {
        //메모관련 테이블
        public static final String _CREATE_MEMO_TABLE = "create table " +_MEMO_TABLE_NAME
                + " (" + _MEMO_MEMO_SEQ + " integer primary key autoincrement, "
                + _MEMO_CONTENTS + " text, "
                + _MEMO_REG_DATE + " datetime, "
                + _MEMO_MOD_DATE + " datetime "
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
                + _USER_DUTY + " text, "
                + _USER_REG_ID + " text"
                + " ); ";

        //coupon 테이블
        public static final String _CREATE_COUPON_TABLE = "create table " + _COUPON_TABLE_NAME
                + " (" + _COUPON_SEQ + " text, "
                + _COUPON_NAME + " text, "
                + _COUPON_EXPLANATION + " text, "
                + _COUPON_SERIAL + " text, "
                + _COUPON_IMG + " text, "
                + _COUPON_REG_DATE + " datetime"
                + " ); ";

        //binderSession 테이블
        public static final String _CREATE_BINDER_SESSION_TABLE = "create table " + _BINDER_SESSION_TABLE_NAME
                + " (" + _BINDER_SESSION_SEQ + " text, "
                + _BINDER_USER_SEQ + " text, "
                + _BINDER_SESSION_TITLE + " text, "
                + _BINDER_SESSION_CONTENTS + " text, "
                + _BINDER_SESSION_SUMMARY + " text, "
                + _BINDER_SESSION_WRITER_SEQ + " text, "
                + _BINDER_SESSION_PRESENTER_SEQ + " text, "
                + _BINDER_SESSION_START_TIME + " datetime, "
                + _BINDER_SESSION_END_TIME + " datetime, "
                + _BINDER_SESSION_ATTACHED + " text, "
                + _BINDER_SESSION_REG_TIME + " datetime, "
                + _BINDER_SESSION_MOD_TIME + " datetime, "
                + _BINDER_SESSION_DATE + " text"
                + " ); ";

        //agendaSession 테이블
        public static final String _CREATE_AGENDA_SESSION_TABLE = "create table " + _AGENDA_SESSION_TABLE_NAME
                + " (" + _AGENDA_SESSION_SEQ + " text, "
                + _AGENDA_SESSION_TITLE + " text, "
                + _AGENDA_SESSION_CONTENTS + " text, "
                + _AGENDA_SESSION_SUMMARY + " text, "
                + _AGENDA_SESSION_WRITER_SEQ + " text, "
                + _AGENDA_SESSION_PRESENTER_SEQ + " text, "
                + _AGENDA_SESSION_START_TIME + " datetime, "
                + _AGENDA_SESSION_END_TIME + " datetime, "
                + _AGENDA_SESSION_ATTACHED + " text, "
                + _AGENDA_SESSION_REG_TIME + " datetime, "
                + _AGENDA_SESSION_MOD_TIME + " datetime, "
                + _AGENDA_SESSION_DATE + " text"
                + " ); ";

        //ConferenceInfo 테이블
        public static final String _CREATE_CONFERENCE_INFO_TABLE = "create table " + _CONFERENCE_INFO_TABLE_NAME
                + " (" + _CONFERENCE_START_DATE + " datetime, "
                + _CONFERENCE_END_DATE + " datetime, "
                + _CONFERENCE_NAME + " text, "
                + _CONFERENCE_SUMMARY + " text"
                + " ); ";

        //Sponsor 테이블
        public static final String _CREATE_SPONSOR_TABLE = "create table " + _SPONSOR_TABLE_NAME
                + " (" + _SPONSOR_SEQ + " text, "
                + _SPONSOR_NAME + " text, "
                + _SPONSOR_EXPLANATION + " text, "
                + _SPONSOR_LOGO + " text, "
                + _SPONSOR_DETAIL_IMG + " text, "
                + _SPONSOR_REG_DATE + " datetime, "
                + _SPONSOR_MOD_DATE + " datetime, "
                + _SPONSOR_ATTACHED + " text"
                + " ); ";

        //Message 테이블
        public static final String _CREATE_MESSAGE_TABLE = "create table " + _MESSAGE_TABLE_NAME
                + " (" + _MESSAGE_SEQ + " text, "
                + _MESSAGE_SEND_USER_SEQ + " text, "
                + _MESSAGE_RECEIVE_USER_SEQ + " text, "
                + _MESSAGE_TEXT + " text, "
                + _MESSAGE_SEND_TIME + " datetime, "
                + _MESSAGE_SENDER_NAME + " text, "
                + _MESSAGE_RECEIVER_NAME + " text"
                + "); ";

        //CardHolder 테이블
        public static final String _CREATE_CARD_HOLDER_TABLE = "create table " + _MY_CARD_HOLDER_TABLE_NAME
                + " (" + _MY_CARD_HOLDER_USER_SEQ + " text, "
                + _MY_CARD_HOLDER_CARD_SEQ + " text, "
                + _MY_CARD_HOLDER_CARD_ID_FLAG + " text, "
                + _MY_CARD_HOLDER_CARD_ID + " text, "
                + _MY_CARD_HOLDER_CARD_NAME + " text, "
                + _MY_CARD_HOLDER_CARD_COMPANY + " text, "
                + _MY_CARD_HOLDER_CARD_DUTY + " text, "
                + _MY_CARD__HOLDER_CARD_PICTURE + " text, "
                + _MY_CARD_HOLDER_CARD_PHONE + " text, "
                + _MY_CARD_HOLDER_CARD_EMAIL + " text, "
                + _MY_CARD_HOLDER_CARD_ADDRESS + " text, "
                + _MY_CARD_HOLDER_CARD_AUTHORITY_KIND + " text, "
                + _MY_CARD_HOLDER_CARD_PHONE1 + " text, "
                + _MY_CARD_HOLDER_CARD_PHONE2 + " text, "
                + _MY_CARD_HOLDER_CARD_CELLPHONE1 + " text, "
                + _MY_CARD_HOLDER_CARD_CELLPHONE2 + " text, "
                + _MY_CARD_HOLDER_CARD_CODE + " text, "
                + _MY_CARD_HOLDER_CARD_SHARE_FLAG + " text, "
                + _MY_CARD_HOLDER_CARD_NATION_CODE + " text, "
                + _MY_CARD_HOLDER_CARD_PLATFORM + " text, "
                + _MY_CARD_HOLDER_CARD_REG_DATE + " datetime, "
                + _MY_CARD_HOLDER_CARD_MOD_DATE + " datetime"
                + ");";

        //IndoorMap 테이블
        public static final String _CREATE_INDOOR_MAP_TABLE = "create table " + _INDOOR_MAP_TABLE_NAME
                + " (" + _INDOOR_MAP_SEQ + " text, "
                + _INDOOR_MAP_ORDER + " text, "
                + _INDOOR_MAP_TITLE + " text, "
                + _INDOOR_MAP_IMAGE + " text"
                + ");";

        //Schedule 테이블
        public static final String _CREATE_SCHEDULE_TABLE = "create table " + _SCHEDULE_TABLE_NAME
                + " (" + _SCHEDULE_SEQ + " INTEGER PRIMARY KEY, "
                + _SCHEDULE_PARTNER_NAME + " text, "
                + _SCHEDULE_COMMENTS + " text, "
                + _SCHEDULE_TIME + " text"
                + ");";

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
