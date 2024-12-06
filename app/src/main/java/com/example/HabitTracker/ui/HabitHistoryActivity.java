package com.example.HabitTracker.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.HabitTracker.R;
import adapter.HabitHistoryAdapter;
import data.Habit;
import viewmodel.HabitViewModel;

import java.util.List;

public class HabitHistoryActivity extends AppCompatActivity {

    private HabitViewModel habitViewModel;
    private HabitHistoryAdapter habitHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        // Initialize the RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.rvHabitHistory);
        habitHistoryAdapter = new HabitHistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(habitHistoryAdapter);

        // ViewModel to fetch completed habits from the database
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Fetch completed habits today (using the new field 'isCompletedToday')
        habitViewModel.getCompletedHabitsToday().observe(this, habits -> {
            if (habits != null && !habits.isEmpty()) {
                // Submit the list of completed habits to the adapter
                habitHistoryAdapter.submitList(habits);

                // Hide "No history" view and show the RecyclerView
                findViewById(R.id.noHistoryContainer).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                // Set the completed habit count
                TextView completedCount = findViewById(R.id.completedCount);
                completedCount.setText("You have completed " + habits.size() + " habit(s) today!");
            } else {
                // Show "No history" if no habits are completed
                findViewById(R.id.noHistoryContainer).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
