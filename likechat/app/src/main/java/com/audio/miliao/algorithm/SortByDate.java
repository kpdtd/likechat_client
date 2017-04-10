package com.audio.miliao.algorithm;

import com.audio.miliao.entity.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * 按照动态发表日期排序
 */
public class SortByDate implements SortZoneList
{
    @Override
    public List<Zone> sort(List<Zone> zoneList)
    {
        List<Zone> sortList = new ArrayList<>();
        try
        {
            while (zoneList.size() >= 0)
            {
                Zone latestZone = getLatestZone(zoneList);
                if (latestZone != null)
                {
                    sortList.add(latestZone);
                    zoneList.remove(latestZone);
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
    private Zone getLatestZone(List<Zone> zoneList)
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
                else if (latestZone.date < zone.date)
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
