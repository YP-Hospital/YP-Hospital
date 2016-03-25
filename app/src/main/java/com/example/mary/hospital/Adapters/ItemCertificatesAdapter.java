package com.example.mary.hospital.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.Action.ListOfUsersActivity;
import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;


import java.util.List;
import java.util.Map;

public class ItemCertificatesAdapter extends ArrayAdapter<String> {
    private int layout;
    private Map<String, Certificate> mObjects;
    public ItemCertificatesAdapter(Context context, int resource, Map<String, Certificate> map, List<String> names) {
        super(context, resource, names);
        mObjects = map;
        layout = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewholder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.itemTextView);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.title.setText(getItem(position));
        return convertView;
    }
}
