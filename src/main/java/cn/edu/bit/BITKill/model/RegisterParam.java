package cn.edu.bit.BITKill.model;

public class RegisterParam {

    private String name;

    private String password;

    private String sex;

    private String age;

    public RegisterParam() {
    }

    public RegisterParam(String name, String password, String sex, String age) {
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
