package com.example.mary.hospital.Model;


public class User {

    public static final String DATABASE_TABLE = "users";
    public static final String ID_COLUMN = "id";
    public static final String LOGIN_COLUMN = "login";
    public static final String USER_NAME_COLUMN = "name";
    public static final String PASSWORD_COLUMN = "password";
    public static final String PHONE_COLUMN = "phone";
    public static final String AGE_COLUMN = "age";
    public static final String ROLE_COLUMN = "role";
    public static final String DOCTOR_ID_COLUMN = "doctor_id";

    private Integer id;
    private String login;
    private String name;
    private String password;
    private Role role;
    private Integer age;
    private String phone;
    private Integer doctorID;

    public User(Integer id, String login, String password, String name, Role role, Integer age, String phone, Integer doctorID) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.role = role;
        this.doctorID = doctorID;
    }

    public User(String login, String password, String name,  Role role, Integer age, String phone) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.role = role;
        this.age = age;
        this.phone = phone;
    }

    public User(Integer id, String login, String password, String name, Role role, Integer age, String phone) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.role = role;
        this.doctorID = null;
    }

    public User(String login, String password, String name, Role role, Integer age, String phone, Integer doctorID) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.role = role;
        this.age = age;
        this.phone = phone;
        this.doctorID = doctorID;
    }

    public User() {
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStringToInsertInServer(){
        return "insert " + DATABASE_TABLE + " " + login
                + " " + password
                + " " + name
                + " " + role
                + " " + age
                + " " + phone
                + " " + doctorID;
    }

    @Override
    public String toString() {
        return "Name: " + name + '\n' +
                "Phone: " + phone + '\n' +
                "Age: " + age + '\n' +
                "Role: " + role;
    }
}
