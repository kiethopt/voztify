package nhom2.voztify.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

import nhom2.voztify.R;
import nhom2.voztify.Model.TopRadioGenre;

public class TopRadioGenresAdapter extends RecyclerView.Adapter<TopRadioGenresAdapter.ViewHolder> {
    private List<TopRadioGenre> radioGenres;

    public TopRadioGenresAdapter(List<TopRadioGenre> radioGenres) {
        this.radioGenres = radioGenres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radio_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopRadioGenre radioGenre = radioGenres.get(position);
        holder.bind(radioGenre);
    }

    @Override
    public int getItemCount() {
        return radioGenres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRadioGenre;
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRadioGenre = itemView.findViewById(R.id.ivRadioGenre);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(TopRadioGenre radioGenre) {
            Picasso.get().load(radioGenre.getPictureUrl()).resize(120, 120).centerCrop().into(ivRadioGenre);
            titleTextView.setText(radioGenre.getTitle());
        }
    }
}

