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
    private List<User> users;
    private int AllOrMy; // 0 if selected All, 1 if My
    private int doctorID;
    private Context context;

    public ItemAdapter(Context context, int resource, List<User> users, int AllOrMy, List<String> names, int doctorID) {
        super(context, resource, names);
        this.context = context;
        this.users = users;
        layout = resource;
        this.AllOrMy = AllOrMy;
        this.doctorID = doctorID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mainViewholder;
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
        if(AllOrMy == 0){
            if(users.get(position).getDoctorID() == doctorID){
                mainViewholder.buttonAdd.setVisibility(View.GONE);
                mainViewholder.buttonDelete.setVisibility(View.VISIBLE);
            } else if(users.get(position).getDoctorID() == 0){
                mainViewholder.buttonAdd.setVisibility(View.VISIBLE);
                mainViewholder.buttonDelete.setVisibility(View.GONE);
            } else {
                mainViewholder.buttonAdd.setVisibility(View.GONE);
                mainViewholder.buttonDelete.setVisibility(View.GONE);
            }
            mainViewholder.buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListOfUsersActivity.addUserToDoctor(position, context);
                    v.setVisibility(View.GONE);
                    v.invalidate();
                    mainViewholder.buttonDelete.setVisibility(View.VISIBLE);
                }
            });
            mainViewholder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListOfUsersActivity.deleteUserFromDoctor(position, context);
                    v.setVisibility(View.GONE);
                    v.invalidate();
                    mainViewholder.buttonAdd.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mainViewholder.buttonAdd.setVisibility(View.GONE);
            mainViewholder.buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListOfUsersActivity.addUserToDoctor(position, context);
                    v.setVisibility(View.GONE);
                    v.invalidate();
                }
            });
            mainViewholder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListOfUsersActivity.deleteUserFromDoctor(position, context);
                    v.setVisibility(View.GONE);
                    v.invalidate();
                }
            });
        }
        mainViewholder.title.setText(getItem(position));
        return convertView;
    }
}
