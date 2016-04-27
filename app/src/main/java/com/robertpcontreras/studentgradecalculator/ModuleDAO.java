package com.robertpcontreras.studentgradecalculator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by RobContreras on 24/04/16.
 */
public class ModuleDAO implements ModuleDAOInterface {

    private StudentCalculatorSQLiteHelper dbHelper;
    private SQLiteDatabase db;
    private Course course;

    public ModuleDAO(StudentCalculatorSQLiteHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * This function fetches a module from the Database based on the moduleID parameter.
     *
     * @param moduleID The ID of the module we want to retrieve
     * @return The new Module object
     */
    @Override
    public Module fetchModuleByID(long moduleID) {
        db = dbHelper.getWritableDatabase();
        // Set the columns we want to retrieve from the database.
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_GRADE};
        // Set the where clause for the query, specifying id of the module we require
        String whereClause = COLUMN_ID + " = ?";
        // Set the value of the '?' placeholder in the where clause.
        String[] whereArgs = {String.valueOf(moduleID)};

        // Use query() function to generate the query, results returned within a Cursor object.
        Cursor cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null);

        // Check the query retrieved a module from the ID. If not return null.
        if (! cursor.moveToFirst()) {
            return null;
        }

        // Create a new module object from the data returned from the database.
        Module module = new Module(course, cursor.getString(1), cursor.getInt(2));

        // Assign the modules _id
        module._id = cursor.getLong(0);

        // Close the cursor. We are done we it so we can free up the resources.
        cursor.close();
        db.close();

        // Return the module object retrieved form the database.
        return module;
    }

    /**
     * This function fetches all the modules from the database
     *
     * @return The modules that are found
     */
    @Override
    public ArrayList<Module> fetchAllModules() {

        db = dbHelper.getWritableDatabase();

        // Set the columns we want to retrieve from the database.
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_GRADE};

        // Set the where clause for the query, specifying id of the module we require
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        // Instantiate a new ArrayList object, all the modules returned from
        // the database will be stored within it.
        ArrayList<Module> modules = new ArrayList<>();

        // This loops through all the results returned from the query until the last result is reached
        while (cursor.moveToNext()) {
            // A new module is created from the data and is added to the modules ArrayList object
            Module module = new Module(course, cursor.getString(1), cursor.getInt(2));

            // Assign the modules _id
            module._id = cursor.getLong(0);

            // Add the module to the array.
            modules.add(module);
        }

        // Close the cursor. We are done we it so we can free up the resources.
        cursor.close();
        db.close();

        // Return the list of new modules.
        return modules;
    }

    /**
     * This function adds a new module to the database. A ContentValues
     * object is used to store the key/value pairs of the data
     * before it is inserted to the database
     *
     * @param module The Module to be added to the database
     * @return The new _id of the module added.
     */
    @Override
    public long insertModule(Module module) {
        db = dbHelper.getWritableDatabase();

        // A new ContentValues object is retrieved from our setContentValues
        // method of the data that will be inserted into the database.
        ContentValues moduleValues = setContentValues(module);

        // Adds the row to the table and returns the _id of the added module
        long _id = db.insert(TABLE_NAME, null, moduleValues);

        db.close();

        return _id;
    }

    /**
     * This function updates a module within the database by creating a
     * ContentValues object of the new values. The update() method
     * from then updates the module in the database based upon
     * the value of the moduleID parameter.
     *
     * @param moduleID The _id of the module we are updating
     * @param module The updated Module object
     */
    @Override
    public void updateModule(long moduleID, Module module) {
        db = dbHelper.getWritableDatabase();

        // A new ContentValues object is retrieved from our setContentValues
        // method containing the updated values of the module.
        ContentValues moduleValues = setContentValues(module);

        // Update the data in the table. Here we specify the table name,
        // the moduleValues ContentValues object, the where clause and the
        // where argument to replace '?' in the where clause. Our moduleID int
        // needs to be converted to a string.
        db.update(TABLE_NAME, moduleValues, COLUMN_ID + " = ?", new String[]{String.valueOf(moduleID)});

        db.close();
    }

    /**
     * This method deletes a module from the database based upon the value of
     * the moduleID parameter.
     *
     * @param moduleID The _id of the module that should be deleted.
     */
    @Override
    public void deleteModule(long moduleID) {
        db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(moduleID)});
        db.close();
    }

    /**
     * This method reduces redundant code within the class as this functionality
     * is required in multiple times within the class. It has been set to
     * private as it is only required by methods within the class. It
     * returns a ContentValues object based upon the values
     * of the Module object passed in.
     *
     * @param module The Module object that we need a ContentValue object for
     * @return New ContentValue object
     */
    private ContentValues setContentValues(Module module) {
        // A new ContentValues object is instantiated to store the key/value pairs
        // of the the modules data.
        ContentValues moduleValues = new ContentValues();

        // Sets the values in the moduleValues object
        moduleValues.put(COLUMN_TITLE, module.title);
        moduleValues.put(COLUMN_GRADE, module.grade);

        // Returns the new ContentValues object
        return moduleValues;
    }

    /**
     * Set the value of the course object as we need it throughout this object
     *
     * @param course The Course Object the modules here will belong to.
     */
    public void setCourse(Course course) {
        this.course = course;
    }
}
