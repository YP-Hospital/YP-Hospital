package com.example.mary.hospital.Service.Impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserServiceImpl implements UserService {
    private int booleanAnswer = 0;
    private int dataAnswer = 1;
    private final static String HASH_ALGORITHM = "SHA-256";
    private Context context;

    public UserServiceImpl (Context context) {
        this.context = context;
    }

    public Boolean addUserInDB (User user) {
        String query = user.getStringToInsertInServer();
        user.setPassword(passwordToHash(user.getPassword()));
        return useQuery(query);
    }

    public Boolean isUserExist(String login) {
        String result = "";
        try {
            result = getAnswerFromServerForQuery("select " + User.DATABASE_TABLE + " " + User.ID_COLUMN + " where " + User.LOGIN_COLUMN + " " + login).get(booleanAnswer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(result);
    }

    public User logIn(String login, String password) {
        List<String> answerFromServer;
        try {
            answerFromServer = getAnswerFromServerForQuery("select " + User.DATABASE_TABLE + " " + User.PASSWORD_COLUMN
                                                            + " " + User.ROLE_COLUMN + " where " + User.LOGIN_COLUMN + " " + login);
            if (answerFromServer.get(booleanAnswer).equals("false")) {
                ExtraResource.showErrorDialog(R.string.error_invalid_login, context);
                return null;
            } else {
                List<User> users = stringToUsers(answerFromServer.get(dataAnswer));
                if (!users.get(booleanAnswer).getPassword().equals(passwordToHash(password))) {
                    ExtraResource.showErrorDialog(R.string.error_incorrect_password, context);
                    return null;
                }
                return users.get(booleanAnswer);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<User> stringToUsers(String answerFromServer) {
        List<User> users = new ArrayList<>();
        List<String> words = new ArrayList<>(Arrays.asList(answerFromServer.split(" ")));
        Boolean isAllFields = words.get(booleanAnswer).equals("*");
        if (isAllFields) {
            for (int i = 2; i < words.size(); i++) {
                users.add(new User(Integer.valueOf(words.get(i++)), words.get(i++), words.get(i++), words.get(i++), Role.valueOf(words.get(i++)),
                                    Integer.valueOf(words.get(i++)), words.get(i++), Integer.valueOf(words.get(i++))));
            }
        } else {
            formListOfUsers(users, words);
        }
        return users;
    }
    /**Warning! GOVNOKOD*/
    private void formListOfUsers(List<User> users, List<String> words) {
        Boolean isLogin = false, isPassword = false, isName = false, isRole = false, isAge = false, isPhone = false, isDoctorID = false ;
        int i;
        for (i = 0; !words.get(i).equals("0."); i++) {
            switch (words.get(i)) {
                case User.LOGIN_COLUMN:
                    isLogin = true;
                    break;
                case User.PASSWORD_COLUMN:
                    isPassword = true;
                    break;
                case User.USER_NAME_COLUMN:
                    isName = true;
                    break;
                case User.ROLE_COLUMN:
                    isRole = true;
                    break;
                case User.AGE_COLUMN:
                    isAge = true;
                    break;
                case User.PHONE_COLUMN:
                    isPhone = true;
                    break;
                case User.DOCTOR_ID_COLUMN:
                    isDoctorID = true;
                    break;
            }
        }
        User user = null;
        for (;  i<words.size() ; i++) {
            if (words.get(i).matches("[0-9]+.")) {
                user = new User();
                continue;
            }
            if (isLogin) {
                user.setLogin(words.get(i++));
            }
            if (isPassword) {
                user.setPassword(words.get(i++));
            }
            if (isName) {
                user.setName(words.get(i++));
            }
            if (isRole) {
                user.setRole(Role.valueOf(words.get(i++)));
            }
            if (isAge) {
                user.setAge(Integer.valueOf(words.get(i++)));
            }
            if (isPhone) {
                user.setPhone(words.get(i++));
            }
            if (isDoctorID) {
                user.setPhone(words.get(i++));
            }
            users.add(user);
        }
    }

    public static String passwordToHash(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.reset();
            messageDigest.update(password.getBytes());

            byte result[] = messageDigest.digest();
            password = getHexadecimalValue(result);
        } catch (NoSuchAlgorithmException e1) {
            e1.getMessage();
        }
        return password;
    }

    private static String getHexadecimalValue(byte[] result) {
        StringBuilder buf = new StringBuilder();
        for (byte bytes : result) {
            buf.append(String.format("%02x", bytes & 0xff));
        }
        return buf.toString();
    }

    public User getUserByLogin(String login) {
        String result = "";
        User user = null;
        try {
            result = getAnswerFromServerForQuery("select " + User.DATABASE_TABLE + " * where " + User.LOGIN_COLUMN + " " + login).get(dataAnswer);
            user = stringToUsers(result).get(booleanAnswer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Role getUsersRole(String login) {
        Role role = null;
        try {
            String result = getAnswerFromServerForQuery("select " + User.DATABASE_TABLE + " " + User.ROLE_COLUMN
                                                        + " where " + User.LOGIN_COLUMN + " " + login).get(dataAnswer);
            role = Role.valueOf(result.split(" ")[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    public List<User> getAllUsers() {
        String query = "select " + User.DATABASE_TABLE + " *";
        return getUsers(query);
    }

    public List<User> getAllPatient() {
        String query = "select " + User.DATABASE_TABLE + " * where " + User.ROLE_COLUMN+ " " + Role.Patient;
        return getUsers(query);
    }
    /**DON"T RECOMMEND TO USE*/
    public Boolean updateUserInDB(User user) {
        String query = "update " + User.DATABASE_TABLE + " " + User.LOGIN_COLUMN + " " + User.PASSWORD_COLUMN + " " + User.USER_NAME_COLUMN
                        + " " + User.ROLE_COLUMN + " " + User.AGE_COLUMN + " " + User.PHONE_COLUMN + " " + User.DOCTOR_ID_COLUMN
                        + " " + user.getLogin() + " " + user.getPassword() + " " + user.getName() + " " + user.getRole() + " " + user.getAge()
                        + " " + user.getPhone() + " " + user.getDoctorID() + " " + user.getId();
        return useQuery(query);
    }

    public Boolean setDoctorToUser(User doctor, User patient) {
        String query = "update " + User.DATABASE_TABLE + " " + User.DOCTOR_ID_COLUMN + " " + doctor.getDoctorID() + " " + patient.getId();
        return useQuery(query);
    }

    @NonNull
    private Boolean useQuery(String query) {
        Boolean isSuccess = false;
        try {
            isSuccess = Boolean.parseBoolean(getAnswerFromServerForQuery(query).get(booleanAnswer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Nullable
    private List<User> getUsers(String query) {
        List<User> users = null;
        try {
            String result = getAnswerFromServerForQuery(query).get(dataAnswer);
            users = stringToUsers(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return users;
    }

    private List<String> getAnswerFromServerForQuery(String query) throws InterruptedException, ExecutionException {
        return new Connector(context).execute(query).get();
    }
}
