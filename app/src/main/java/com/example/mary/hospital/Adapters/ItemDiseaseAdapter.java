package com.example.mary.hospital.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.Action.ListOfUsersActivity;
import com.example.mary.hospital.R;


import java.util.List;

/**
 * Created by Grishalive on 20.03.2016.
 */
public class ItemDiseaseAdapter extends ArrayAdapter<String> {
    private int layout;
    private List<String> mObjects;
    public ItemDiseaseAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mObjects = objects;
        layout = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewholder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.itemDiseaseTextView);
            viewHolder.buttonAdd = (ImageView) convertView.findViewById(R.id.itemDiseaseEditImageView);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mainViewholder.title.setText(getItem(position));
        if(mObjects.isEmpty()) {
            mainViewholder.title.setText("There is no disease");
            mainViewholder.buttonAdd.setVisibility(View.GONE);
        }
        return convertView;
    }
}
