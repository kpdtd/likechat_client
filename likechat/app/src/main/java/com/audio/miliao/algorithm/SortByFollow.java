package com.audio.miliao.algorithm;

import com.audio.miliao.entity.Zone;

import java.util.List;

/**
 * 按照用户关注的主播发的动态，最新的放最上面
 */
public class SortByFollow implements SortZoneList
{
    @Override
    public List<Zone> sort(List<Zone> zoneList)
    {
        return zoneList;
    }
}
