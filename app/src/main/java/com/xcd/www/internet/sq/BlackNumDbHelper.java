package com.xcd.www.internet.sq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gs on 2018/11/3.
 */

public class BlackNumDbHelper extends SQLiteOpenHelper {


    public BlackNumDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    /**
     * 第一次运行时，创建数据库，调用此方法
     */
    public void onCreate(SQLiteDatabase db) {
        //建表：自增长的主键
        db.execSQL("create table black_num(_id integer primary key autoincrement," +
                "userId varchar(20) ," +
                "name varchar(20) ," +
                "nick varchar(20) ," +
                "headUrl," +
                "number varchar(20))");

    }

    @Override
    /**
     * 当数据库版本不一样，升级数据库时，调用此方法
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}