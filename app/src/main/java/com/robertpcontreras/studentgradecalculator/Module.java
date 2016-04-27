package com.robertpcontreras.studentgradecalculator;

/**
 * Created by RobContreras on 15/01/16.
 *
 * This class is responsible for providing the business rules
 * related to modules. At the moment this is only responsible
 * for validating a module before it is added to the course,
 * however having this will ensure the addition of features
 * relating to modules will be as easy as possible in the
 * future. For example, being able to save specific modules
 * to a database, giving users an option to select from
 * a list of added modules when inputting their results.
 */
public class Module {

    /**
     * Defining class properties
     */
    public long _id;
    private Course course;
    public String title;
    public int grade;

    /**
     * Constructor to set the class dependencies
     *
     * @param course The course this module belongs to
     * @param title  The modules title
     * @param grade  The modules grade
     */
    public Module(Course course, String title, int grade) {
        this.course = course;
        this.title = title;
        this.grade = grade;
    }

    /**
     * Validates the values that will be used to create the object. This is a static
     * method allowing us to call the method without an instance of the object.
     *
     * @param moduleTitle   The title that the user has entered
     * @param moduleGrade   The grade that the user has entered
     * @param errorListener An instance of the ModuleValidationErrorListener interface allowing us to call Error Events
     * @return boolean      The result of the validation
     */
    public static boolean validateModule(Course course, String moduleTitle, String moduleGrade, ModuleValidationErrorListener errorListener) {

        /**
         * Call our helper function to check the values passed in are all valid
         */
        if (! validateFormat(course, moduleTitle, moduleGrade, errorListener)){
            return false;
        }

        // Check to see if the same module has not already been added
        for (Module module : course.modules) {
            if (moduleTitle.equals(module.title)) {
                errorListener.onDuplicateModuleError();
                return false;
            }
        }

        return true;
    }

    /**
     * Validates the values that will be used to create the object. This is a static
     * method allowing us to call the method without an instance of the object.
     *
     * @param moduleTitle   The title that the user has entered
     * @param moduleGrade   The grade that the user has entered
     * @param errorListener An instance of the ModuleValidationErrorListener interface allowing us to call Error Events
     * @return boolean      The result of the validation
     */
    public static boolean validateModule(long moduleID, Course course, String moduleTitle, String moduleGrade, ModuleValidationErrorListener errorListener) {

        /**
         * Call our helper function to check the values passed in are all valid
         */
        if (! validateFormat(course, moduleTitle, moduleGrade, errorListener)){
            return false;
        }

        // Check to see if the same module has not already been added but only if it has a different id
        for (Module module : course.modules) {
            if (moduleTitle.equals(module.title) && module._id != moduleID) {
                errorListener.onDuplicateModuleError();
                return false;
            }
        }

        return true;
    }

    private static boolean validateFormat(Course course, String moduleTitle, String moduleGrade, ModuleValidationErrorListener errorListener) {

        // Check to see if the user has entered a module title.
        if (!moduleTitle.matches(".+")) {
            errorListener.onModuleTitleError();
            return false;
        }

        // Check to see if a grade has been entered and it is an integer
        if (!moduleGrade.matches("\\d+")) {
            errorListener.onModuleGradeError();
            return false;
        }

        // Module grade is an int. Now we can cast to int and check range for valid percentage.
        int percentage = Integer.parseInt(moduleGrade);

        if (percentage < 0 || percentage > 100) {
            errorListener.onModuleGradeError();
            return false;
        }

        return true;
    }
}
