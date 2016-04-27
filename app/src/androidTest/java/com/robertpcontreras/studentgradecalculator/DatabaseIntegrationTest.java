package com.robertpcontreras.studentgradecalculator;

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
public class DatabaseIntegrationTest {

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
    }

    @Before
    public void setUpDBStuff () {
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "");
        dbHelper = new StudentCalculatorSQLiteHelper(mMockContext);
        modulesDB = new ModuleDAO(dbHelper);
        dbHelper.clearDB();
    }

    @After
    public void clearDB() {
        dbHelper.clearDB();
    }

    public void the_user_can_type_text_into_the_module_title() {
        String inputText = "TEST MODULE";

        onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());

        onView(withId(R.id.moduleTitle)).check(matches(withText(inputText)));
    }

    public void the_user_can_type_text_into_the_module_grade() {
        String inputGrade = "80";

        onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());

        onView(withId(R.id.moduleGrade)).check(matches(withText(inputGrade)));
    }

    @Test
    public void when_the_user_adds_a_new_module_it_is_added_to_the_database() {

        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();

        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("80%")));

        onView(atPositionInTable(0, 1)).check(matches(withText("TEST MODULE")));
        onView(atPositionInTable(1, 1)).check(matches(withText("80%")));

        ArrayList<Module> modules = modulesDB.fetchAllModules();
        Module module = modules.get(0);

        assertTrue(modules.size() == 1);
        assertTrue(module.title.equals("TEST MODULE"));
    }

    @Test
    public void modules_can_be_added_and_all_can_be_removed () {
        for (int i = 1; i < 6; i++) {
            String inputText = "TEST MODULE " + i;
            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            String inputGrade = "80";
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("80%")));

        for (int i = 1; i < 6; i++) {
            // delete the first module in the table
            onView(atPositionInTable(2,1)).perform(click());
            onView(withId(R.id.deleteModuleButton)).perform(click());
        }

        onView(atPositionInTable(0, 1)).check(matches(withText("Please add some Module Results")));

        ArrayList<Module> modules = modulesDB.fetchAllModules();

        assertTrue(modules.size() == 0);
    }

    @Test
    public void when_you_can_add_a_module_then_update_it_shows_in_table_and_db_and_classifications() {

        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).perform(typeText("Test Module 1"), closeSoftKeyboard());
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(atPositionInTable(2,1)).perform(click());

        onView(withId(R.id.moduleTitle)).perform(clearText());
        onView(withId(R.id.moduleTitle)).perform(typeText("Test Module Edit"));
        onView(withId(R.id.moduleGrade)).perform(clearText());
        onView(withId(R.id.moduleGrade)).perform(typeText("90"));
        onView(withId(R.id.saveModuleButton)).perform(click());

        onView(atPositionInTable(0, 1)).check(matches(withText("Test Module Edit")));
        onView(atPositionInTable(1, 1)).check(matches(withText("90%")));

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("85%")));

        ArrayList<Module> modules = modulesDB.fetchAllModules();
        Module module = modules.get(0);

        assertTrue(module.title.equals("Test Module Edit"));
        assertTrue(module.grade == 90);
    }

    @Test
    public void when_you_delete_a_module_it_shows_in_table_and_db_and_classifications() {

        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).perform(typeText("Test Module 1"), closeSoftKeyboard());
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(atPositionInTable(2,1)).perform(click());

        onView(withId(R.id.deleteModuleButton)).perform(click());

        onView(atPositionInTable(0, 1)).check(matches(withText("Test Module 1")));
        onView(atPositionInTable(1, 1)).check(matches(withText("80%")));

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("80%")));

        ArrayList<Module> modules = modulesDB.fetchAllModules();

        assertTrue(modules.size() == 1);
    }

    @Test
    public void you_can_add_a_module_then_update() {
        String inputText = "Dummy Test MODULE";
        onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
        the_user_can_type_text_into_the_module_grade();

        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(atPositionInTable(2,1)).perform(click());

        onView(withId(R.id.moduleTitle)).check(matches(withText("Dummy Test MODULE")));
    }

    @Test
    public void when_updating_a_module_title_must_be_entered () {
        you_can_add_a_module_then_update();

        onView(withId(R.id.moduleTitle)).perform(clearText());

        onView(withId(R.id.saveModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_title_error))));
    }

    @Test
    public void when_updating_a_duplicate_module_cannot_be_entered () {

        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).perform(typeText("TEST MODULE 2"), closeSoftKeyboard());
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(atPositionInTable(2,1)).perform(click());

        onView(withId(R.id.moduleTitle)).perform(clearText());
        onView(withId(R.id.moduleTitle)).perform(typeText("TEST MODULE 2"));

        onView(withId(R.id.saveModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.duplicate_module_error))));
    }

    @Test
    public void when_updating_a_grade_must_be_entered (){
        you_can_add_a_module_then_update();

        onView(withId(R.id.moduleGrade)).perform(clearText());

        onView(withId(R.id.saveModuleButton)).perform(click());

        onView(withId(R.id.moduleGrade)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_grade_error))));
    }

    @Test
    public void when_updating_a_grade_must_be_between_0_and_100 () {
        you_can_add_a_module_then_update();

        onView(withId(R.id.moduleGrade)).perform(clearText());
        onView(withId(R.id.moduleGrade)).perform(typeText("101"), closeSoftKeyboard());

        onView(withId(R.id.saveModuleButton)).perform(click());

        onView(withId(R.id.moduleGrade)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_grade_error))));

        onView(withId(R.id.moduleGrade)).perform(clearText());
        onView(withId(R.id.moduleGrade)).perform(typeText("-10"), closeSoftKeyboard());

        onView(withId(R.id.saveModuleButton)).perform(click());

        onView(withId(R.id.moduleGrade)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_grade_error))));
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