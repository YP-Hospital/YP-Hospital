package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class UserActivity extends AppCompatActivity {

    private UserService userService;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl(this);
        setContentView(R.layout.activity_user);
        User user = userService.getUserByLogin(getIntent().getStringExtra(ExtraResource.USER_LOGIN));
        TextView textView = (TextView)findViewById(R.id.userInfoTextView);
        textView.setText(user.toString());
    }

    public void repaintListView(int position){

        listView = (ListView)findViewById(R.id.listOfUsersListView);
        listView.setFocusable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListOfUsersActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                Intent IntentTemp = new Intent(view.getContext(), UserActivity.class);
                //IntentTemp.putExtra(ExtraResource.USER_LOGIN, users.get(position).getName());
                //IntentTemp.putExtra(ExtraResource.USER_ROLE, users.get(position).getRole().toString());
                startActivity(IntentTemp);
                //Toast.makeText(ListOfUsersActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        //listView.setAdapter(new ItemAdapter(this, R.layout.item_list_of_users, temp));
    }
}

