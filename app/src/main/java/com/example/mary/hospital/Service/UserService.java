package com.example.mary.hospital.Service;

import android.content.Context;

import com.example.mary.hospital.Model.User;

import java.util.List;

public interface UserService {
    void addUserInDB(User user);
    Boolean isUserExist(String name);
    Boolean isCorrectPassword(String name, String password);
    User getUserByName(String name);
    List<User> getAllUsers();
    List<User> getAllPatient();
}
