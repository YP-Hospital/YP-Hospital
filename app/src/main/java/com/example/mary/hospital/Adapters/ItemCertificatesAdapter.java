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
            viewHolder.buttonShow = (Button) convertView.findViewById(R.id.button4);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfUsersActivity.deleteUserFromDoctor(position);
                //v.setClickable(false);
                v.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Button2 was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                v.invalidate();
            }
        });
        mainViewholder.title.setText(getItem(position));
        return convertView;
    }
}
