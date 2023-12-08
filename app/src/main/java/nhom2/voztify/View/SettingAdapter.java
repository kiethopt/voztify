package nhom2.voztify.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nhom2.voztify.R;

public class SettingAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.setting_list_item, parent, false);
        }

        // Set data for each setting item (icon and title)
        ImageView settingIcon = convertView.findViewById(R.id.settingIcon);
        TextView settingTitle = convertView.findViewById(R.id.settingTitle);

        // You may need to customize the data based on the position
        // For example, switch (position) and set different icons and titles

        return convertView;
    }
}
