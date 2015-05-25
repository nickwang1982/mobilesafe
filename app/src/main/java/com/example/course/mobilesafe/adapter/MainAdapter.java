package com.example.course.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.course.mobilesafe.R;

/**
 * Created by Guangyu on 2015/5/24.
 */
public class MainAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    // Put 9 icon pics into this list
    private static final int[] icons = {
            R.drawable.widget01,
            R.drawable.widget02,
            R.drawable.widget03,
            R.drawable.widget04,
            R.drawable.widget05,
            R.drawable.widget06,
            R.drawable.widget07,
            R.drawable.widget08,
            R.drawable.widget09
    };
    // The title for each icon
    private static final String[] names = {
            "Safe",
            "Security",
            "Apps",
            "Process",
            "Data",
            "AntiVirus",
            "Optimize",
            "Advance",
            "Settings"
    };

    public MainAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.main_item,  null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_main_item_name);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_main_item_icon);

        tv_name.setText(names[position]);
        iv_icon.setImageResource(icons[position]);
        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return names.length;
    }
}
