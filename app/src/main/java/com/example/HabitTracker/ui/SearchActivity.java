package com.example.HabitTracker.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.HabitTracker.R;

import adapter.HabitAdapter;
import viewmodel.HabitViewModel;

public class SearchActivity extends AppCompatActivity {

    private HabitViewModel habitViewModel;
    private HabitAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearchResults);
        habitAdapter = new HabitAdapter();
        recyclerView.setAdapter(habitAdapter);

        // Initialize ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Setup SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optionally, update results as the user types
                performSearch(newText);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        // Correct method call: Call searchHabits() instead of searchHabitByName
        habitViewModel.searchHabits(query).observe(this, habits -> {
            if (habits != null && !habits.isEmpty()) {
                habitAdapter.submitList(habits); // Update RecyclerView with filtered habits
            } else {
                Toast.makeText(this, "No habits found for \"" + query + "\"", Toast.LENGTH_SHORT).show();
                habitAdapter.submitList(null); // Clear RecyclerView if no results
            }
        });
    }
}
