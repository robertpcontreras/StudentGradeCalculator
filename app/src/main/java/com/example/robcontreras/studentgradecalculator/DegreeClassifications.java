package com.example.robcontreras.studentgradecalculator;

/**
 * Created by RobContreras on 17/01/16.
 *
 * This enum defines the default degree classifications including a description and lower grade boudaries.
 * We use the properties of this enum when finding the classifaction after the average percentage has
 * been worked out. Storing these details here will easily allow us to change the enum into a
 * model if the classifications need to become dynamic in the future.
 */
public enum DegreeClassifications {
    /**
     * Defining the constants and their parameters
     */
    FIRST("First Class Honours", 70),
    UPPER_SECOND("Upper Second Class Honours", 60),
    LOWER_SECOND("Lower Second Class Honours", 50),
    THIRD("Third Class Honours", 40),
    FAIL("Fail", 0);

    /**
     * Defining the constant properties
     */
    public final String description;
    public final int lowerGradeBoundary;

    /**
     * The enum constructor sets properties against the constants.
     *
     * @param description        The description of the degree classification
     * @param lowerGradeBoundary The lower grade boundary of the degree classification
     */
    DegreeClassifications(String description, int lowerGradeBoundary) {
        this.description = description;
        this.lowerGradeBoundary = lowerGradeBoundary;
    }
}
