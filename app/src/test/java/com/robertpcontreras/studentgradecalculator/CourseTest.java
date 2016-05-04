package com.robertpcontreras.studentgradecalculator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by RobContreras on 17/01/16.
 */
public class CourseTest {

    private Course course;

    @Before
    public void setUp() throws Exception {
        course = new Course();
    }

    @Test
    public void a_module_can_be_added_to_a_course() throws Exception {
        String newModuleString = "Test Module";
        String newModuleGrade = "78";

        course.addModule(newModuleString, newModuleGrade);

        assertTrue(course.modules.size() == 1);
    }

    @Test
    public void multiple_modules_can_be_added_to_a_course() throws Exception {
        String moduleTitle1 = "Module 1";
        String moduleGrade1 = "100";
        String moduleTitle2 = "Module 2";
        String moduleGrade2 = "100";
        String moduleTitle3 = "Module 3";
        String moduleGrade3 = "100";

        course.addModule(moduleTitle1, moduleGrade1);
        course.addModule(moduleTitle2, moduleGrade2);
        course.addModule(moduleTitle3, moduleGrade3);

        assertTrue(course.modules.size() == 3);
    }

    @Test
    public void can_calculate_average_percentage() throws Exception {
        String moduleTitle1 = "Module 1";
        String moduleGrade1 = "100";
        String moduleTitle2 = "Module 2";
        String moduleGrade2 = "80";
        String moduleTitle3 = "Module 3";
        String moduleGrade3 = "90";

        course.addModule(moduleTitle1, moduleGrade1);
        course.addModule(moduleTitle2, moduleGrade2);
        course.addModule(moduleTitle3, moduleGrade3);

        int averagePercentage = course.calculateAveragePercentage();

        assertTrue(averagePercentage == 90);
    }

    @Test
    public void can_calculate_overall_classification() throws Exception {
        String moduleTitle1 = "Module 1";
        String moduleGrade1 = "100";
        String moduleTitle2 = "Module 2";
        String moduleGrade2 = "80";
        String moduleTitle3 = "Module 3";
        String moduleGrade3 = "90";

        course.addModule(moduleTitle1, moduleGrade1);
        course.addModule(moduleTitle2, moduleGrade2);
        course.addModule(moduleTitle3, moduleGrade3);

        String overallClassification = course.calculateOverallClassification();

        assertTrue(overallClassification.equals(DegreeClassifications.FIRST.description));
    }
}