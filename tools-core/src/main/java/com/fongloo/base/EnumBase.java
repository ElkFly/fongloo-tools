package com.fongloo.base;

import com.fongloo.utils.MapHelper;

import java.util.Arrays;
import java.util.Map;

/**
 * 枚举类型基类
 *
 */
public interface EnumBase {
    /**
     * 将制定的枚举集合转成 map
     * key -> name
     * value -> desc
     *
     * @param list
     * @return
     */
    static Map<String, String> getMap(EnumBase[] list) {
        return MapHelper.uniqueIndex(Arrays.asList(list), EnumBase::getCode, EnumBase::getDesc);
    }


    /**
     * 编码重写
     *
     * @return
     */
    default String getCode() {
        return toString();
    }

    /**
     * 描述
     *
     * @return
     */
    String getDesc();
}
