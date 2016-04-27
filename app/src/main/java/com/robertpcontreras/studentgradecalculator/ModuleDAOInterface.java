package com.robertpcontreras.studentgradecalculator;

import java.util.ArrayList;

/**
 * Created by RobContreras on 24/04/16.
 */
public interface ModuleDAOInterface {

    String TABLE_NAME = "modules";
    String COLUMN_ID = "_id";
    String COLUMN_TITLE = "ModuleTitle";
    String COLUMN_GRADE = "ModuleGrade";
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_GRADE + " INTEGER NOT NULL " +
            ");";

    Module fetchModuleByID(long moduleID);
    ArrayList<Module> fetchAllModules();
    long insertModule(Module module);
    void updateModule(long moduleID, Module module);
    void deleteModule(long moduleID);
    void setCourse(Course course);
}
