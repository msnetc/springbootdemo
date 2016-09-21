package org.cent.springBootDemo.util;

/**
 * 字符串工具类
 * Created by cent on 2016/9/21.
 */
public class StringUtil {

    /**
     * 将字符串数组转换为按指定分隔符分隔的字符串。
     *
     * @param strArrs   字符串数组
     * @param separator 分隔符
     * @return
     */
    public static String join(String[] strArrs, String separator) {
        if (ArrayUtil.isEmpty(strArrs)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String item : strArrs) {
            if (isNotBlank(sb.toString())) {
                sb.append(separator);
            }
            sb.append(item);
        }

        return sb.toString();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else if (str.length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        if (str == null) {
            return false;
        } else if (str.length() <= 0) {
            return false;
        }
        return true;
    }
}
