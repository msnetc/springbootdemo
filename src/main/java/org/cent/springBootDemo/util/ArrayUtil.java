package org.cent.springBootDemo.util;

import java.util.Collection;

/**
 * 集合工具类
 * Created by cent on 2016/9/21.
 */
public abstract class ArrayUtil {

    /**
     * 判断集合是否为空
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection c) {
        if (c == null) {
            return true;
        } else if (c.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否非空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection c) {
        if (c == null) {
            return false;
        } else if (c.size() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断数组是否为空
     * @param c
     * @return
     */
    public static boolean isEmpty(Object[] c) {
        if (c == null) {
            return true;
        } else if (c.length <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否非空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Object[] c) {
        if (c == null) {
            return false;
        } else if (c.length <= 0) {
            return false;
        }
        return true;
    }
}
