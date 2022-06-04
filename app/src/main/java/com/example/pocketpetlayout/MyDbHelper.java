package com.example.pocketpetlayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class MyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "Pocketpet.db";

    //회원가입 - 주인
    private static final String SQL_CREATE_MEMBER =
            "CREATE TABLE " + Member.TABLE_NAME + " (" +
                    Member.MEMBER_ID + " TEXT PRIMARY KEY," +
                    Member.PASSWORD + " TEXT," +
                    Member.NICKNAME + " TEXT," +
                    Member.SEX + " TEXT," +
                    Member.BIRTHDAY + " INTEGER)";

    //회원가입 - 펫
    private static final String SQL_CREATE_PET =
            "CREATE TABLE " + Pet.TABLE_NAME + " (" +
                    Pet.PET_NAME + " TEXT PRIMARY KEY," +
                    Pet.BIRTHDAY + " INTEGER," +
                    Pet.SEX + " TEXT)";

    //board
    private static final String SQL_CREATE_BOARD =
            "CREATE TABLE " + Board.TABLE_NAME + " (" +
                    Board.COLUMN_TITLE + " TEXT PRIMARY KEY," +
                    Board.COLUMN_WRITER + " TEXT," +
                    Board.COLUMN_IMAGE + " TEXT," +
                    Board.COLUMN_MAINTEXT + " TEXT," +
                    Board.COLUMN_COMMENT_CNT + " INTEGER," +
                    Board.COLUMN_LIKE_CNT + " INTEGER," +
                    Board.COLUMN_CATEGORY + " TEXT," +
                    Board.COLUMN_REGISTER_DATE + " INTEGER)";

    //Feed
    private static final String SQL_CREATE_FEED =
            "CREATE TABLE " + Feed.TABLE_NAME + " (" +
                    Feed.FEED_ID + " INTEGER PRIMARY KEY," +
                    Feed.FEED_TITLE + " TEXT," +
                    Feed.WRITER + " TEXT," +
                    Feed.IMAGE + " TEXT)";

    private static final  String SQL_DELETE_MEMBER =
            "DROP TABLE IF EXISTS " + Member.TABLE_NAME;

    private static final String SQL_DELETE_PET =
            "DROP TABLE IF EXISTS " + Pet.TABLE_NAME;

    private static final String SQL_DELETE_BOARD =
            "DROP TABLE IF EXISTS " + Board.TABLE_NAME;

    private static final String SQL_DELETE_FEED =
            "DROP TABLE IF EXISTS " + Feed.TABLE_NAME;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MEMBER);
        sqLiteDatabase.execSQL(SQL_CREATE_PET);
        sqLiteDatabase.execSQL(SQL_CREATE_BOARD);
        sqLiteDatabase.execSQL(SQL_CREATE_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_MEMBER);
        sqLiteDatabase.execSQL(SQL_DELETE_PET);
        sqLiteDatabase.execSQL(SQL_DELETE_BOARD);
        sqLiteDatabase.execSQL(SQL_DELETE_FEED);
        onCreate(sqLiteDatabase);
    }
}