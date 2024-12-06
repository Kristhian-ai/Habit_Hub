package data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HabitRepository {

    private HabitDao habitDao;
    private LiveData<List<Habit>> allHabits;
    private ExecutorService executorService;

    // Constructor to initialize HabitDao, LiveData, and Executor
    public HabitRepository(Application application) {
        HabitDatabase db = HabitDatabase.getDatabase(application);
        habitDao = db.habitDao();
        allHabits = habitDao.getAllHabits();
        executorService = (ExecutorService) HabitDatabase.databaseWriteExecutor; // Using databaseWriteExecutor from HabitDatabase
    }

    // Get all habits as LiveData
    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    // Search for habits by query
    public LiveData<List<Habit>> searchHabits(String query) {
        return habitDao.searchHabits(query);
    }

    // Fetch completed habits today
    public LiveData<List<Habit>> getCompletedHabitsToday() {
        return habitDao.getCompletedHabitsToday();
    }

    // Get habits by date range (start date to end date)
    public LiveData<List<Habit>> getHabitsByDateRange(long startDate, long endDate) {
        return habitDao.getHabitsByDateRange(startDate, endDate);
    }

    // Count the total number of habits in the database
    public LiveData<Integer> countHabits() {
        return habitDao.countHabits();
    }

    // Insert a new habit into the database
    public void insert(Habit habit) {
        executorService.execute(() -> habitDao.insert(habit)); // Insert on a background thread
    }

    // Update an existing habit in the database
    public void update(Habit habit) {
        executorService.execute(() -> habitDao.update(habit)); // Update on a background thread
    }

    // Delete a habit from the database
    public void delete(Habit habit) {
        executorService.execute(() -> habitDao.delete(habit)); // Delete on a background thread
    }

    // Update checked state (completion status) for a specific habit
    public void updateCheckedState(int habitId, boolean isChecked) {
        executorService.execute(() -> habitDao.updateCheckedState(habitId, isChecked));
    }

    // Close the executor when it's no longer needed
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
