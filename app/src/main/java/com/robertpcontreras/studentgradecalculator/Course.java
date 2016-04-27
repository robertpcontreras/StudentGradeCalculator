package com.robertpcontreras.studentgradecalculator;

import java.util.ArrayList;

/**
 * Created by RobContreras on 15/01/16.
 *
 * This class is responsible for carrying out the business rules
 * related to courses. At the minute, the application can only
 * have one course, however use of this class will allow us
 * to assign multiple courses to a user in future upgrades.
 */
public class Course {

    /**
     * A course consists of multiple modules. This class stores
     * these modules in this array of Module objects.
     */
    public ArrayList<Module> modules;

    /**
     * Constructor to set the dependencies of the class. For now we set an
     * empty modules array, however going forward we can fetch any
     * existing course modules from a database if they exist.
     */
    public Course() {
        this.modules = new ArrayList<>();
    }

    /**
     * Add a module to the class but first we need to validate user input.
     * We can then pass the new module back to the presenter to
     * complete the request.
     *
     * @param moduleTitle The new module title entered by the user
     * @param moduleGrade The new module result entered by the user
     */
    public Module addModule(String moduleTitle, String moduleGrade) {
        Module newModule = new Module(this, moduleTitle, Integer.parseInt(moduleGrade));

        modules.add(newModule);

        return newModule;
    }

    /**
     * This function finds the module we are updating in the current list. The values
     * are then updated and returned. If no module is found, null is returned.
     *
     * @param moduleID The ID of the module we are updating.
     * @param moduleTitle The new title of the module we are updating
     * @param moduleGrade The new grade of the module we are updating
     * @return The updated module object
     */
    public Module updateModule(long moduleID, String moduleTitle, String moduleGrade) {
        // Find the new module and assign it
        Module module = findModuleByID(moduleID);

        // If the module is found, update it and return the new one.
        if (module != null) {
            module.title = moduleTitle;
            module.grade = Integer.parseInt(moduleGrade);

            return module;
        }

        // If no module can be found, return null.
        return null;
    }

    /**
     * Removes the module with the module ID passed in.
     *
     * @param moduleID The ID of the module we need to remove.
     */
    public void removeModule(long moduleID) {
        this.modules.remove(findModuleByID(moduleID));
    }

    /**
     * Calculates and returns the degree classification for the classes modules
     *
     * @return classification
     */
    public String calculateOverallClassification() {
        // First calculate the average Grade of all modules
        int averageGrade = calculateAveragePercentage();
        String result = "#N/A";

        // Now find the classification based on the Degree Classification Enum
        // and return the classification
        for (DegreeClassifications classification : DegreeClassifications.values()) {
            if (averageGrade >= classification.lowerGradeBoundary) {
                result = classification.description;
                break;
            }
        }

        return result;
    }

    /**
     * Calculates and returns the average percentage for the classes modules
     *
     * @return percentage
     */
    public int calculateAveragePercentage() {
        int sum = 0;
        int count = 0;

        // If there are no modules set the percentage to -1 representing an invalid result
        if (modules.size() == 0) {
            return -1;
        }

        for (Module module : modules) {
            sum += module.grade;
            count++;
        }

        return sum / count;
    }

    /**
     * Setter method to set methods that are retrieved from the database
     *
     * @param modules The modules to add to the course.
     */
    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    /**
     * Helper function to find a specific module by id in the modules ArrayList of this object.
     * As this logic will be used multiple times in the object this reduces redundant code.
     *
     * @param moduleID The id of the module we need to find
     * @return The module object that has been found, or null if not found.
     */
    private Module findModuleByID(long moduleID) {
        // Loop through the modules to find the module with the correct id
        // then remove it from the ArrayList.
        for (Module module : modules) {
            if (module._id == moduleID) {
                return module;
            }
        }

        return null;
    }
}
