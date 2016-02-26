package com.example.robcontreras.studentgradecalculator;

/**
 * Created by RobContreras on 15/01/16.
 */
public class CoursePresenter implements ModuleValidationErrorListener {

    /**
     * Defining class properties
     */
    private CourseView courseView;
    public Course course;

    /**
     * Constructor sets the presenters dependencies.
     *
     * @param courseView The activity that instantiated the object
     */
    public CoursePresenter(CourseView courseView) {

        /**
         * Assigning the view to the presenter. This will
         * allow us to modify the view when required.
         */
        this.courseView = courseView;

        /**
         * Instantiate a new course. This can now be used to add modules and calculate
         * the overall grades. Going forward we can check to see if a course is
         * already stored within a databse and retrieve it from there.
         */
        this.course = new Course();
    }

    /**
     * The user has requested to add a new module to his course. Start the progress
     * bar, validate the input then add the module to the course.
     *
     * @param moduleTitle The module title entered by the user
     * @param moduleGrade The module grade entered by the user
     */
    public void addNewModuleToCourse(String moduleTitle, String moduleGrade) {
        courseView.showProgressBar();
        if (Module.validateModule(course, moduleTitle, moduleGrade, this) == true) {
            onModuleAdded(course.addModule(moduleTitle, moduleGrade));
        }
    }

    /**
     * Validation has passed and the business logic has been handled
     * by the model classes. Now lets show the user the results.
     */
    private void onModuleAdded(Module newModule) {
        courseView.removeNoEntriesTableRow();
        courseView.addModuleResultToTable(newModule.title, newModule.grade);
        courseView.updateOverallResults(course.calculateOverallClassification(), course.calculateAveragePercentage());
        courseView.clearEditTextFields();
        courseView.hideProgressBar();
        courseView.displayToastConfirmation();
    }

    /**
     * There was an error with the module title entered by the
     * user. Hide the progress bar and display the error.
     */
    public void onModuleTitleError() {
        courseView.hideProgressBar();
        courseView.setModuleTitleError();
    }

    /**
     * The User has tried to enter a module that has already been
     * added. Hide the progress bar and display the error.
     */
    public void onDuplicateModuleError() {
        courseView.hideProgressBar();
        courseView.setDuplicateModuleError();
    }

    /**
     * There was an error with the module grade entered by the
     * user. Hide the progress bar and display the error.
     */
    public void onModuleGradeError() {
        courseView.hideProgressBar();
        courseView.setModuleGradeError();
    }
}
