package com.qunar.qchat.utils;

import com.qunar.qchat.model.JsonResult;

/**
 * Author : mingxing.shao
 * Date : 16-4-12
 * email : mingxing.shao@qunar.com
 */
public class JsonResultUtils {

    public static JsonResult<?> success() {
        return success("");
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(JsonResult.SUCCESS, JsonResult.SUCCESS_CODE, "", data);
    }

    public static JsonResult<?> fail(int errcode, String errmsg) {
        return new JsonResult<>(JsonResult.FAIL, errcode, errmsg, "");
    }

    public static <T> JsonResult<T> fail(int errcode, String errmsg, T data) {
        return new JsonResult<>(JsonResult.FAIL, errcode, errmsg, data);
    }

    public static JsonResult<?> response(String response) {
        try {
            if (response == null) {
                return new JsonResult<>(JsonResult.FAIL, 1, "parse response fail", "");
            }
            JsonResult result = JacksonUtils.string2Obj(response, JsonResult.class);
            return result;
        } catch  (Exception e) {
            return new JsonResult<>(JsonResult.FAIL, 1, "parse response fail", "");
        }
    }
}
