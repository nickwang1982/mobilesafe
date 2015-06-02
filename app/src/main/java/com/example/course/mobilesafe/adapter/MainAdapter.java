package com.example.course.mobilesafe.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.course.mobilesafe.R;
import com.example.course.mobilesafe.domain.ItemBeanInfo;

import java.util.ArrayList;

/**
 * Adapter for Main UI items
 */
public class MainAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private String newName;
    public ArrayList<ItemBeanInfo> mList = new ArrayList<ItemBeanInfo>();


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
    public MainAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String[] names = context.getResources().getStringArray(R.array.ItemName);

        for (int i = 0; i < 9; i++) {
            ItemBeanInfo itemBean;
            itemBean = new ItemBeanInfo(i, names[i], icons[i]);
            mList.add(itemBean);
        }

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        newName = sp.getString("newname", "");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // solution 1:
//        View view = inflater.inflate(R.layout.main_item,  null);
//        TextView tv_name = (TextView) view.findViewById(R.id.tv_main_item_name);
//        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_main_item_icon);
//
//        tv_name.setText(mList.get(position).getItemTitle());
//        iv_icon.setImageResource(mList.get(position).getItemRes());
//        return view;

        // Solution 2: use convertView
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.main_item,null);
//        }
//        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_main_item_name);
//        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_main_item_icon);
//
//        tv_name.setText(mList.get(position).getItemTitle());
//        iv_icon.setImageResource(mList.get(position).getItemRes());
//        return convertView;

        // Solution 3: use viewHolder
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.main_item, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_main_item_name);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_main_item_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mList.get(position).getItemTitle());
        viewHolder.imageView.setImageResource(mList.get(position).getItemRes());


        // For mobile safe item the title can be custormize by user.
        if (position == 0) {
            // See whether user has already config the tile
            if (!TextUtils.isEmpty(newName)) {
                viewHolder.textView.setText(newName);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public TextView textView;
        public ImageView imageView;
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
        return mList.size();
    }
}
