package com.robertpcontreras.studentgradecalculator;

/**
 * Created by RobContreras on 17/01/16.
 *
 * This internace defines the contract that the CourseActivity
 * class must follow. Implementing this contract ensures our
 * presenter and business models dont have to change if
 * for some reason we cannot use the current CourseActivity
 * class in the future.
 */
public interface CourseView {

    void showProgressBar();
    void removeNoEntriesTableRow();
    void addModuleResultToTable(long moduleID, String classification, int percentage);
    void updateOverallResults(String classification, int percentage);
    void displayToastConfirmation();
    void displayUpdateToastConfirmation();
    void displayDeleteToastConfirmation();
    void clearEditTextFields();
    void hideProgressBar();
    void setModuleTitleError();
    void setDuplicateModuleError();
    void setModuleGradeError();
    void addModuleToEditFields(Module module);
    void removeAllTableRows();
    void showEditButtons();
    void showAddButton();
    void addNoEntriesTableRow();
}
