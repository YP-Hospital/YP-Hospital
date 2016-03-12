package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Model.Role;

import java.util.List;

public interface UserService {
    void addUserInDB(User user);
    Boolean isUserExist(String name);
    Boolean isCorrectPassword(String name, String password);
    User getUserByName(String name);
    Role getUsersRole(String name);
    List<User> getAllUsers();
    List<User> getAllPatient();
    void deleteTable();
}
