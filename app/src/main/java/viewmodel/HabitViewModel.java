package viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Habit;
import data.HabitRepository;

public class HabitViewModel extends AndroidViewModel {

    private HabitRepository habitRepository;

    // Constructor to initialize repository and LiveData
    public HabitViewModel(Application application) {
        super(application);
        habitRepository = new HabitRepository(application); // Initialize the repository
    }

    // Get all habits as LiveData
    public LiveData<List<Habit>> getAllHabits() {
        return habitRepository.getAllHabits();  // Observes data changes and updates UI automatically
    }

    // Search for habits by query
    public LiveData<List<Habit>> searchHabits(String query) {
        return habitRepository.searchHabits(query);
    }

    // Fetch completed habits today
    public LiveData<List<Habit>> getCompletedHabitsToday() {
        return habitRepository.getCompletedHabitsToday();
    }

    // Get habits by date range (start date to end date)
    public LiveData<List<Habit>> getHabitsByDateRange(long startDate, long endDate) {
        return habitRepository.getHabitsByDateRange(startDate, endDate);
    }

    // Count the total number of habits in the database
    public LiveData<Integer> countHabits() {
        return habitRepository.countHabits();
    }

    // Insert a new habit asynchronously
    public void insert(Habit habit) {
        habitRepository.insert(habit); // Insert habit in background
    }

    // Update an existing habit asynchronously
    public void update(Habit habit) {
        habitRepository.update(habit); // Update habit in background
    }

    // Delete a habit asynchronously
    public void delete(Habit habit) {
        habitRepository.delete(habit); // Delete habit in background
    }

    // Update the checked state of a habit (completed today)
    public void updateHabitChecked(int position, boolean isChecked) {
        habitRepository.updateCheckedState(position, isChecked); // Update checked state
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        habitRepository.shutdown();  // Ensure repository cleanup when ViewModel is cleared
    }
}
