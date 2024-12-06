package data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Habit.class}, version = 3, exportSchema = false) // Incremented version to 3
public abstract class HabitDatabase extends RoomDatabase {

    // Singleton instance of HabitDatabase
    private static volatile HabitDatabase INSTANCE;

    // DAO access
    public abstract HabitDao habitDao();

    // Executor to handle database operations in the background
    public static final Executor databaseWriteExecutor = Executors.newFixedThreadPool(4); // Using a thread pool for background tasks

    // Migration from version 1 to 2: Add missing columns (only if they don't exist)
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Check if the column `isCompletedToday` exists before adding it
            if (!columnExists(database, "habits", "isCompletedToday")) {
                database.execSQL("ALTER TABLE habits ADD COLUMN isCompletedToday INTEGER NOT NULL DEFAULT 0");
            }

            // Check if the column `completionTimestamp` exists before adding it
            if (!columnExists(database, "habits", "completionTimestamp")) {
                database.execSQL("ALTER TABLE habits ADD COLUMN completionTimestamp INTEGER NOT NULL DEFAULT 0");
            }
        }

        // Helper method to check if a column exists in a table
        private boolean columnExists(SupportSQLiteDatabase database, String tableName, String columnName) {
            String query = "PRAGMA table_info(" + tableName + ")";
            try (Cursor cursor = database.query(query)) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String column = cursor.getString(cursor.getColumnIndex("name"));
                    if (column.equals(columnName)) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    // Migration from version 2 to 3 (if applicable)
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Example of further migration steps if necessary
        }
    };

    // Singleton pattern to ensure only one instance of HabitDatabase exists
    public static HabitDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HabitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    HabitDatabase.class, "habit_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add migrations here
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
