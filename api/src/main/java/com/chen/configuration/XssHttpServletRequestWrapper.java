package com.chen.configuration;

import com.chen.util.XssUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CHEN
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final HttpServletRequest orgRequest;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    // 重写这几个方法，并且处理xss
    @Override
    public String getParameter(String name) {
        return XssUtil.xssEncode(super.getParameter(XssUtil.xssEncode(name)));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        Map<String, String[]> newParameterMap = new HashMap<>(8);
        for(Map.Entry<String, String[]> entry : parameterMap.entrySet()){
            // 处理key的标签
            String newKey = XssUtil.xssEncode(entry.getKey());
            // 处理value的标签
            String[] params = entry.getValue();
            String[] newParams = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                newParams[i] = XssUtil.xssEncode(params[i]);
            }
            newParameterMap.put(newKey,newParams);
        }
        return newParameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(XssUtil.xssEncode(name));
        String[] newParams = new String[parameterValues.length];
        for (int i = 0; i < parameterValues.length; i++) {
            newParams[i] = XssUtil.xssEncode(parameterValues[i]);
        }
        return newParams;
    }

    // 如果将来想继续使用原始的request，可以通过这个方法获取
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }
}
