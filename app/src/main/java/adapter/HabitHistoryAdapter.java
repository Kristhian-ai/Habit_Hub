package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.HabitTracker.R;
import data.Habit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HabitHistoryAdapter extends ListAdapter<Habit, HabitHistoryAdapter.HabitViewHolder> {

    public HabitHistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view for the item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_card, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = getItem(position);

        // Bind habit data to the UI
        holder.habitName.setText(habit.getName());
        holder.habitDescription.setText(habit.getDescription());

        // Get the completion date and format it
        String formattedDate = formatDate(habit.getCompletionTimestamp());
        holder.completionDate.setText("Completed on: " + formattedDate);
    }

    // Format the completion date from timestamp (if the completion date is stored as a timestamp)
    private String formatDate(long timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date(timestamp);
            return sdf.format(date);
        } catch (Exception e) {
            return "Invalid Date"; // Return default message if there's an error
        }
    }

    // DiffUtil to compare the old and new list items
    public static final DiffUtil.ItemCallback<Habit> DIFF_CALLBACK = new DiffUtil.ItemCallback<Habit>() {
        @Override
        public boolean areItemsTheSame(@NonNull Habit oldItem, @NonNull Habit newItem) {
            return oldItem.getId() == newItem.getId(); // Compare by ID
        }

        @Override
        public boolean areContentsTheSame(@NonNull Habit oldItem, @NonNull Habit newItem) {
            // Compare the contents of the Habit objects
            return oldItem.equals(newItem); // Assuming Habit has a proper equals method implemented
        }
    };

    // ViewHolder class to hold references to the UI elements
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView habitName;
        TextView habitDescription;
        TextView completionDate;

        public HabitViewHolder(View itemView) {
            super(itemView);
            // Initialize views by finding them in the item layout
            habitName = itemView.findViewById(R.id.habitName);
            habitDescription = itemView.findViewById(R.id.habitDescription);
            completionDate = itemView.findViewById(R.id.completionDate);
        }
    }
}
