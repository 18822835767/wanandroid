package com.example.sorena.wanandroidapp.bean;

public class Article
{
    private String author;
    private String link;
    private String title;
    private String niceDate;
    private String chapterName;
    private Integer id;
    private boolean collect;

    public Article(String author, String link, String title, String niceDate, String chapterName , int id) {
        this.author = author;
        this.link = link;
        this.title = title;
        this.niceDate = niceDate;
        this.chapterName = chapterName;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "Article{" +
                "author='" + author + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", niceDate='" + niceDate + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", id=" + id +
                '}';
    }
}
