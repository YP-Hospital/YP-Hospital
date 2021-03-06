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
    private String separator = "]\\[";
    private String separatorForSending = "][";
    private final static String HASH_ALGORITHM = "SHA-256";
    private Context context;

    public UserServiceImpl (Context context) {
        this.context = context;
    }

    public String insertUserInDB(User user) {
        if(user.getDoctorID() == null)
            user.setDoctorID(0);
        user.setPassword(passwordToHash(user.getPassword()));
        String query = user.getStringToInsertInServer();
        String privateKey = "";
        try {
            privateKey = getAnswerFromServerForQuery(query).get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public Boolean deleteUserFromDB(Integer id) {
        String query = "delete" +separatorForSending + User.DATABASE_TABLE + separatorForSending
                        + id;
        return useQuery(query);
    }

    public Boolean isUserExist(String login) {
        String result = "";
        try {
            result = getAnswerFromServerForQuery("select" + separatorForSending + User.DATABASE_TABLE + separatorForSending
                    + User.ID_COLUMN + separatorForSending + "where" + separatorForSending 
                    + User.LOGIN_COLUMN + separatorForSending + login).get(booleanAnswer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(result);
    }

    public User signIn(String login, String password) {
        List<String> answerFromServer;
        try {
            answerFromServer = getAnswerFromServerForQuery("select" + separatorForSending + User.DATABASE_TABLE
                    + separatorForSending + "*" + separatorForSending + "where" + separatorForSending
                    + User.LOGIN_COLUMN + separatorForSending + login);
            if (answerFromServer.get(booleanAnswer).equals("false")) {
                ExtraResource.showErrorDialog(R.string.error_invalid_login, context);
                return null;
            } else {
                List<User> users = stringToUsers(answerFromServer.get(dataAnswer));
                if (!users.get(0).getPassword().equals(passwordToHash(password))) {
                    ExtraResource.showErrorDialog(R.string.error_incorrect_password, context);
                    return null;
                }
                ExtraResource.setCurrentUser(users.get(0));
                return users.get(0);
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
        try {
            List<String> words = new ArrayList<>(Arrays.asList(answerFromServer.split(separator)));
            Boolean isAllFields = words.get(booleanAnswer).equals("*");
            if (isAllFields) {
                for (int i = 2; i < words.size(); i++) {
                    users.add(new User(Integer.valueOf(words.get(i++)), words.get(i++), words.get(i++), words.get(i++), Role.valueOf(words.get(i++)),
                                        Integer.valueOf(words.get(i++)), words.get(i++), Integer.valueOf(words.get(i++))));
                }
            } else {
                formListOfUsers(users, words);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    /**Warning! GOVNOKOD*/
    private void formListOfUsers(List<User> users, List<String> words) {
        Boolean isID = false, isLogin = false, isPassword = false, isName = false, isRole = false, isAge = false, isPhone = false, isDoctorID = false ;
        int i;
        for (i = 0; !words.get(i).equals("0."); i++) {
            switch (words.get(i)) {
                case User.ID_COLUMN:
                    isID = true;
                    break;
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
            if (isID) {
                user.setId(Integer.valueOf(words.get(i++)));
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
                user.setDoctorID(Integer.valueOf(words.get(i++)));
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
            result = getAnswerFromServerForQuery("select" + separatorForSending + User.DATABASE_TABLE
                    + separatorForSending + "*" + separatorForSending + "where" + separatorForSending
                    + User.LOGIN_COLUMN + separatorForSending + login).get(dataAnswer);
            user = stringToUsers(result).get(booleanAnswer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserById(Integer id) {
        String result = "";
        User user = null;
        try {
            result = getAnswerFromServerForQuery("select" + separatorForSending + User.DATABASE_TABLE
                                                + separatorForSending + "*" + separatorForSending
                                                + "where" + separatorForSending
                                                + User.ID_COLUMN + separatorForSending + id).get(dataAnswer);
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
            String result = getAnswerFromServerForQuery("select" + separatorForSending + User.DATABASE_TABLE 
                                                        + separatorForSending + User.ROLE_COLUMN 
                                                        + separatorForSending + "where" + separatorForSending 
                                                        + User.LOGIN_COLUMN + separatorForSending + login).get(dataAnswer);
            role = Role.valueOf(result.split(separator)[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    public List<User> getAllUsers() {
        String query = "select" + separatorForSending + User.DATABASE_TABLE + separatorForSending + "*";
        return getUsers(query);
    }

    public List<User> getAllPatients() {
        String query = "select" + separatorForSending + User.DATABASE_TABLE + separatorForSending 
                        + "*" + separatorForSending + "where" + separatorForSending
                        + User.ROLE_COLUMN + separatorForSending + Role.Patient;
        return getUsers(query);
    }

    public List<User> getAllDoctors() {
        String query = "select" + separatorForSending + User.DATABASE_TABLE + separatorForSending 
                        + "*" + separatorForSending + "where" + separatorForSending
                        + User.ROLE_COLUMN + separatorForSending + Role.Doctor;
        return getUsers(query);
    }

    public List<User> getPatientsByDoctor(User doctor) {
        return getPatientsByDoctor(doctor.getId());
    }

    public List<User> getPatientsByDoctor(Integer doctorID) {
        String query = "select" + separatorForSending + User.DATABASE_TABLE + separatorForSending
                + "*" + separatorForSending + "where" + separatorForSending
                + User.DOCTOR_ID_COLUMN + separatorForSending + doctorID;
        return getUsers(query);
    }

    public List<String> getNamesOfPatientsByDoctor(User doctor) {
        return getNamesOfPatientsByDoctor(doctor.getId());
    }

    public List<String> getNamesOfPatientsByDoctor(Integer doctorID) {
        String query = "select" + separatorForSending + User.DATABASE_TABLE + separatorForSending
                + User.USER_NAME_COLUMN + separatorForSending + "where" + separatorForSending + User.DOCTOR_ID_COLUMN
                + separatorForSending + doctorID;
        List<String> allNames = new ArrayList<>();
        try {
            String answer = getAnswerFromServerForQuery(query).get(dataAnswer);
            List<String> words = new ArrayList<>(Arrays.asList(answer.split(separator)));
            for (String word : words) {
                if (word.matches("[0-9]+.") || word.equals(User.USER_NAME_COLUMN) || word.isEmpty()) {
                    continue;
                }
                allNames.add(word);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return allNames;
    }

    /**DON"T RECOMMEND TO USE*/
    public Boolean updateUserInDB(User user) {
        String query = "update" + separatorForSending + User.DATABASE_TABLE + separatorForSending
                + User.LOGIN_COLUMN + separatorForSending + User.PASSWORD_COLUMN + separatorForSending + User.USER_NAME_COLUMN
                + separatorForSending + User.ROLE_COLUMN + separatorForSending + User.AGE_COLUMN + separatorForSending 
                + User.PHONE_COLUMN + separatorForSending + User.DOCTOR_ID_COLUMN + separatorForSending 
                + user.getLogin() + separatorForSending + user.getPassword() + separatorForSending + user.getName() 
                + separatorForSending + user.getRole() + separatorForSending + user.getAge() + separatorForSending 
                + user.getPhone() + separatorForSending + user.getDoctorID() + separatorForSending + user.getId();
        return useQuery(query);
    }

    public Boolean setDoctorToUser(User doctor, User patient) {
        return setDoctorToUser(doctor.getId(), patient);
    }

    public Boolean setDoctorToUser(Integer id, User patient) {
        return setDoctorToUser(id, patient.getId());
    }

    public Boolean setDoctorToUser(String login, User patient) {
        User user = getUserByLogin(login);
        return setDoctorToUser(user, patient);
    }

    public Boolean setDoctorToUser(Integer idDoctor, Integer idPatient) {
        String query = "update" + separatorForSending + User.DATABASE_TABLE + separatorForSending
                + User.DOCTOR_ID_COLUMN + separatorForSending + idDoctor + separatorForSending + idPatient;
        return useQuery(query);
    }

    public Boolean deleteDoctorToUser(User patient) {
        User doc = new User();
        doc.setId(0);
        return setDoctorToUser(doc, patient);
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
