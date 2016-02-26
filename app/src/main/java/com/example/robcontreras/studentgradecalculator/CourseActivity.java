package com.example.robcontreras.studentgradecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/**
 * Created by RobContreras on 15/01/16.
 *
 * This class is responsible for providing the animations
 * of the app including adding modules, updating the
 * overall result and displaying feedback.
 */
public class CourseActivity extends AppCompatActivity implements CourseView {

    /**
     * Defining our view objects. We will need to interact with these later so in order to
     * make this as easy as possible we will store them as class properties.
     */
    private EditText moduleTitle;
    private EditText moduleGrade;
    private Button addNewModuleButton;
    private TextView overallClassificationValue;
    private TextView averagePercentageValue;
    private TableLayout modulesTable;
    private TableRow noEntriesTableRow;
    private ProgressBar progressBar;

    /**
     * Defining activities presenter interface. We will
     * pass all requests to be dealt with here.
     */
    private CoursePresenter presenter;

    /**
     * Our on create method. We will initialise all our properties,
     * instatiate out presenter class and event listeners.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Calls the parents constructor. Lots of complex android stuff
         * in here that we do not want to write ourself.
         */
        super.onCreate(savedInstanceState);

        /**
         * Set the view for the activity from the XML file in the layout folder.
         */
        setContentView(R.layout.activity_grade_calculator);

        /**
         * Assigning the activities presenter.
         */
        presenter = new CoursePresenter(this);

        /**
         * Assigning all our view objects by their respective id's
         */
        moduleTitle = (EditText) findViewById(R.id.moduleTitle);
        moduleGrade = (EditText) findViewById(R.id.moduleGrade);
        addNewModuleButton = (Button) findViewById(R.id.addNewModuleButton);
        overallClassificationValue = (TextView) findViewById(R.id.overallClassificationValue);
        averagePercentageValue = (TextView) findViewById(R.id.averagePercentageValue);
        modulesTable = (TableLayout) findViewById(R.id.modulesTable);
        noEntriesTableRow = (TableRow) findViewById(R.id.noEntriesTableRow);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * This is the function we specified in the layout resourse to fire on the click
     * of the add module button. We will pass the relevant information onto
     * the presenter. It's their job to deal with the request.
     */
    public void onAddModuleClick(View view) {
        presenter.addNewModuleToCourse(moduleTitle.getText().toString(), moduleGrade.getText().toString());
    }

    /**
     * Show the progress bar and hide the add module button
     * until our request has been dealt with.
     */
    public void showProgressBar() {
        addNewModuleButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * The request has been dealt with. Hide the progress
     * bar and show the add new module button.
     */
    public void hideProgressBar() {
        addNewModuleButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * The user entered an invalid module
     * title. Show them an error message.
     */
    public void setModuleTitleError() {
        moduleTitle.setError(getString(R.string.module_title_error));
    }

    /**
     * The user entered an invalid module
     * title. Show them an error message.
     */
    public void setDuplicateModuleError() {
        moduleTitle.setError(getString(R.string.duplicate_module_error));
    }

    /**
     * The user entered a duplicate module
     * Show them an error message.
     */
    public void setModuleGradeError() {
        moduleGrade.setError(getString(R.string.module_grade_error));
    }

    /**
     * We need to add the newly added module to the modules table.
     *
     * @param moduleTitle The title of the new module we need to add to the module table.
     * @param moduleGrade The grade of the new module we need to add to the module table.
     */
    public void addModuleResultToTable(String moduleTitle, int moduleGrade) {
        //Instantiate a new table row
        TableRow newModuleRow = new TableRow(this);

        // Instantiate new text views to display the module title and grade
        TextView newModuleTitle = new TextView(this);
        TextView newModuleGrade = new TextView(this);

        // Apply the new module values to the view objects
        newModuleTitle.setText(moduleTitle);
        newModuleTitle.setGravity(Gravity.CENTER);
        newModuleGrade.setText(String.valueOf(moduleGrade) + "%");
        newModuleGrade.setGravity(Gravity.CENTER);

        // Add the new view objects to the row.
        newModuleRow.addView(newModuleTitle);
        newModuleRow.addView(newModuleGrade);

        // Add the new row to the module table
        modulesTable.addView(newModuleRow);
    }

    /**
     * We have recieved new overall results. Lets update them
     *
     * @param classification The new overall degree classification
     * @param percentage     The new average module grade percentage
     */
    public void updateOverallResults(String classification, int percentage) {
        overallClassificationValue.setText(classification);
        averagePercentageValue.setText(String.valueOf(percentage) + "%");

    }

    /**
     * Display Toast Confirmation to the user that the new module has been
     * added and the table and overall results have been updated.
     */
    public void displayToastConfirmation() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getText(R.string.toast_new_module_confirmation),
                Toast.LENGTH_SHORT
        );
        toast.show();
    }

    /**
     * The previous module has been added to the table so we need to
     * clear the fields to allow the user to enter new modules
     */
    public void clearEditTextFields() {
        moduleTitle.getText().clear();
        moduleGrade.getText().clear();
    }

    /**
     * Check if exists and remove the no entries table row that is initially displayed.
     */
    public void removeNoEntriesTableRow() {
        if (noEntriesTableRow.getParent() != null){
            modulesTable.removeView(noEntriesTableRow);
        }
    }
}
