package com.robertpcontreras.studentgradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class is responsible for creating, upgrading and downgrading the SQLite Database
 *
 * Created by RobContreras on 24/04/16.
 */
public class StudentCalculatorSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "student_grades_calculator.db";

    public StudentCalculatorSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The onCreate Method is called when the database is first created.
     * Therefore, we need to create the tables and any data we require
     * within the database within this method.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ModuleDAO.CREATE_TABLE);
    }

    /**
     * This function is required for when we need to upgrade the database. SQLite Helper
     * class checks if the database version number passed in the constructor is
     * higher than the previously used database version number. If it is,
     * the SQL lite helper will run the the code within this function
     * to upgrade the database.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ModuleDAO.TABLE_NAME);
        onCreate(db);
    }

    public void clearDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ModuleDAO.TABLE_NAME);
        onCreate(db);
    }
}
