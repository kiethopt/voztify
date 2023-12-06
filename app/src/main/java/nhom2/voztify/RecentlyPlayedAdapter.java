package nhom2.voztify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import nhom2.voztify.Class.History;

public class RecentlyPlayedAdapter extends RecyclerView.Adapter<RecentlyPlayedAdapter.ViewHolder> {

    private Context context;
    private List<History> historyList;

    public RecentlyPlayedAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_played_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);

        holder.titleTextView.setText(history.getTitle());
        holder.artistTextView.setText(history.getArtist());
        holder.timestampTextView.setText(formatTimestamp(history.getTimestamp()));
        // Load the image into the ImageView using Picasso (you need to add the Picasso library to your dependencies)

        String imgUrl =  "https://e-cdns-images.dzcdn.net/images/cover" + "/" + history.getImageUrl() + "/120x120-000000-80-0-0.jpg";
        Picasso.get().load(imgUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView artistTextView;
        public TextView timestampTextView;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            imageView = itemView.findViewById(R.id.img_recentlyPlayed);
        }
    }

    private String formatTimestamp(long timestamp) {
        // If the timestamp is in seconds, convert it to milliseconds
        if (String.valueOf(timestamp).length() == 10) {
            timestamp *= 1000;
        }

        // Create a Date object from the timestamp
        Date date = new Date(timestamp);

        // Format the Date object using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
}