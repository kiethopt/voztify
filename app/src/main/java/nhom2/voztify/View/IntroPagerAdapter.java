package nhom2.voztify.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import nhom2.voztify.R;

public class IntroPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] images = {R.drawable.intro2, R.drawable.intro6,R.drawable.intro4, R.drawable.intro1, R.drawable.intro3};

    public IntroPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.intro_item, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
