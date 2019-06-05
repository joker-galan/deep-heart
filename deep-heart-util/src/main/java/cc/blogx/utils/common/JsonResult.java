package cc.blogx.utils.common;

import cc.blogx.utils.enums.GlobalEnum;

import java.io.Serializable;
import java.util.List;

public class JsonResult<T> implements Serializable {

    private String code;
    private String msg;
    private T obj;
    private List<T> array;
    private Exception e;

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

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<T> getArray() {
        return array;
    }

    public void setArray(List<T> array) {
        this.array = array;
    }

    public void setException(Exception e) {
        this.msg = e.getMessage();
        this.code = "0";
    }

    public static JsonResult getSuccess() {
        JsonResult result = new JsonResult();
        result.setCode(GlobalEnum.SUCCESS.getCode());
        result.setMsg(GlobalEnum.SUCCESS.getMsg());
        return result;
    }

    public static JsonResult getFailed() {
        JsonResult result = new JsonResult();
        result.setCode(GlobalEnum.ERROR.getCode());
        result.setMsg(GlobalEnum.ERROR.getMsg());
        return result;
    }
}
