package com.example.sorena.wanandroidapp.bean;

import com.example.sorena.wanandroidapp.myInterface.CollectAble;

/**
 * 搜索结果适配器
 */
public class SearchResult implements CollectAble
{
    private String author;
    private String chapterName;
    private String superChapterName;
    private String title;
    private int id;
    private String link;
    private boolean collect;
    private String date;


    public SearchResult(String author, String chapterName, String superChapterName, String title, int id, String link) {
        this.author = author;
        this.chapterName = chapterName;
        this.superChapterName = superChapterName;
        this.title = title;
        this.id = id;
        this.link = link;
    }

    public SearchResult(String author, String chapterName, String superChapterName, String title, int id, String link, boolean collect,String date) {
        this.author = author;
        this.chapterName = chapterName;
        this.superChapterName = superChapterName;
        this.title = title;
        this.id = id;
        this.link = link;
        this.collect = collect;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }


    @Override
    public String toString() {
        return "SearchResult{" +
                "author='" + author + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", superChapterName='" + superChapterName + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", link='" + link + '\'' +
                ", collect=" + collect +
                ", date='" + date + '\'' +
                '}' + '\n';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
