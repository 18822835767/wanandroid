package com.example.sorena.wanandroidapp.bean;

import java.io.Serializable;
import java.util.List;

public class Chapter implements Serializable
{
    private List<FlowItem> flowItems;
    private String chapterName;


    public List<FlowItem> getFlowItems() {
        return flowItems;
    }

    public void setFlowItems(List<FlowItem> flowItems) {
        this.flowItems = flowItems;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "flowItems=" + flowItems +
                ", chapterName='" + chapterName + '\'' +
                '}' + '\n';
    }
}
