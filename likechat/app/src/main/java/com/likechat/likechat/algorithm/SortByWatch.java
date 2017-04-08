package com.likechat.likechat.algorithm;

import com.likechat.likechat.entity.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * 按照观看数排序（最热门的）
 */
public class SortByWatch implements SortZoneList
{
    @Override
    public List<Zone> sort(List<Zone> zoneList)
    {
        List<Zone> sortList = new ArrayList<>();
        try
        {
            while (zoneList.size() >= 0)
            {
                Zone maxWatch = getMaxWatch(zoneList);
                if (maxWatch != null)
                {
                    sortList.add(maxWatch);
                    zoneList.remove(maxWatch);
                }
                else
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sortList;
    }

    /**
     * 获取最新的动态
     *
     * @param zoneList
     * @return
     */
    private Zone getMaxWatch(List<Zone> zoneList)
    {
        Zone latestZone = null;
        try
        {
            for (Zone zone : zoneList)
            {
                if (latestZone == null)
                {
                    latestZone = zone;
                }
                else if (latestZone.watch < zone.watch)
                {
                    latestZone = zone;
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return latestZone;
    }
}
