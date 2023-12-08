package nhom2.voztify.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nhom2.voztify.Controller.DeezerRadio;
import nhom2.voztify.R;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.RadioViewHolder> {
    private Context context;
    private List<DeezerRadio> radioList;

    public RadioAdapter(Context context, List<DeezerRadio> radioList) {
        this.context = context;
        this.radioList = radioList;
    }

    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.radio_item, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioViewHolder holder, int position) {
        DeezerRadio radio = radioList.get(position);

        // Hiển thị thông tin Top Radio
        holder.tvTopRadio.setText(radio.getTitle());

        // Sử dụng Picasso hoặc Glide để tải hình ảnh từ URL
        Picasso.get().load(radio.getPicture()).into(holder.imgTopRadio);
    }

    @Override
    public int getItemCount() {
        return radioList.size();
    }

    static class RadioViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopRadio;
        ImageView imgTopRadio;

        RadioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopRadio = itemView.findViewById(R.id.tvTopRadio);
            imgTopRadio = itemView.findViewById(R.id.imgTopRadio);
        }
    }
}
