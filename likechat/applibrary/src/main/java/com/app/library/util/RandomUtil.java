package com.app.library.util;

import java.util.ArrayList;
import java.util.List;
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

        return min;
    }

    /**
     * [min , max） 之间的size个随机数
     * @param min
     * @param max
     * @param size
     * @return
     */
    public static Integer[] nextInts(int min, int max, int size)
    {
        Integer[] randoms = new Integer[size];
        for (int i = 0; i < size; i++)
        {
            randoms[i] = min;
        }

        try
        {
            Random rand = new Random();
            List<Integer> randomSet = new ArrayList<>();
            for (int i = 0; i < size; )
            {
                int random = rand.nextInt(max - min) + min;
                if (randomSet.contains(random))
                {
                    do
                    {
                        random = rand.nextInt(max - min) + min;
                    }
                    while (randomSet.contains(random));
                }

                randomSet.add(random);
                i++;
            }

            randoms = randomSet.toArray(new Integer[size]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return randoms;
    }
}
