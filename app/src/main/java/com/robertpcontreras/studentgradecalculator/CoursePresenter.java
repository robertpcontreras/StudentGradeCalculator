package com.robertpcontreras.studentgradecalculator;

import java.util.ArrayList;

/**
 * Created by RobContreras on 15/01/16.
 */
public class CoursePresenter implements ModuleValidationErrorListener {

    /**
     * Defining class properties
     */
    private CourseView courseView;
    private ModuleDAOInterface moduleDAO;
    public Course course;

    /**
     * Constructor sets the presenters dependencies.
     *
     * @param courseView The activity that instantiated the object
     */
    public CoursePresenter(CourseView courseView, ModuleDAOInterface moduleDAO) {

        /**
         * Assigning the view to the presenter. This will
         * allow us to modify the view when required.
         */
        this.courseView = courseView;

        /**
         * Instantiate a new course. This can now be used to add modules and calculate
         * the overall grades. Going forward we can check to see if a course is
         * already stored within a database and retrieve it from there.
         */
        this.course = new Course();

        // Assign the Module data access object so we can use it throughout the presenter.
        this.moduleDAO = moduleDAO;

        // Assign the course in the data access object as we need it there.
        moduleDAO.setCourse(course);

        // Find any modules stored in the database.
        ArrayList<Module> modules = moduleDAO.fetchAllModules();

        // If modules are found, assign them and display them in the view.
        if (modules.size() > 0) {
            course.setModules(modules);
            createTableFromModules(modules);
        }
    }

    /**
     * The user has requested to add a new module to his course. Start the progress
     * bar, validate the input then add the module to the course.
     *
     * @param moduleTitle The module title entered by the user
     * @param moduleGrade The module grade entered by the user
     */
    public void addNewModuleToCourse(String moduleTitle, String moduleGrade) {
        // Show the progress bar while this function is being processed.
        courseView.showProgressBar();

        // Check if the module values are valid.
        if (Module.validateModule(course, moduleTitle, moduleGrade, this)) {

            // Create the new module object
            Module module = course.addModule(moduleTitle, moduleGrade);

            // Add the module to the database and assign the new _id
            module._id = moduleDAO.insertModule(module);

            // Call the onModuleAdded method to update the view as required.
            this.onModuleAdded(module);
        }
    }

    /**
     * This function updates the module on the course object as well as in the database.
     *
     * @param moduleID The id of the module we are updating
     * @param moduleTitle The new title of the module
     * @param moduleGrade The new grade of the module
     */
    public void updateModule(long moduleID, String moduleTitle, String moduleGrade) {
        // Show the progress bar while this function is being processed.
        courseView.showProgressBar();

        if (Module.validateModule(moduleID, course, moduleTitle, moduleGrade, this)) {
            // Update value in modules ArrayList on Course Object
            Module updatedModule = course.updateModule(moduleID, moduleTitle, moduleGrade);

            // Update the module in the database
            moduleDAO.updateModule(moduleID, updatedModule);

            // Call the onModuleAdded method to update the view as required.
            this.onModuleUpdated();
        }
    }

    public void deleteModule(long moduleID) {
        // Show the progress bar while this function is being processed.
        courseView.showProgressBar();

        //Delete the module in the database
        moduleDAO.deleteModule(moduleID);

        // Remove the module from the modules array on the course object
        course.removeModule(moduleID);

        // Call then on module deleted function to update the view.
        this.onModuleDeleted();
    }

    /**
     * We have retrieved these modules from the database so we need to tell the view
     * to display them.
     *
     * @param modules The modules we need to display
     */
    private void createTableFromModules(ArrayList<Module> modules) {
        // Show the progress bar while this function is being processed.
        courseView.showProgressBar();

        // Removes all the current table rows as we are resetting the table with the current modules.
        courseView.removeNoEntriesTableRow();
        courseView.removeAllTableRows();

        if (modules.size() == 0) {
            courseView.addNoEntriesTableRow();
            courseView.updateOverallResults("", -1);
        } else {
            // Loop through the modules adding them with the addModuleResult to table.
            for (Module module : modules) {
                courseView.addModuleResultToTable(module._id, module.title, module.grade);
            }
            // Update the overall results for the course.
            courseView.updateOverallResults(course.calculateOverallClassification(), course.calculateAveragePercentage());
        }

        // Ensure the text fields are blank.
        courseView.clearEditTextFields();

        // Hide the progress bar.
        courseView.hideProgressBar();
    }

    /**
     * Function gets the module required from the database then sends it back to the view
     * to populate the TextView objects.
     *
     * @param moduleID The _id of the module we need to get
     */
    public void moduleEditClick(long moduleID) {
        // Fetch the module we are editing from the database
        Module module = moduleDAO.fetchModuleByID(moduleID);

        // Add the module to the edit text fields on the view so they can be updated
        courseView.addModuleToEditFields(module);

        // Show the buttons required to update or delete the module.
        courseView.showEditButtons();
    }

    /**
     * Validation has passed and the business logic has been handled
     * by the model classes. Now lets show the user the results.
     */
    private void onModuleAdded(Module newModule) {
        courseView.removeNoEntriesTableRow();
        courseView.addModuleResultToTable(newModule._id, newModule.title, newModule.grade);
        courseView.updateOverallResults(course.calculateOverallClassification(), course.calculateAveragePercentage());
        courseView.clearEditTextFields();
        courseView.showAddButton();
        courseView.hideProgressBar();
        courseView.displayToastConfirmation();
    }

    private void onModuleUpdated() {
        createTableFromModules(course.modules);
        courseView.updateOverallResults(course.calculateOverallClassification(), course.calculateAveragePercentage());
        courseView.clearEditTextFields();
        courseView.showAddButton();
        courseView.hideProgressBar();
        courseView.displayUpdateToastConfirmation();
    }

    private void onModuleDeleted() {
        createTableFromModules(course.modules);
        courseView.updateOverallResults(course.calculateOverallClassification(), course.calculateAveragePercentage());
        courseView.clearEditTextFields();
        courseView.showAddButton();
        courseView.hideProgressBar();
        courseView.displayDeleteToastConfirmation();
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
