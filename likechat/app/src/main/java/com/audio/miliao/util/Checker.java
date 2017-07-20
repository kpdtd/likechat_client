package com.audio.miliao.util;

import java.util.Collection;
import java.util.Map;

/**
 * 各种检查工具
 */
public class Checker
{
    public static <E> boolean isEmpty(Collection<E> collection)
    {
        if (collection == null || collection.size() == 0)
        {
            return true;
        }

        return false;
    }

    public static <E> boolean isNotEmpty(Collection<E> collection)
    {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(String str)
    {
        if (str == null || str.length() == 0)
        {
            return true;
        }

        return false;
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static <E> boolean isEmpty(E[] array)
    {
        return (array == null || array.length == 0);
    }

    public static <E> boolean isNotEmpty(E[] array)
    {
        return !isEmpty(array);
    }

    public static <K, V> boolean isEmpty(Map<K, V> sourceMap)
    {
        return (sourceMap == null || sourceMap.size() == 0);
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> sourceMap)
    {
        return (sourceMap == null || sourceMap.size() == 0);
    }
}
