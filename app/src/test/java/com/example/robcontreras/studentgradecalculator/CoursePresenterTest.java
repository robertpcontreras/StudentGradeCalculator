package com.example.robcontreras.studentgradecalculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by RobContreras on 17/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoursePresenterTest {

    @Mock
    private CourseView view;

    private CoursePresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new CoursePresenter(view);
    }

    @Test
    public void adding_a_module_works() throws Exception {
        String moduleTitle = "Mobile Application Developement";
        String moduleGrade = "78";

        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).addModuleResultToTable(anyString(), anyInt());
    }

    @Test
    public void adding_an_empty_module_title_displays_an_error_to_the_user() throws Exception {
        String moduleTitle = "";
        String moduleGrade = "78";
        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).setModuleTitleError();
    }

    @Test
    public void adding_a_duplicate_module_title_displays_an_error_to_the_user() throws Exception {
        String moduleTitle = "AddMeTwice";
        String moduleGrade = "78";

        Module duplicateModule = new Module(presenter.course, moduleTitle, Integer.parseInt(moduleGrade));
        presenter.course.modules.add(duplicateModule);

        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).setDuplicateModuleError();
    }

    @Test
    public void adding_an_empty_module_grade_displays_an_error_to_the_user() throws Exception {
        String moduleTitle = "A Module";
        String moduleGrade = "";

        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).setModuleGradeError();
    }

    @Test
    public void adding_a_string_module_grade_displays_an_error_to_the_user() throws Exception {
        String moduleTitle = "A Module";
        String moduleGrade = "Not a percentage";

        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).setModuleGradeError();
    }

    @Test
    public void adding_a_module_grade_less_than_0_displays_an_error_to_the_user() throws Exception {
        String moduleTitle = "A Module";
        String moduleGrade = "-10";

        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).setModuleGradeError();
    }

    @Test
    public void adding_a_module_grade_more_than_100_displays_an_error_to_the_user() throws Exception {
        String moduleTitle = "A Module";
        String moduleGrade = "105";

        presenter.addNewModuleToCourse(moduleTitle, moduleGrade);

        verify(view).setModuleGradeError();
    }
}