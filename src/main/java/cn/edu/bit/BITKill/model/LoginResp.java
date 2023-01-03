package cn.edu.bit.BITKill.model;

public class LoginResp {

    private final String type = "login";

    private boolean success;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResp(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
