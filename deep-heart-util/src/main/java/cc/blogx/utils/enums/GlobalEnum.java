package cc.blogx.utils.enums;

public enum GlobalEnum {

    SUCCESS("1", "请求成功！"),
    ERROR("0", "请求失败！");

    private String code;
    private String msg;

    GlobalEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
