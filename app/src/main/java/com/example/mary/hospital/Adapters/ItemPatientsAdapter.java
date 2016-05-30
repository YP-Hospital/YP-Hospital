package com.example.mary.hospital.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mary.hospital.Action.DoctorInfoActivity;
import com.example.mary.hospital.Action.ListOfPatientsActivity;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;

import java.util.List;

/**
 * Created by Grishalive on 04.04.2016.
 */
public class ItemPatientsAdapter extends ArrayAdapter<String> {

    private List<User> users;
    private int layout;
    private int doctorID;
    private String fromWhichActivity;
    private Context context;

    public ItemPatientsAdapter(Context context, int resource, List<User> users, List<String> names, int doctorID, String fromWhichActivity) {
        super(context, resource, names);
        this.context = context;
        this.users = users;
        this.layout = resource;
        this.doctorID = doctorID;
        this.fromWhichActivity = fromWhichActivity;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mainViewholder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.itemTextView);
            viewHolder.buttonAdd = (ImageView) convertView.findViewById(R.id.itemAddImage);
            viewHolder.buttonDelete = (ImageView) convertView.findViewById(R.id.itemDeleteImage);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
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
                    if(fromWhichActivity.equals("DoctorInfoActivity"))
                        DoctorInfoActivity.addUserToDoctor(position, context);
                    else{
                        ListOfPatientsActivity.addUserToDoctor(position, context);
                    }
                    v.setVisibility(View.GONE);
                    v.invalidate();
                    mainViewholder.buttonDelete.setVisibility(View.VISIBLE);
                }
            });
            mainViewholder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fromWhichActivity.equals("DoctorInfoActivity"))
                        DoctorInfoActivity.deleteUserFromDoctor(position, context);
                    else{
                        ListOfPatientsActivity.deleteUserFromDoctor(position, context);
                    }
                    v.setVisibility(View.GONE);
                    v.invalidate();
                    mainViewholder.buttonAdd.setVisibility(View.VISIBLE);
                }
            });
        mainViewholder.title.setText(getItem(position));
        return convertView;
    }
}
