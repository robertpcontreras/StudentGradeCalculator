package com.example.robcontreras.studentgradecalculator;

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

        for (Module module : modules) {
            sum += module.grade;
            count++;
        }

        return sum / count;
    }
}
