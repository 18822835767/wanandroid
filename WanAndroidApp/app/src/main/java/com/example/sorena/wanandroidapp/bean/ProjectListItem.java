package com.example.sorena.wanandroidapp.bean;

/**
 * 项目列表适配器
 */
public class ProjectListItem
{
    private String pictureLink;
    private String projectLink;
    private String title;
    private String description;
    private String date;
    private String author;
    private int id;
    private boolean collect;


    public ProjectListItem(String pictureLink, String projectLink, String title, String description, String date, String author, int id,boolean collect) {
        this.pictureLink = pictureLink;
        this.projectLink = projectLink;
        this.title = title;
        this.description = description;
        this.date = date;
        this.author = author;
        this.id = id;
        this.collect = collect;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return "ProjectListItem{" +
                "pictureLink='" + pictureLink + '\'' +
                ", projectLink='" + projectLink + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", id=" + id +
                ", collect=" + collect +
                '}';
    }
}
