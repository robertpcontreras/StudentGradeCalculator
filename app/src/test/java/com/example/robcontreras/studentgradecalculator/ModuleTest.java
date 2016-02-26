package com.example.robcontreras.studentgradecalculator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by RobContreras on 17/01/16.
 */
public class ModuleTest {

    @Mock
    ModuleValidationErrorListener errorListener;

    @Before
    public void setUp() throws Exception {
        this.errorListener = new ModuleValidationErrorListener() {
            @Override
            public void onModuleTitleError() {}

            @Override
            public void onDuplicateModuleError() {}

            @Override
            public void onModuleGradeError() {}
        };
    }

    @Test
    public void a_module_can_be_successfully_validated() throws Exception {
        Course course = new Course();
        String moduleTitle = "Module Test";
        String moduleGrade = "100";

        assertTrue(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }

    @Test
    public void a_module_requires_a_title() throws Exception {
        Course course = new Course();
        String moduleTitle = "";
        String moduleGrade = "100";

        assertFalse(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }

    @Test
    public void a_course_cannot_have_duplicate_modules() throws Exception {
        Course course = new Course();
        String moduleTitle = "Duplicate Module";
        String moduleGrade = "100";

        course.addModule("Not a duplicate", moduleGrade);
        course.addModule(moduleTitle, moduleGrade);

        assertFalse(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }

    @Test
    public void a_module_grade_must_be_numeric() throws Exception {
        Course course = new Course();
        String moduleTitle = "Test Module";
        String moduleGrade = "Not a percentage";

        assertFalse(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }

    @Test
    public void a_module_grade_must_be_entered() throws Exception {
        Course course = new Course();
        String moduleTitle = "Test Module";
        String moduleGrade = "";

        assertFalse(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }

    @Test
    public void a_module_grade_must_be_lower_than_or_equal_to_100() throws Exception {
        Course course = new Course();
        String moduleTitle = "Test Module";
        String moduleGrade = "103";

        assertFalse(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }

    @Test
    public void a_module_grade_must_be_greater_than_or_equal_to_0() throws Exception {
        Course course = new Course();
        String moduleTitle = "Test Module";
        String moduleGrade = "-12";

        assertFalse(Module.validateModule(course, moduleTitle, moduleGrade, errorListener));
    }
}