package com.example.robcontreras.studentgradecalculator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class CourseActivityTest {

    @Rule
    public ActivityTestRule<CourseActivity> mActivityRule = new ActivityTestRule<>(CourseActivity.class);

    @Test
    public void the_user_can_type_text_into_the_module_title() {
        String inputText = "TESTING THE INPUT";

        onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());

        onView(withId(R.id.moduleTitle)).check(matches(withText(inputText)));
    }

    @Test
    public void the_user_can_type_text_into_the_module_grade() {
        String inputGrade = "80";

        onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());

        onView(withId(R.id.moduleGrade)).check(matches(withText(inputGrade)));
    }

    @Test
    public void the_user_can_add_a_module_to_table_and_a_toast_message_gets_displayed() {
        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();

        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("80%")));

        onView(atPositionInTable(0, 1)).check(matches(withText("TESTING THE INPUT")));
        onView(atPositionInTable(1, 1)).check(matches(withText("80%")));
        onView(withText(R.string.toast_new_module_confirmation)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void the_user_can_add_10_modules() {
        for (int i = 1; i < 11; i++) {
            String inputText = "TEST MODULE " + i;
            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            String inputGrade = "80";
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
        onView(withId(R.id.averagePercentageValue)).check(matches(withText("80%")));

        onView(atPositionInTable(0, 10)).check(matches(withText("TEST MODULE 10")));
        onView(atPositionInTable(1, 10)).check(matches(withText("80%")));
        onView(atPositionInTable(0, 1)).check(matches(withText("TEST MODULE 1")));
        onView(atPositionInTable(1, 1)).check(matches(withText("80%")));
    }

    @Test
    public void the_correct_classification_show_when_the_user_fails() {
        int min = 0;
        int max = 39;
        int randomNumber;
        Random random = new Random();


        for (int i = 1; i < 5; i++) {
            randomNumber = random.nextInt((max-min) + 1) + min;
            String inputText = "TEST MODULE " + i;
            String inputGrade = Integer.toString(randomNumber);

            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("Fail")));
    }

    @Test
    public void the_correct_classification_show_when_the_user_achieves_third() {
        int min = 40;
        int max = 49;
        int randomNumber;
        Random random = new Random();


        for (int i = 1; i < 5; i++) {
            randomNumber = random.nextInt((max-min) + 1) + min;
            String inputText = "TEST MODULE " + i;
            String inputGrade = Integer.toString(randomNumber);

            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("Third Class Honours")));
    }

    @Test
    public void the_correct_classification_show_when_the_user_achieves_2_2() {
        int min = 50;
        int max = 59;
        int randomNumber;
        Random random = new Random();


        for (int i = 1; i < 5; i++) {
            randomNumber = random.nextInt((max-min) + 1) + min;
            String inputText = "TEST MODULE " + i;
            String inputGrade = Integer.toString(randomNumber);

            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("Lower Second Class Honours")));
    }

    @Test
    public void the_correct_classification_show_when_the_user_achieves_2_1() {
        int min = 60;
        int max = 69;
        int randomNumber;
        Random random = new Random();


        for (int i = 1; i < 5; i++) {
            randomNumber = random.nextInt((max-min) + 1) + min;
            String inputText = "TEST MODULE " + i;
            String inputGrade = Integer.toString(randomNumber);

            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("Upper Second Class Honours")));
    }

    @Test
    public void the_correct_classification_show_when_the_user_achieves_first() {
        int min = 70;
        int max = 100;
        int randomNumber;
        Random random = new Random();


        for (int i = 1; i < 5; i++) {
            randomNumber = random.nextInt((max-min) + 1) + min;
            String inputText = "TEST MODULE " + i;
            String inputGrade = Integer.toString(randomNumber);

            onView(withId(R.id.moduleTitle)).perform(typeText(inputText), closeSoftKeyboard());
            onView(withId(R.id.moduleGrade)).perform(typeText(inputGrade), closeSoftKeyboard());
            onView(withId(R.id.addNewModuleButton)).perform(click());
        }

        onView(withId(R.id.overallClassificationValue)).check(matches(withText("First Class Honours")));
    }

    @Test
    public void an_error_is_shown_when_the_user_doesnt_enter_a_module_title() {
        the_user_can_type_text_into_the_module_grade();

        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_title_error))));
    }

    @Test
    public void an_error_is_shown_when_the_user_enters_a_duplicate_module_title() {
        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        the_user_can_type_text_into_the_module_title();
        the_user_can_type_text_into_the_module_grade();
        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleTitle)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.duplicate_module_error))));
    }

    @Test
    public void an_error_is_shown_when_the_user_doesnt_enter_a_module_grade() {
        the_user_can_type_text_into_the_module_title();

        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleGrade)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_grade_error))));
    }

    @Test
    public void an_error_is_shown_when_the_user_enters_a_module_grade_over_100() {
        the_user_can_type_text_into_the_module_title();

        onView(withId(R.id.moduleGrade)).perform(typeText("101"), closeSoftKeyboard());

        onView(withId(R.id.addNewModuleButton)).perform(click());

        onView(withId(R.id.moduleGrade)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.module_grade_error))));
    }

    @Test
    public void an_error_is_shown_when_the_user_enters_a_module_grade_under_0() {
        the_user_can_type_text_into_the_module_title();

        onView(withId(R.id.moduleGrade)).perform(typeText("-10"), closeSoftKeyboard());

        onView(withId(R.id.addNewModuleButton)).perform(click());

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