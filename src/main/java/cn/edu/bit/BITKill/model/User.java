package cn.edu.bit.BITKill.model;

public class User {
    private String username;

    private String password;

    private String sex;

    private String age;

    public User() {
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //使用RegisterParam构造
    public User(RegisterParam registerParam) {
        this.username = registerParam.getName();
        this.password = registerParam.getPassword();
        this.age = registerParam.getAge();
        this.sex = registerParam.getSex();
    }
    public User(String username, String password, String sex, String age) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
