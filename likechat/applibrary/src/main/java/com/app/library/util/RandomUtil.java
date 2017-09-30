package com.app.library.util;

import java.util.Random;

/**
 * 生成随机数
 */
public class RandomUtil
{
    /***
     * [0 , max）之间的随机数
     * @param max
     * @return
     */
    public static int nextInt(int max)
    {
        return nextInt(0, max);
    }

    /**
     * [min , max） 之间的随机数
     * @param min
     * @param max
     * @return
     */
    public static int nextInt(int min, int max)
    {
        try
        {
            Random rand = new Random();
            int nIndex = rand.nextInt(max - min) + min;
            return nIndex;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }
}
