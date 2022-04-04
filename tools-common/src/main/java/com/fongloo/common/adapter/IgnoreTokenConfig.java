package com.fongloo.common.adapter;

import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * 忽略Token配置类
 */
public class IgnoreTokenConfig {
    public static final List<String> LIST = Arrays.asList(
            "/error",
            "/actuator/**",
            "/gate/**",
            "/static/**",
            "/anno/**",
            "/**/anno/**",
            "/**/swagger-ui.html",
            "/**/doc.html",
            "/**/webjars/**",
            "/**/v2/api-docs/**",
            "/**/v2/api-docs-ext/**",
            "/**/swagger-resources/**",
            "/menu/router/**"
    );
    
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    
    public static boolean isIgnoreToken(String currentUrl) {return isIgnoreToken(LIST,currentUrl);}

    private static boolean isIgnoreToken(List<String> list, String currentUrl) {
        if (list.isEmpty()) {
            return false;
        }
        return list.stream().anyMatch(
                url -> currentUrl.startsWith(url) || ANT_PATH_MATCHER.match(url,currentUrl)
        );
    }
}
