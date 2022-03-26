package com.fongloo.context;

/**
 * 常量工具类
 * 用于JWT
 */
public class BaseContextConstants {
    /**
     * Token
     */
    public static final String TOKEN_NAME = "token";

    /**
     * 用户ID
     */
    public static final String JWT_KEY_USER_ID = "userid";

    /**
     * 名称
     */
    public static final String JWT_KEY_NAME = "name";

    /**
     * 账户
     */
    public static final String JWT_KEY_ACCOUNT = "account";

    /**
     * 组织ID
     */
    public static final String JWT_KEY_ORG_ID = "orgid";

    /**
     * 岗位ID
     */
    public static final String JWT_KEY_STATION_ID = "stationid";

    /**
     * 动态数据库名前缀, 每个项目配置死的
     */
    public static final String DATABASE_NAME = "database_name";
}
