package com.example.HabitTracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HabitTracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import adapter.HabitAdapter;
import viewmodel.HabitViewModel;

public class HabitTrackingActivity extends AppCompatActivity implements HabitAdapter.HabitAdapterListener {

    private HabitViewModel habitViewModel;
    private HabitAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_habit);

        // Initialize ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Initialize FloatingActionButtons and set click listeners
        initializeHabitViews();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHabits);
        if (recyclerView != null) {
            habitAdapter = new HabitAdapter(this); // Pass `this` as the listener
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(habitAdapter);
        }

        // Observe habit data from the ViewModel
        habitViewModel.getAllHabits().observe(this, habits -> {
            if (habits != null) {
                habitAdapter.submitList(habits);
            }

            // Handle empty state
            View emptyStateContainer = findViewById(R.id.emptyStateContainer);
            if (habits == null || habits.isEmpty()) {
                emptyStateContainer.setVisibility(View.VISIBLE);
            } else {
                emptyStateContainer.setVisibility(View.GONE);
            }
        });
    }

    private void initializeHabitViews() {
        FloatingActionButton fabAddHabit = findViewById(R.id.fabAddHabit);
        if (fabAddHabit != null) {
            fabAddHabit.setOnClickListener(v -> {
                Intent intent = new Intent(HabitTrackingActivity.this, AddEditHabitActivity.class);
                startActivity(intent);
            });
        }

        FloatingActionButton fabWatchData = findViewById(R.id.fabWatchData);
        if (fabWatchData != null) {
            fabWatchData.setOnClickListener(v -> {
                Intent intent = new Intent(HabitTrackingActivity.this, WatchDataActivity.class);
                startActivity(intent);
            });
        }

        FloatingActionButton fabSearch = findViewById(R.id.fabSearch);
        if (fabSearch != null) {
            fabSearch.setOnClickListener(v -> {
                Intent intent = new Intent(HabitTrackingActivity.this, SearchActivity.class);
                startActivity(intent);
            });
        }

        FloatingActionButton fabSettings = findViewById(R.id.fabSettings);
        if (fabSettings != null) {
            fabSettings.setOnClickListener(v -> {
                Intent intent = new Intent(HabitTrackingActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Ensure correct menu layout
        return true;
    }



    // Implement the HabitAdapterListener interface method
    @Override
    public void onHabitChecked(int position, boolean isChecked) {
        // Update the habit in the ViewModel or local data source as needed
        habitViewModel.updateHabitChecked(position, isChecked);
    }
}
