package com.fongloo.context;

import com.fongloo.utils.NumberHelper;
import com.fongloo.utils.StrHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类用于 JWT
 * 用于获取和存储 BaseContextConstants中的属性
 * 获取当前域中的 用户id appid 用户昵称
 * 注意： appid 通过token解析，  用户id 和 用户昵称必须在前端 通过请求头的方法传入。 否则这里无法获取
 */
public class BaseContextHandler {
    // 存放 token解析的属性 => BaseContextConstants
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(String key, Long value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? "0" : String.valueOf(value));
    }

    public static void set(String key, String value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? "" : value);
    }

    public static void set(String key, Boolean value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? "false" : value.toString());
    }

    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setThreadLocal(Map<String, String> treadLocalMap) {
        THREAD_LOCAL.set(treadLocalMap);
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.getOrDefault(key, "");
    }

    public static void removeThreadLocal() {
        if (THREAD_LOCAL != null) {
            THREAD_LOCAL.remove();
        }
    }

    /**
     * 获取账号ID
     *
     * @return userId
     */
    public static Long getUserId() {
        Object value = get(BaseContextConstants.JWT_KEY_USER_ID);
        return NumberHelper.longValueOf0(value);
    }

    /**
     * 设置账号Id
     */
    public static void setUserId(Long userId) {
        set(BaseContextConstants.JWT_KEY_USER_ID, userId);
    }

    public static void setUserId(String userId) {
        setUserId(NumberHelper.longValueOf0(userId));
    }

    /**
     * 账号表中的name
     *
     * @return
     */
    public static String getAccount() {
        Object value = get(BaseContextConstants.JWT_KEY_ACCOUNT);
        return returnObjectValue(value);
    }

    /**
     * 账号表中的name
     *
     * @param name
     */
    public static void setAccount(String name) {
        set(BaseContextConstants.JWT_KEY_ACCOUNT, name);
    }


    /**
     * 登录的账号
     *
     * @return
     */
    public static String getName() {
        Object value = get(BaseContextConstants.JWT_KEY_NAME);
        return returnObjectValue(value);
    }

    /**
     * 登录的账号
     *
     * @param account
     */
    public static void setName(String account) {
        set(BaseContextConstants.JWT_KEY_NAME, account);
    }

    /**
     * 获取用户token
     *
     * @return
     */
    public static String getToken() {
        Object value = get(BaseContextConstants.TOKEN_NAME);
        return StrHelper.getObjectValue(value);
    }

    public static void setToken(String token) {
        set(BaseContextConstants.TOKEN_NAME, token);
    }

    public static Long getOrgId() {
        Object value = get(BaseContextConstants.JWT_KEY_ORG_ID);
        return NumberHelper.longValueOf0(value);
    }

    public static void setOrgId(String val) {
        set(BaseContextConstants.JWT_KEY_ORG_ID, val);
    }


    public static Long getStationId() {
        Object value = get(BaseContextConstants.JWT_KEY_STATION_ID);
        return NumberHelper.longValueOf0(value);
    }

    public static void setStationId(String val) {
        set(BaseContextConstants.JWT_KEY_STATION_ID, val);
    }

    public static String getDatabase() {
        Object value = get(BaseContextConstants.DATABASE_NAME);
        return StrHelper.getObjectValue(value);
    }

    public static void setDatabase(String val) {
        set(BaseContextConstants.DATABASE_NAME, val);
    }


    private static String returnObjectValue(Object value) {
        return value == null ? "" : value.toString();
    }


}
