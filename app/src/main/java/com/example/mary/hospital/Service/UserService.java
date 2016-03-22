package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Model.Role;

import java.util.List;

public interface UserService {
    String addUserInDB(User user);
    Boolean updateUserInDB(User user);
    Boolean isUserExist(String name);
    User signIn(String name, String password);
    User getUserByLogin(String name);
    User getUserById(Integer id);
    Role getUsersRole(String login);
    List<User> getAllUsers();
    List<User> getAllPatients();
    List<User> getAllDoctors();
    Boolean setDoctorToUser(User doctor, User patient);
    Boolean setDoctorToUser(Integer id, User patient);
    Boolean deleteDoctorToUser(User patient);
}
