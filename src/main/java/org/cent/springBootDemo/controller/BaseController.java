package org.cent.springBootDemo.controller;

import org.cent.springBootDemo.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础Controller
 * Created by cent on 2016/9/21.
 */
public class BaseController {

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    protected Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();

        for (String key : request.getParameterMap().keySet()) {
            params.put(key, StringUtil.join(request.getParameterValues(key), ","));
        }

        return params;
    }
}
