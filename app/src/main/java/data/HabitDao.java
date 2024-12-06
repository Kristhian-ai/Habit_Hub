package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {

    // Insert a habit into the database
    @Insert
    void insert(Habit habit);

    // Update a habit in the database
    @Update
    void update(Habit habit);

    // Delete a habit from the database
    @Delete
    void delete(Habit habit);

    // Update checked state (completion status) for a specific habit
    @Query("UPDATE habits SET isDone = :isChecked WHERE id = :habitId")
    void updateCheckedState(int habitId, boolean isChecked);

    // Search for habits by name (case-insensitive)
    @Query("SELECT * FROM habits WHERE name LIKE '%' || :query || '%' COLLATE NOCASE")
    LiveData<List<Habit>> searchHabits(String query);

    // Get all habits, ordered by name in ascending order
    @Query("SELECT * FROM habits ORDER BY name ASC")
    LiveData<List<Habit>> getAllHabits();

    // Get completed habits today
    @Query("SELECT * FROM habits WHERE isCompletedToday = 1")
    LiveData<List<Habit>> getCompletedHabitsToday();

    // Get all completed habits
    @Query("SELECT * FROM habits WHERE isDone = 1")
    LiveData<List<Habit>> getCompletedHabits(); // Query method

    // Get habits within a specific date range (start date to end date)
    @Query("SELECT * FROM habits WHERE startDate >= :startDate AND endDate <= :endDate ORDER BY startDate ASC")
    LiveData<List<Habit>> getHabitsByDateRange(long startDate, long endDate);

    // Count the total number of habits in the database
    @Query("SELECT COUNT(*) FROM habits")
    LiveData<Integer> countHabits();
}
