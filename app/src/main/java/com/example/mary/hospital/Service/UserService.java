package com.example.mary.hospital.Service;

import android.content.Context;

import com.example.mary.hospital.Model.User;

public interface UserService {
    void addUserInDB(User user);
    Boolean isUserExist(String name);
    Boolean isCorrectPassword(String name, String password);
}
