package com.fongloo.wrapper;

import cn.hutool.core.util.StrUtil;
import com.fongloo.utils.XssUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.List;
import java.util.Map;


/**
 * 根据继承下来的请求所过滤
 */
@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper {

    // 需要忽略的值
    private List<String> ignoreParamValueList;

    public XssRequestWrapper(HttpServletRequest request, List<String> ignorePathList) {
        super(request);
        this.ignoreParamValueList = ignorePathList;
    }

    /**
     * 过滤从客户端传递过来的参数值
     *
     * @param paramString 参数值
     * @return
     */
    @Override
    public String[] getParameterValues(String paramStringValue) {
        String[] parameterValues = super.getParameterValues(paramStringValue);
        String[] arr = new String[parameterValues.length];
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String clean = XssUtils.xssClean(parameterValues[i], this.ignoreParamValueList);
            arr[i] = clean;
        }
        return arr;
    }

    /**
     * 过滤从客户端传递过来参数名称
     * 例如 xxx.com/paramString = "value"
     *
     * @param paramString 参数名
     * @return
     */
    @Override
    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return XssUtils.xssClean(paramString, this.ignoreParamValueList);
    }

    /**
     * 过滤Map类型的参数
     *
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        for (Map.Entry<String, String[]> key : parameterMap.entrySet()) {
            log.debug("{ } :", key.getKey());
            String[] values = key.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = XssUtils.xssClean(values[i],this.ignoreParamValueList);
            }
        }
        return parameterMap;
    }

    /**
     * 过滤请求头
     */
    @Override
    public String getHeader(String headerValue) {
        String header = super.getHeader(headerValue);
        if (StrUtil.isBlank(header)) {
            return null;
        }
        return XssUtils.xssClean(header, this.ignoreParamValueList);
    }
}
