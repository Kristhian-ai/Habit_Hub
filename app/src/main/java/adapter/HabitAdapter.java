package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HabitTracker.R;

import data.Habit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HabitAdapter extends ListAdapter<Habit, HabitAdapter.HabitViewHolder> {

    private HabitAdapterListener listener;

    // Constructor with listener
    public HabitAdapter(HabitAdapterListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // No-argument constructor for use in HabitTrackingActivity without a listener
    public HabitAdapter() {
        super(DIFF_CALLBACK);
        this.listener = null; // Set listener to null if not provided
    }

    @Override
    public HabitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit_card, parent, false);

        // Log the views to check if they are correctly initialized
        TextView habitNameTextView = itemView.findViewById(R.id.habitName);
        TextView habitDescriptionTextView = itemView.findViewById(R.id.habitDescription);
        TextView completionDateTextView = itemView.findViewById(R.id.completionDate);
        ProgressBar progressBar = itemView.findViewById(R.id.habitProgress);
        CheckBox checkBoxDone = itemView.findViewById(R.id.habitCheckBox);

        if (habitNameTextView == null || habitDescriptionTextView == null || completionDateTextView == null || progressBar == null || checkBoxDone == null) {
            throw new NullPointerException("One or more views in item_habit_card.xml are null");
        }

        return new HabitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HabitViewHolder holder, int position) {
        Habit habit = getItem(position);
        holder.bind(habit);
    }

    // ViewHolder to bind habit data to the UI
    public class HabitViewHolder extends RecyclerView.ViewHolder {
        private TextView habitNameTextView;
        private TextView habitDescriptionTextView;
        private TextView completionDateTextView;
        private ProgressBar progressBar;
        private CheckBox checkBoxDone;

        public HabitViewHolder(View itemView) {
            super(itemView);
            habitNameTextView = itemView.findViewById(R.id.habitName);
            habitDescriptionTextView = itemView.findViewById(R.id.habitDescription);
            completionDateTextView = itemView.findViewById(R.id.completionDate);
            progressBar = itemView.findViewById(R.id.habitProgress);
            checkBoxDone = itemView.findViewById(R.id.habitCheckBox);

            // Set up checkbox listener
            checkBoxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onHabitChecked(getAdapterPosition(), isChecked);
                }
            });
        }

        // Bind habit data to the UI
        public void bind(Habit habit) {
            habitNameTextView.setText(habit.getName());
            habitDescriptionTextView.setText(habit.getDescription());

            // Ensure that completionDateTextView is not null and set visibility based on habit status
            if (completionDateTextView != null) {
                if (habit.isDone()) {
                    String formattedDate = formatDate(habit.getCompletionTimestamp());
                    completionDateTextView.setText("Completed on: " + formattedDate);
                    completionDateTextView.setVisibility(View.VISIBLE); // Make sure it's visible if completed
                } else {
                    completionDateTextView.setVisibility(View.GONE); // Hide if habit is not completed
                }
            }

            // Calculate and set progress for the habit
            if (habit.isDone()) {
                progressBar.setProgress(100);  // Mark as 100% completed
            } else {
                long currentDate = System.currentTimeMillis();
                long startDate = habit.getStartDate();
                long endDate = habit.getEndDate();

                // Prevent division by zero in case endDate is before startDate
                if (endDate > startDate) {
                    long totalDuration = endDate - startDate;
                    long elapsedTime = currentDate - startDate;

                    int progress = (int) ((elapsedTime * 100) / totalDuration);
                    progress = Math.max(0, Math.min(100, progress)); // Ensure the progress is between 0 and 100

                    progressBar.setProgress(progress);
                } else {
                    progressBar.setProgress(0); // If dates are invalid, set progress to 0
                }
            }

            // Ensure checkbox reflects the habit's current done status
            checkBoxDone.setChecked(habit.isDone());
        }

        // Helper method to format completion timestamp
        private String formatDate(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(timestamp);
            return sdf.format(date); // Convert timestamp to formatted date string
        }
    }

    // Interface to handle checkbox checked events
    public interface HabitAdapterListener {
        void onHabitChecked(int position, boolean isChecked);
    }

    // DiffUtil callback to optimize RecyclerView updates
    private static final DiffUtil.ItemCallback<Habit> DIFF_CALLBACK = new DiffUtil.ItemCallback<Habit>() {
        @Override
        public boolean areItemsTheSame(Habit oldItem, Habit newItem) {
            return oldItem.getId() == newItem.getId(); // Compare by ID
        }

        @Override
        public boolean areContentsTheSame(Habit oldItem, Habit newItem) {
            return oldItem.equals(newItem); // Compare the contents of the Habit objects
        }
    };
}
