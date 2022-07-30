package com.xzz.basic.util;

import lombok.Data;

@Data
public class JsonResult {

    private Boolean success=true;
    private String msg;
    private Object resultObj;

    public static JsonResult me(){
        return new JsonResult();
    }

    public JsonResult setMsg(String msg) {
        this.success = false;
        this.msg = msg;
        return this;
    }

    public JsonResult setResultObj(Object resultObj) {
        this.resultObj = resultObj;
        return this;
    }
}
