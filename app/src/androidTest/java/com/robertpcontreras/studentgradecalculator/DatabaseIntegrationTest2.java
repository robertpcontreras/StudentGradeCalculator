package com.robertpcontreras.studentgradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.clearText;
import static org.junit.Assert.*;

import static org.hamcrest.Matchers.not;

import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.matcher.RootMatchers.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

/**
 * Created by RobContreras on 19/01/16.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseIntegrationTest2 {

    Context mMockContext;
    StudentCalculatorSQLiteHelper dbHelper;
    ModuleDAO modulesDB;

    @Rule
    public ActivityTestRule<CourseActivity> mActivityRule = new ActivityTestRule<>(CourseActivity.class);

    @BeforeClass
    public static void clearDatabase () {
        Context mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "");
        StudentCalculatorSQLiteHelper dbHelper = new StudentCalculatorSQLiteHelper(mMockContext);
        dbHelper.clearDB();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        addModuleToDB(db, "TEST MODULE 1", 80);
        addModuleToDB(db, "TEST MODULE 2", 70);
        addModuleToDB(db, "TEST MODULE 3", 70);
        addModuleToDB(db, "TEST MODULE 4", 60);
    }

    @Before
    public void setUpDBStuff () {
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "");
        dbHelper = new StudentCalculatorSQLiteHelper(mMockContext);
        modulesDB = new ModuleDAO(dbHelper);
    }

    public static void addModuleToDB(SQLiteDatabase db, String title, int grade) {
        ContentValues moduleValues = new ContentValues();
        // Sets the values in the moduleValues object
        moduleValues.put(ModuleDAO.COLUMN_TITLE, title);
        moduleValues.put(ModuleDAO.COLUMN_GRADE, grade);

        db.insert(ModuleDAO.TABLE_NAME, null, moduleValues);
    }

    @Test
    public void module_stored_in_the_database_are_shown_when_the_application_is_opened () {
        onView(atPositionInTable(0, 1)).check(matches(withText("TEST MODULE 1")));
        onView(atPositionInTable(1, 1)).check(matches(withText("80%")));
        onView(atPositionInTable(0, 2)).check(matches(withText("TEST MODULE 2")));
        onView(atPositionInTable(1, 2)).check(matches(withText("70%")));
        onView(atPositionInTable(0, 3)).check(matches(withText("TEST MODULE 3")));
        onView(atPositionInTable(1, 3)).check(matches(withText("70%")));
        onView(atPositionInTable(0, 4)).check(matches(withText("TEST MODULE 4")));
        onView(atPositionInTable(1, 4)).check(matches(withText("60%")));

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("70%")));
    }


    static Matcher<View> atPositionInTable(final int x, final int y) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("is at position # " + x + " , " + y);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                if (!(viewParent instanceof TableRow)) {
                    return false;
                }
                TableRow row = (TableRow) viewParent;
                TableLayout table = (TableLayout) row.getParent();
                if (table.indexOfChild(row) != y)
                    return false;
                if (row.indexOfChild(view) == x)
                    return true;
                else
                    return false;
            }
        };
    }

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

}