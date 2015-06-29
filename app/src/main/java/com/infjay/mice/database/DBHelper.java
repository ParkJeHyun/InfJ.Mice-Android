package com.infjay.mice.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HJHOME on 2015-06-26.
 */
public class DBHelper {

    private static final String DATABASE_NAME = "mice.db";

    private static final int DATABASE_VERSION = 3;


    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    public DBHelper(Context context){
        this.mCtx = context;
    }

    public DBHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            /*
            //DB TABLE CREATE SAMPLE
            db.execSQL(MynahDB.CreateDB._CREATE_USER_TABLE);
            */

            db.execSQL(MiceDB.CreateDB._CREATE_MEMO_TABLE);
            db.execSQL(MiceDB.CreateDB._CREATE_USER_INFO_TABLE);
            db.execSQL(MiceDB.CreateDB._CREATE_BINDER_SESSION_TABLE);
            db.execSQL(MiceDB.CreateDB._CREATE_CONFERENCE_INFO_TABLE);
            db.execSQL(MiceDB.CreateDB._CREATE_AGENDA_SESSION_TABLE);
            db.execSQL(MiceDB.CreateDB._CREATE_COUPON_TABLE);
            db.execSQL(MiceDB.CreateDB._CREATE_SPONSOR_TABLE);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            /*
            //DB VERSION UP 시 TABLE 갱신할 때 다시 지우기
            db.execSQL("DROP TABLE IF EXISTS " + MynahDB._USER_TABLE_NAME);

            onCreate(db);
            */

            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._MEMO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._USER_INFO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._BINDER_SESSION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._CONFERENCE_INFO_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._AGENDA_SESSION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._COUPON_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MiceDB._SPONSOR_TABLE_NAME);
            onCreate(db);
        }
    }
}
