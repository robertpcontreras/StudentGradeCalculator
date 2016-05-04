package com.robertpcontreras.studentgradecalculator;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by RobContreras on 15/01/16.
 * <p/>
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
    private Button saveModuleButton;
    private Button deleteModuleButton;
    private TextView overallClassificationValue;
    private TextView averagePercentageValue;
    private TableLayout modulesTable;
    private TableRow noEntriesTableRow;
    private ProgressBar progressBar;

    /**
     * Stores the id of the module if we are editing one.
     */
    private long editingModuleID;

    private ArrayList<TableRow> moduleRows;

    /**
     * The DB helper that Android uses to create and manage our SQLite DB.
     */
    StudentCalculatorSQLiteHelper dbHelper;

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
         * Assigning all our view objects by their respective id's
         */
        moduleTitle = (EditText) findViewById(R.id.moduleTitle);
        moduleGrade = (EditText) findViewById(R.id.moduleGrade);
        addNewModuleButton = (Button) findViewById(R.id.addNewModuleButton);
        saveModuleButton = (Button) findViewById(R.id.saveModuleButton);
        deleteModuleButton = (Button) findViewById(R.id.deleteModuleButton);
        overallClassificationValue = (TextView) findViewById(R.id.overallClassificationValue);
        averagePercentageValue = (TextView) findViewById(R.id.averagePercentageValue);
        modulesTable = (TableLayout) findViewById(R.id.modulesTable);
        noEntriesTableRow = (TableRow) findViewById(R.id.noEntriesTableRow);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        moduleRows = new ArrayList<>();

        /**
         * Now we are integrating a database, we need to instantiate our SQLite Helper Class.
         * This class ensures the correct version of the database has been created.
         */
        dbHelper = new StudentCalculatorSQLiteHelper(this);

        /**
         * We create the android and sqlite compatible instance of the ModuleDAO Interface
         * and pass it to the presenter so it can be used to manage the data within
         * the database.
         */
        ModuleDAOInterface moduleDAO = new ModuleDAO(dbHelper);

        /**
         * Assigning the activities presenter.
         */
        presenter = new CoursePresenter(this, moduleDAO);
    }

    /**
     * This is the function we specified in the layout resourse to fire on the click
     * of the add module button. We will pass the relevant information onto
     * the presenter. It's their job to deal with the request.
     */
    public void onAddModuleClick(View view) {
        presenter.addNewModuleToCourse(moduleTitle.getText().toString(), moduleGrade.getText().toString());
    }

    public void onSaveModuleClick(View view) {
        presenter.updateModule(editingModuleID, moduleTitle.getText().toString(), moduleGrade.getText().toString());
    }

    public void onDeleteModuleClick(View view) {
        presenter.deleteModule(editingModuleID);
    }

    /**
     * Show the progress bar and hide the add module button
     * until our request has been dealt with.
     */
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * The request has been dealt with. Hide the progress
     * bar and show the add new module button.
     */
    public void hideProgressBar() {
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
     * Insert the values of the module passed in to the EditText Objects as
     * the user wants to edit the module.
     *
     * @param module The module we are editing
     */
    public void addModuleToEditFields(Module module) {
        moduleTitle.setText(module.title);
        moduleGrade.setText(String.valueOf(module.grade));
    }

    /**
     * Show the buttons required for updating, or deleting a module.
     * Need to hide the add module button as well!
     */
    public void showEditButtons() {
        saveModuleButton.setVisibility(View.VISIBLE);
        deleteModuleButton.setVisibility(View.VISIBLE);
        addNewModuleButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Show the buttons required for adding new modules. This is default,
     * we only need to do it when a module has been updated or deleted.
     */
    public void showAddButton() {
        saveModuleButton.setVisibility(View.INVISIBLE);
        deleteModuleButton.setVisibility(View.INVISIBLE);
        addNewModuleButton.setVisibility(View.VISIBLE);
    }

    /**
     * We need to add the newly added module to the modules table.
     *
     * @param moduleID The id of the new module we need to add to the module table.
     * @param moduleTitle The title of the new module we need to add to the module table.
     * @param moduleGrade The grade of the new module we need to add to the module table.
     */
    public void addModuleResultToTable(final long moduleID, String moduleTitle, int moduleGrade) {
        //Instantiate a new table row
        TableRow newModuleRow = new TableRow(this);

        // Instantiate new text views to display the module title and grade
        TextView newModuleTitle = new TextView(this);
        TextView newModuleGrade = new TextView(this);
        ImageButton newModuleEditBtn = new ImageButton(this);

        // Apply the new module values to the view objects
        newModuleTitle.setText(moduleTitle);
        newModuleTitle.setGravity(Gravity.CENTER);
        newModuleGrade.setText(String.valueOf(moduleGrade) + "%");
        newModuleGrade.setGravity(Gravity.CENTER);
        newModuleEditBtn.setImageResource(R.drawable.ic_build_cyan_a400_18dp);

        // Add the new view objects to the row.
        newModuleRow.addView(newModuleTitle);
        newModuleRow.addView(newModuleGrade);
        newModuleRow.addView(newModuleEditBtn);

        // Add the row to the moduleRows List so we can keep track of it
        moduleRows.add(newModuleRow);

        // Add the new row to the module table
        modulesTable.addView(newModuleRow);

        newModuleEditBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                presenter.moduleEditClick(moduleID);
                editingModuleID = moduleID;
            }
        });
    }

    /**
     * If we delete all our modules we need to add the No Entries Row again
     */
    public void addNoEntriesTableRow() {
        modulesTable.addView(noEntriesTableRow);
    }

    /**
     * We have received new overall results. Lets update them
     *
     * @param classification The new overall degree classification
     * @param percentage     The new average module grade percentage
     */
    public void updateOverallResults(String classification, int percentage) {

        // If -1 means there are no modules, therefore just set an empty text view.
        if (percentage == -1) {
           averagePercentageValue.setText("");
        } else {
            averagePercentageValue.setText(String.valueOf(percentage) + "%");
        }

        overallClassificationValue.setText(classification);
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
     * Display toast confirmation that the module has been successfully updated.
     */
    public void displayUpdateToastConfirmation() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getText(R.string.toast_update_module_confirmation),
                Toast.LENGTH_SHORT
        );
        toast.show();
    }

    /**
     * Display toast confirmation that the module has been successfully deleted.
     */
    public void displayDeleteToastConfirmation() {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getText(R.string.toast_delete_module_confirmation),
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
        if (noEntriesTableRow.getParent() != null) {
            modulesTable.removeView(noEntriesTableRow);
        }
    }

    /**
     * Function removes all rows from table so it can be regenerated
     */
    public void removeAllTableRows () {
        if (moduleRows.size() > 0) {
            modulesTable.removeViewsInLayout(1,moduleRows.size());
            moduleRows.clear();
        }
    }
}
