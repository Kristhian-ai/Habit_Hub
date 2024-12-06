package com.example.HabitTracker.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.HabitTracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import data.Habit;
import viewmodel.HabitViewModel;

public class AddEditHabitActivity extends AppCompatActivity {

    private HabitViewModel habitViewModel;
    private AutoCompleteTextView habitNameAutoComplete;
    private TextView habitDescriptionEditText;
    private Spinner habitFrequencySpinner;
    private Button selectDatesButton;
    private String startDate = "";
    private String endDate = "";
    private TextView tvSelectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_habit);

        // Initialize ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Initialize UI elements
        habitNameAutoComplete = findViewById(R.id.etHabitName);
        habitDescriptionEditText = findViewById(R.id.etHabitDescription);
        habitFrequencySpinner = findViewById(R.id.spinnerFrequency);
        selectDatesButton = findViewById(R.id.btnSelectDates);
        tvSelectedDates = findViewById(R.id.tvSelectedDate);
        Button saveButton = findViewById(R.id.btnSaveHabit);

        // Setup components
        setupFrequencySpinner();
        setupDatePicker();
        setupAutoCompleteHabitName();

        // Save Button Click Listener
        saveButton.setOnClickListener(v -> {
            String name = habitNameAutoComplete.getText().toString().trim();
            String description = habitDescriptionEditText.getText().toString().trim();

            // Validate Inputs
            if (name.isEmpty() || description.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields and select dates.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert startDate and endDate from String to long
            long startDateMillis = convertDateToMillis(startDate);
            long endDateMillis = convertDateToMillis(endDate);

            Log.d("HabitActivity", "Start Date: " + startDateMillis);
            Log.d("HabitActivity", "End Date: " + endDateMillis);

            if (startDateMillis == -1 || endDateMillis == -1) {
                Toast.makeText(this, "Invalid date format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create Habit and Save to Database (use the ViewModel's insert method)
            Habit habit = new Habit(name, description, startDateMillis, endDateMillis);
            habitViewModel.insert(habit);  // Make sure insert is done asynchronously in ViewModel

            // Return to Previous Screen
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void setupFrequencySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.habit_frequencies,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitFrequencySpinner.setAdapter(adapter);
    }

    private void setupDatePicker() {
        selectDatesButton.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            // Start Date Picker
            DatePickerDialog startDatePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                startDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                // End Date Picker
                DatePickerDialog endDatePicker = new DatePickerDialog(this, (view1, endYear, endMonth, endDayOfMonth) -> {
                    endDate = endYear + "-" + (endMonth + 1) + "-" + endDayOfMonth;
                    tvSelectedDates.setText("Selected dates: " + startDate + " to " + endDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                endDatePicker.show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            startDatePicker.show();
        });
    }

    private void setupAutoCompleteHabitName() {
        List<String> recommendations = getRecommendations();

        // Create an ArrayAdapter with the recommendations
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                recommendations
        );

        habitNameAutoComplete.setAdapter(adapter);

        // When the user selects a recommendation, update the description automatically
        habitNameAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHabit = (String) parent.getItemAtPosition(position);
            habitNameAutoComplete.setText(selectedHabit);
            habitDescriptionEditText.setText("Start a habit to " + selectedHabit.toLowerCase());
        });
    }

    private List<String> getRecommendations() {
        return Arrays.asList(
                "Exercise Daily",
                "Meditate for 10 minutes",
                "Read a Book",
                "Drink More Water",
                "Practice Gratitude",
                "Learn a New Skill",
                "Wake Up Early",
                "Journal Your Thoughts"
        );
    }

    // Helper Method to Convert Date Strings to Milliseconds
    private long convertDateToMillis(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
