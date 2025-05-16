package dev.pages.creeperbabytea.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericUtils {
    /**
     * 获取对象的泛型参数
     *
     * @param obj 目标对象
     * @return 泛型参数对应的 Class，如果无法解析则返回 null
     */
    public static Type[] getGenericParameters(Object obj) {
        Type superclass = obj.getClass().getGenericSuperclass();

        if (superclass instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return null;
    }

    /**
     * 获取对象的第 i 个泛型参数的 Class
     *
     * @param obj 目标对象
     * @param index 泛型参数索引，从 0 开始
     * @return 泛型参数对应的 Class，如果无法解析则返回 null
     */
    public static Type getGenericParameter(Object obj, int index) {
        Type superclass = obj.getClass().getGenericSuperclass();

        if (superclass instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments()[index];
        }
        return null;
    }
}
