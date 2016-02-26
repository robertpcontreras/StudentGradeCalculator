package com.example.robcontreras.studentgradecalculator;

/**
 * Created by RobContreras on 15/01/16.
 *
 * This interface provides a contract the presenter must implement
 * in ordert to correctly show any validation erros returned by
 * the Module class.
 */
public interface ModuleValidationErrorListener {
    void onModuleTitleError();
    void onDuplicateModuleError();
    void onModuleGradeError();
}
