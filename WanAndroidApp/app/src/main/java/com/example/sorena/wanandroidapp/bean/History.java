package com.example.sorena.wanandroidapp.bean;

import java.util.Objects;

public class History
{
    private int id;
    private String word;

    public History(int id, String word) {
        this.id = id;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", word='" + word + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(word, history.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
