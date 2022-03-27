package com.fongloo.context.utils;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * AntiSQLFilter 是一个 J2EE Web 应用程序过滤器，可保护 Web 组件免受 SQL 注入黑客攻击。<br>
 * 必须使用 web.xml 描述符进行配置。
 * <br><br>
 * 下面，要配置的过滤器初始化参数：
 * <br><br>
 * <b>logging</b> - a <i>true</i> 启用输出到 Servlet 上下文日志记录，以防 SQL 注入检测。
 * 默认是关闭的 <i>false</i>.
 * <br><br>
 * <b>behavior</b> - 在 SQL 注入检测的情况下存在三种可能的行为：
 * <li> protect : (default) 危险的 SQL 关键字被抑制了第二个字符 /
 * 危险的 SQL 定界符被替换为空格。
 * 之后请求按预期流动。
 * <li> throw: 抛出 ServletException - 中断请求流。
 * <li> forward: 请求被转发到特定资源。
 * <br><br>
 * <b>forwardTo</b> - 设置转发行为时要转发的资源。<br>
 * <P>
 * http://antisqlfilter.sourceforge.net/
 * </p>
 *
 * @author rbellia
 * @version 0.1
 */
public class AntiSqlFile {
    // 危险的关键字
    private static final String[] KEY_WORDS =
            {";", "\"", "\"", "/*", "*/", "--", "exec",
                    "select", "update", "delete", "insert",
                    "alter", "drop", "create", "shutdown"};

    /**
     * 扫描以上关键字 并替换为 " "
     *
     * @param parameterMap
     * @return
     */
    public static Map<String, String[]> getSafeParameterMap(Map<String, String[]> parameterMap) {
        HashMap<String, String[]> map = new HashMap<>(parameterMap.size());
        for (String key : map.keySet()) {
            String[] oldValues = parameterMap.get(key);
            map.put(key, getSafeValues(oldValues));
        }
        return map;
    }

    private static String[] getSafeValues(String[] oldValues) {
        // 克隆
        if (ArrayUtil.isNotEmpty(oldValues)) {
            String[] newValues = new String[oldValues.length];
            for (int i = 0; i < oldValues.length; i++) {
                newValues[i] = getSageValue(oldValues[i]);
            }
            return newValues;
        }
        return null;
    }

    private static String getSageValue(String oldValue) {
        if (StrUtil.isBlank(oldValue)) {
            return oldValue;
        }
        StringBuilder sb = new StringBuilder(oldValue);
        String lowerCase = oldValue.toLowerCase();
        for (String keyWorld : KEY_WORDS) {
            int x;
            while ((x = lowerCase.indexOf(keyWorld)) >= 0) {
                if (keyWorld.length() == 1) {
                    sb.replace(x, x + 1, " ");
                    lowerCase = sb.toString().toLowerCase();
                    continue;
                }
                sb.deleteCharAt(x + 1);
                lowerCase = sb.toString().toLowerCase();
            }
        }
        return sb.toString();
    }
}
