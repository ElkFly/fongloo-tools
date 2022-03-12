package com.fongloo.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.owasp.validator.html.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * XSS工具类
 * 也用于过滤XSS攻击
 */
@Slf4j
public class XssUtils {
    // 策略文件名称
    private static final String ANTISAMY_TACTICS_FILE = "antisamy-anythinggoes.xml";

    private static Policy policy = null;

    /**
     * 读取XSS策略文件并生成 Policy 对象
     */
    static {
        log.debug("读取XSS策略文件: [ {} ]", ANTISAMY_TACTICS_FILE);
        InputStream inputStream = XssUtils.class.getClassLoader().getResourceAsStream(ANTISAMY_TACTICS_FILE);

        try {
            policy = Policy.getInstance(inputStream);
            log.debug("读取XSS策略文件: [ {} ] 成功！", ANTISAMY_TACTICS_FILE);

        } catch (PolicyException e) {
            e.printStackTrace();
            log.error("读取XSS策略文件: [ {} ] 失败, 原因为:", e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭XSS策略文件: [ {} ] 失败, 原因为:", e);
                }
            }
        }
    }

    /**
     * XSS 过滤
     *
     * @param paramValue           需要过滤的字符串
     * @param ignoneParamValueList 需要忽略的字符串列表
     * @return
     */
    public static String xssClean(String paramValue, List<String> ignoneParamValueList) {
        AntiSamy antiSamy = new AntiSamy();
        log.debug("XSS要过滤的值为: {}", paramValue);
        // 是否忽略
        if (isIngoreParamValue(paramValue, ignoneParamValueList)) {
            log.debug("XSS忽略过滤 值为: {}", paramValue);
            return paramValue;
        } else {
            try {
                final CleanResults scan = antiSamy.scan(paramValue, policy);
                scan.getErrorMessages().forEach(log::debug);
                String str = scan.getCleanHTML();
                /*String str = StringEscapeUtils.escapeHtml(cr.getCleanHTML());
                str = str.replaceAll((antiSamy.scan("&nbsp;", policy)).getCleanHTML(), "");
                str = StringEscapeUtils.unescapeHtml(str);*/
                /*str = str.replaceAll("&quot;", "\"");
                str = str.replaceAll("&amp;", "&");
                str = str.replaceAll("'", "'");
                str = str.replaceAll("'", "＇");

                str = str.replaceAll("&lt;", "<");
                str = str.replaceAll("&gt;", ">");*/
                log.debug("XSS过滤后的值为: ", str);
                return str;
            } catch (ScanException e) {
                log.error("XSS扫描失败，原因是: [ {} ]", e);
            } catch (PolicyException e) {
                log.error("AntiSamy 转换失败，原因是: : [ {} ]",e);
            }
        }
        return paramValue;
    }


    /**
     * 判断需要过滤的字符串是否可以忽略
     * 忽略：如果需要过滤的字符串为空 或
     * 在需要忽略的字符串列表中有这个字符串
     * <p>
     * 不忽略：如果需要过滤的字符串不为空 或
     * 在需要忽略的字符串列表中没有有这个字符串
     *
     * @param paramValue
     * @param ignoneParamValueList
     * @return
     */
    private static boolean isIngoreParamValue(String paramValue, List<String> ignoneParamValueList) {
        if (StrUtil.isBlank(paramValue)) {
            return true;
        }
        if (CollectionUtil.isEmpty(ignoneParamValueList)) {
            return false;
        } else {
            for (String ignoreParamValue : ignoneParamValueList) {
                if (paramValue.contains(ignoreParamValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
