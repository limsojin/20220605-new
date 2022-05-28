package com.example.pocketpetlayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATEBASE_NAME = "PoketPet.db";

    public DBHelper(Context context) {
        super(context, DATEBASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Board.SQL_CREATE_BOARD);
        sqLiteDatabase.execSQL(Comment.SQL_CREATE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Board.SQL_DELETE_BOARD);
        sqLiteDatabase.execSQL(Comment.SQL_DELETE_COMMENT);
        onCreate(sqLiteDatabase);
    }


    public boolean updateBoardHeart(int Id, int heart_cnt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Board.COLUMN_LIKE_CNT, heart_cnt + 1);
        db.update(Board.TABLE_NAME, contentValues, Board.COLUMN_BOARD_ID + " = " + Id, null);
        return true;
    }

    public boolean updateBoardComment(int Id, int comment_cnt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Board.COLUMN_COMMENT_CNT, comment_cnt + 1);
        db.update(Board.TABLE_NAME, contentValues, Board.COLUMN_BOARD_ID + " = " + Id, null);
        return true;
    }

    public boolean updateBoardContents(int Id, String title, String imgName, String category, String mainTxt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Board.COLUMN_TITLE, title);
        contentValues.put(Board.COLUMN_IMAGE, imgName);
        contentValues.put(Board.COLUMN_MAINTEXT, mainTxt);
        db.update(Board.TABLE_NAME, contentValues, Board.COLUMN_BOARD_ID + "=" + Id, null);
        return true;
    }

    public boolean deleteBoardToPK(int Id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE FROM " + Board.TABLE_NAME + " WHERE " + Board.COLUMN_BOARD_ID + " = "  + Id + " ;" );
        db.execSQL(" DELETE FROM " + Comment.TABLE_NAME + " WHERE " + Comment.COLUMN_BOARD_ID + " = "  + Id + " ;" );
        return true;
    }

}
