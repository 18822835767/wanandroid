package com.example.sorena.wanandroidapp.bean;

/**
 *
 */
public class NavFlowItem
{
    /**
     * 导航的流式布局对应的实体类
     */
    private String title;
    private String link;

    public NavFlowItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "NavFlowItem{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}' + '\n';
    }
}
