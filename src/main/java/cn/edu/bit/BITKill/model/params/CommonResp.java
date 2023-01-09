package cn.edu.bit.BITKill.model.params;

public class CommonResp<T> {

    private String type;

    private boolean success;

    private String message;

    private T content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public CommonResp() {
        this.type = "error";
        this.success = false;
        this.message = "An internal error occurred";
        this.content = null;
    }

    public CommonResp(String type, boolean success, String message, T content) {
        this.type = type;
        this.success = success;
        this.message = message;
        this.content = content;
    }
}
