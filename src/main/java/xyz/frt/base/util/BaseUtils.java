package xyz.frt.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author phw
 * @date Created in 04-08-2018
 * @description
 */
public class BaseUtils {

    /**
     * 判断一个对象是否为空
     * @param object 要判断的对象
     * @return 是否为空
     */
    public static boolean isNullOrEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            String str = (String) object;
            return str.equals("") || str.length() == 0;
        } if (object instanceof List<?>) {
            List<?> list = (List<?>) object;
            return list.size() == 0;
        } if (object instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) object;
            return map.size() == 0;
        }
        return false;
    }


    public static Map<String, Object> object2ConditionMap(Object item) {
        if (isNullOrEmpty(item)) {
            return null;
        }
        try {
            Map<String, Object> map = new HashMap<>();
            Field[] fields = item.getClass().getDeclaredFields();
            Field[] superFields = item.getClass().getSuperclass().getDeclaredFields();
            getFieldsAndValues(item, map, fields);
            getFieldsAndValues(item, map, superFields);
            return map;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断一个字符串是否为数字
     * @param str str
     * @return true or false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 替换字符串
     * @param str str
     * @return
     */
    public static String replaceInteger(String str) {
        return str.replaceAll("^[0-9]*$", "{id}");
    }

    private static void getFieldsAndValues(Object item, Map<String, Object> map, Field[] fields) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Field field: fields) {
            String fieldName = field.getName();
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            String methodName = "get" + fieldName;
            Object value = item.getClass().getMethod(methodName).invoke(item);
            if (value != null) {
                map.put(field.getName(), value);
            }
        }
    }

}
