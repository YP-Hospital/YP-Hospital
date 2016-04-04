package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Model.Role;

import java.util.List;

public interface UserService {
    String insertUserInDB(User user);
    Boolean updateUserInDB(User user);
    Boolean isUserExist(String name);
    Boolean deleteUserFromDB(Integer id);
    User signIn(String name, String password);
    User getUserByLogin(String name);
    User getUserById(Integer id);
    Role getUsersRole(String login);
    List<User> getAllUsers();
    List<User> getAllPatients();
    List<User> getAllDoctors();
    List<User> getPatientsByDoctor(User doctor);
    List<User> getPatientsByDoctor(Integer doctorID);
    List<String> getNamesOfPatientsByDoctor(Integer doctorID);
    List<String> getNamesOfPatientsByDoctor(User doctor);
    Boolean setDoctorToUser(User doctor, User patient);
    Boolean setDoctorToUser(Integer idDoctor, User patient);
    Boolean setDoctorToUser(String loginDoctor, User patient);
    Boolean setDoctorToUser(Integer idDoctor, Integer idPatient);
    Boolean deleteDoctorToUser(User patient);
}
