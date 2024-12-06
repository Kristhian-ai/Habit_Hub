package data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "habits")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private long startDate;  // Habit start date
    private long endDate;    // Habit end date (goal date)
    private boolean isCompletedToday; // Whether habit is marked as completed today
    private int streakCount; // Number of consecutive days habit has been completed
    private String reminderTime; // Reminder time for the habit
    private boolean isDone; // Whether the habit is completed and should be moved to history
    private long completionTimestamp; // Timestamp when the habit was completed (useful for history tracking)

    // No-argument constructor for Room
    public Habit() {}

    // Constructor with parameters (updated to match AddEditHabitActivity constructor)
    @Ignore
    public Habit(String name, String description, long startDate, long endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCompletedToday = false;
        this.streakCount = 0;
        this.isDone = false; // Initially not done
        this.completionTimestamp = 0; // Initially, no completion timestamp
        this.reminderTime = ""; // Set reminderTime as an empty string for now (or use a default value)
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }

    public boolean isCompletedToday() { return isCompletedToday; }
    public void setCompletedToday(boolean isCompletedToday) { this.isCompletedToday = isCompletedToday; }

    public int getStreakCount() { return streakCount; }
    public void setStreakCount(int streakCount) { this.streakCount = streakCount; }

    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }

    public boolean isDone() { return isDone; }
    public void setDone(boolean isDone) { this.isDone = isDone; }

    public long getCompletionTimestamp() { return completionTimestamp; }
    public void setCompletionTimestamp(long completionTimestamp) { this.completionTimestamp = completionTimestamp; }

    // Method to mark the habit as completed and update the streak
    public void completeHabit() {
        this.isCompletedToday = true;
        this.streakCount++;
        this.isDone = true;  // Mark the habit as completed
        this.completionTimestamp = System.currentTimeMillis(); // Set completion date as current timestamp
    }

    // Add setCompleted method
    public void setCompleted(boolean completed) {
        this.isCompletedToday = completed;
        if (completed) {
            this.streakCount++;  // Increase streak count if the habit is completed
        } else {
            this.streakCount = 0; // Reset streak if the habit is not completed today
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habit habit = (Habit) obj;
        return id == habit.id &&
                startDate == habit.startDate &&
                endDate == habit.endDate &&
                isCompletedToday == habit.isCompletedToday &&
                streakCount == habit.streakCount &&
                isDone == habit.isDone &&
                completionTimestamp == habit.completionTimestamp &&
                Objects.equals(name, habit.name) &&
                Objects.equals(description, habit.description) &&
                Objects.equals(reminderTime, habit.reminderTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate, isCompletedToday, streakCount, reminderTime, isDone, completionTimestamp);
    }
}
