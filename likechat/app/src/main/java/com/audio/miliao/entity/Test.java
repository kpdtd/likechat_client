package com.audio.miliao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Test
{
    @Id
    private Long id;
    private String name;
    private int age;

    @Generated(hash = 1255361573)
    public Test(Long id, String name, int age)
    {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 372557997)
    public Test()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }
}
