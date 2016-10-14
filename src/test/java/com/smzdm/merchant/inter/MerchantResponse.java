package com.smzdm.merchant.inter;

/**
 * Created by zhengwenzhu on 16/10/11.
 */
public class MerchantResponse {
    private Integer error_code;
    private String msg_code;
    private Object data;

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(String msg_code) {
        this.msg_code = msg_code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String template = "MerchantResponse{'error_code':'%s', 'msg_code':'%s', 'data:':'%s'";
        return String.format(template, error_code, msg_code, String.valueOf(data));
    }
}
