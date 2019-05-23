package com.example.sorena.wanandroidapp.bean;

import java.io.Serializable;

/**
 *导航的一个章节(projectFragment的viewPager的一个碎片的数据来源)
 */
public class ProjectChapter implements Serializable
{
    private String name;
    private int id;

    public ProjectChapter(String name, int id) {
        this.name = name;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProjectChapter{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}' + '\n';
    }
}
