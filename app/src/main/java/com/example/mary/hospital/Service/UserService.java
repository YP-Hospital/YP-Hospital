package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Model.Role;

import java.util.List;

public interface UserService {
    Boolean addUserInDB(User user);
    Boolean updateUserInDB(User user);
    Boolean isUserExist(String name);
    User logIn(String name, String password);
    User getUserByLogin(String name);
    Role getUsersRole(String name);
    List<User> getAllUsers();
    List<User> getAllPatient();
    Boolean setDoctorToUser(User doctor, User patient);
}
