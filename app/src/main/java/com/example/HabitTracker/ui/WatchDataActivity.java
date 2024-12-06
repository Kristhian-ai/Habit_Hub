package com.example.HabitTracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.HabitTracker.R;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.FitnessOptions;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class WatchDataActivity extends AppCompatActivity {

    private static final int FITNESS_PERMISSIONS_REQUEST_CODE = 1001;
    private GoogleSignInAccount googleSignInAccount;

    private TextView tvSteps, tvCalories, tvDistance, tvHeartRate, tvSleep, tvWeight, tvActiveMinutes;
    private Button btnBackToHome; // Declare the button for going back to home

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_data);

        // Initialize TextViews
        tvSteps = findViewById(R.id.tvSteps);
        tvCalories = findViewById(R.id.tvCalories);
        tvDistance = findViewById(R.id.tvDistance);
        tvHeartRate = findViewById(R.id.tvHeartRate);
        tvSleep = findViewById(R.id.tvSleep);
        tvActiveMinutes = findViewById(R.id.tvActiveMinutes);

        // Initialize Back to Home Button
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            // Navigate back to HabitTrackingActivity
            Intent intent = new Intent(WatchDataActivity.this, HabitTrackingActivity.class);
            startActivity(intent);
            finish(); // Optionally finish this activity to remove it from the stack
        });

        // Check for existing Google Sign-In account
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleSignInAccount == null) {
            signInToGoogleFit();  // Sign in to Google Fit if not signed in
        } else {
            checkPermissionsAndLoadData(); // Check permissions and load data if already signed in
        }
    }

    // Sign-in to Google Fit if not already signed in
    private void signInToGoogleFit() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        GoogleSignIn.requestPermissions(
                this,
                FITNESS_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions);
    }

    // Handle the result of Google Sign-In permission request
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FITNESS_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                googleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(data).getResult();
                checkPermissionsAndLoadData();  // Check permissions and load data after sign-in
            } else {
                Toast.makeText(this, "Data Sync", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Check if Google Fit permissions are granted before loading data
    private void checkPermissionsAndLoadData() {
        if (googleSignInAccount == null) {
            Toast.makeText(this, "Google Fit account is null. Please sign in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if permissions are granted for the required data types
        Fitness.getHistoryClient(this, googleSignInAccount)
                .readData(createDataReadRequest()) // Pass the DataReadRequest here
                .addOnSuccessListener(response -> loadSmartwatchData())
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Permissions not granted. Please re-enable them.", Toast.LENGTH_SHORT).show();
                    Log.e("GoogleFit", "Error checking permissions", e);
                });
    }

    // Create a DataReadRequest to fetch the required data
    private DataReadRequest createDataReadRequest() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - TimeUnit.DAYS.toMillis(1);

        return new DataReadRequest.Builder()
                .read(DataType.TYPE_STEP_COUNT_DELTA)
                .read(DataType.TYPE_CALORIES_EXPENDED)
                .read(DataType.TYPE_DISTANCE_DELTA)
                .read(DataType.TYPE_HEART_RATE_BPM)
                .read(DataType.TYPE_SLEEP_SEGMENT)
                .read(DataType.TYPE_WEIGHT)
                .read(DataType.TYPE_ACTIVITY_SEGMENT)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    // Fetch and load data from Google Fit
    private void loadSmartwatchData() {
        if (googleSignInAccount == null) {
            Toast.makeText(this, "Google Fit account is null", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the data from Google Fit
        Fitness.getHistoryClient(this, googleSignInAccount)
                .readData(createDataReadRequest()) // Use the created DataReadRequest
                .addOnSuccessListener(this::handleDataReadResponse)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to read data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("GoogleFit", "Error reading data", e);
                });
    }

    // Handle the data read response from Google Fit
    private void handleDataReadResponse(DataReadResponse dataReadResponse) {
        for (DataSet dataSet : dataReadResponse.getDataSets()) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                processDataPoint(dp);
            }
        }
    }

    // Process individual data points
    private void processDataPoint(DataPoint dp) {
        if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
            int steps = dp.getValue(Field.FIELD_STEPS).asInt();
            tvSteps.setText("Steps: " + steps);
        } else if (dp.getDataType().equals(DataType.TYPE_CALORIES_EXPENDED)) {
            float calories = dp.getValue(Field.FIELD_CALORIES).asFloat();
            tvCalories.setText("Calories: " + calories);
        } else if (dp.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
            float distance = dp.getValue(Field.FIELD_DISTANCE).asFloat();
            tvDistance.setText("Distance: " + distance + " km");
        } else if (dp.getDataType().equals(DataType.TYPE_HEART_RATE_BPM)) {
            float heartRate = dp.getValue(Field.FIELD_BPM).asFloat();
            tvHeartRate.setText("Heart Rate: " + heartRate + " BPM");
        } else if (dp.getDataType().equals(DataType.TYPE_SLEEP_SEGMENT)) {
            int sleepStage = dp.getValue(Field.FIELD_SLEEP_SEGMENT_TYPE).asInt();
            tvSleep.setText("Sleep Stage: " + sleepStage);
        } else if (dp.getDataType().equals(DataType.TYPE_ACTIVITY_SEGMENT)) {
            String activityType = dp.getValue(Field.FIELD_ACTIVITY).asString();
            tvActiveMinutes.setText("Activity: " + activityType);
        }
    }
}
