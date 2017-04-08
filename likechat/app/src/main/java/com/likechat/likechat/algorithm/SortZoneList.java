package com.likechat.likechat.algorithm;

import com.likechat.likechat.entity.Zone;

import java.util.List;

/**
 * 对发现界面的用户动态进行排序
 */
public interface SortZoneList
{
    public List<Zone> sort(List<Zone> zoneList);
}
