package car.guangqi.util;

/**
 * @author shepard.xia
 * @date 2017年04月18日
 * @description input useage
 */
public class ResponseWrap <T> {
    private String msg;
    private boolean success;
    private int code;
    private T data;

    public ResponseWrap() {}

    public ResponseWrap(String msg, boolean success) {
       this(msg,success,null,0);
    }

    public ResponseWrap(String msg, boolean success, T data,int code) {
        this.msg = msg;
        this.success = success;
        this.data = data;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
