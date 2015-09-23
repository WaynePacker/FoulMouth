package com.imy320.foultmouth.personaldigitaldairy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
    private final static String DB_NAME = "DB_Diary";
    private final static int DB_VERSION = 1;

    //DB Query for onCreate Method.
    private final static String CREATE_QUERY =
            "CREATE TABLE "+DBAbstract.DB_Diary.TABLE_NAME+"("+ DBAbstract.DB_Diary.ENTRY_TITLE+" TEXT,"
            + DBAbstract.DB_Diary.ENTRY_DATE+" TEXT,"+ DBAbstract.DB_Diary.ENTRY_TIME+" TEXT,"+
                    DBAbstract.DB_Diary.ENTRY_DETAILS+ " TEXT);";

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        Log.e("DB_opp: ","DB CREATED/OPENED.");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_QUERY);
        Log.e("DB_Opp: ","Table Created");
    }

    public void insertEntry(String _title, String _date, String _time, String _details, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAbstract.DB_Diary.ENTRY_TITLE, _title);
        contentValues.put(DBAbstract.DB_Diary.ENTRY_DATE, _date);
        contentValues.put(DBAbstract.DB_Diary.ENTRY_TIME, _time);
        contentValues.put(DBAbstract.DB_Diary.ENTRY_DETAILS, _details);

        db.insert(DBAbstract.DB_Diary.TABLE_NAME, DBAbstract.DB_Diary.ENTRY_DETAILS, contentValues);
        Log.e("DB_Opp: ","Data Inserted");
    }

    public Cursor getInformation(SQLiteDatabase db)
    {
        Cursor cursor;
        String[] projections = {DBAbstract.DB_Diary.ENTRY_TITLE, DBAbstract.DB_Diary.ENTRY_DATE,
                DBAbstract.DB_Diary.ENTRY_TIME, DBAbstract.DB_Diary.ENTRY_DETAILS};
        cursor = db.query(DBAbstract.DB_Diary.TABLE_NAME, projections, null, null, null, null, null);
        return cursor;
    }

    public void deleteEntry(String _title, SQLiteDatabase sqLiteDatabase)
    {
        String selection = DBAbstract.DB_Diary.ENTRY_TITLE+" LIKE ?";
        String[] selectionArgs = {_title};

        sqLiteDatabase.delete(DBAbstract.DB_Diary.TABLE_NAME, selection, selectionArgs);
        Log.e("DB_Opp: ","Entry Deleted");
    }

    public void updateEntry(String oldTitle, String newTitle, String oldDate, String newDate, String oldTime,
                            String newTime, String oldDetails, String newDetails, SQLiteDatabase sqLiteDatabase)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAbstract.DB_Diary.ENTRY_TITLE, newTitle);
        contentValues.put(DBAbstract.DB_Diary.ENTRY_DATE, newDate);
        contentValues.put(DBAbstract.DB_Diary.ENTRY_TIME, newTime);
        contentValues.put(DBAbstract.DB_Diary.ENTRY_DETAILS, newDetails);
        String selection = DBAbstract.DB_Diary.ENTRY_TITLE + " LIKE ?";
        String[] selectionArg = {oldTitle};

        sqLiteDatabase.update(DBAbstract.DB_Diary.TABLE_NAME, contentValues, selection, selectionArg);
        Log.e("DB_Opp: ", "Entry Updated");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
