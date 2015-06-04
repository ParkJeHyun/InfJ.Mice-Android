package com.infjay.mice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015-06-02.
 */
public class SqliteManager extends SQLiteOpenHelper {

    public SqliteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //생성자에서 넘겨받은 이름과 버전의 데이터베이스가 존재하지않을때
        //새로운 데이터베이스 만들때 사용
        db.execSQL("CREATE TABLE LoginInfo( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //데이터베이스는 존재하지만 버전이 다를때
        //데이터베이스를 변경하고 싶을 때 버전 올려주고 새로운 작업
    }
}