package com.example.sorena.wanandroidapp.bean;

import java.io.Serializable;

public class FlowItem implements Serializable
{
    private String parentsName;
    private String name;
    private int id;


    public String getParentsName() {
        return parentsName;
    }

    public void setParentsName(String parentsName) {
        this.parentsName = parentsName;
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
        return "FlowItem{" +
                "parentsName='" + parentsName + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}' + '\n';
    }
}
