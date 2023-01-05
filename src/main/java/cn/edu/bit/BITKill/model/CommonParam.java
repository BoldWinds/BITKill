package cn.edu.bit.BITKill.model;

public class CommonParam<T> {

    private String type;

    private T content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public CommonParam() {
        this.type = "error";
        this.content = null;
    }

    public CommonParam(String type, T content) {
        this.type = type;
        this.content = content;
    }
}
