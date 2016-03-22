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
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;


import java.util.List;

/**
 * Created by Grishalive on 20.03.2016.
 */
public class ItemAdapter extends ArrayAdapter<String> {
    private int layout;
    private List<User> mObjects;
    public ItemAdapter(Context context, int resource, List<User> objects, List<String> names) {
        super(context, resource, names);
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.itemTextView);
            viewHolder.buttonAdd = (ImageView) convertView.findViewById(R.id.itemAddImage);
            viewHolder.buttonDelete = (ImageView) convertView.findViewById(R.id.itemDeleteImage);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfUsersActivity.addUserToDoctor(position);
                //v.setClickable(false);
                v.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Button1 was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                v.invalidate();
            }
        });

        mainViewholder.buttonDelete.setOnClickListener(new View.OnClickListener() {
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
