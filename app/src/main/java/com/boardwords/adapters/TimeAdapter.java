package com.boardwords.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boardwords.R;
import com.boardwords.modal.TimeOption;

import java.util.List;


public class TimeAdapter extends ArrayAdapter<TimeOption> {

    public TimeAdapter(Context context, List<TimeOption> timeOptions) {
        super(context, 0, timeOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_select_time, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        TimeOption option = getItem(position);

        vh.time.setText(option.time);

        return convertView;
    }

    private static final class ViewHolder {
        TextView time;

        public ViewHolder(View v) {
            time = (TextView) v.findViewById(R.id.time);
        }
    }
}
