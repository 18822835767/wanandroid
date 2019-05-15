package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.util.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CollectionAdapter extends BaseAdapter
{

    private List<Article> articleList;
    private Context context;
    private int resourceId;


    public CollectionAdapter(Context context, int resourceId,List<Article> articleList){
        this.context = context;
        this.resourceId = resourceId;
        this.articleList = articleList;
    }


    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Article article = (Article) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.articleTextViewShowIsTopping = view.findViewById(R.id.article_textView_showIsTopping);
            viewHolder.articleTextViewTitle = view.findViewById(R.id.article_textView_title);
            viewHolder.articleTextViewAuthor = view.findViewById(R.id.article_textView_author);
            viewHolder.articleTextViewChapterName = view.findViewById(R.id.article_textView_chapterName);
            viewHolder.articleTextViewTime = view.findViewById(R.id.article_textView_time);
            viewHolder.articleImageViewCollect = view.findViewById(R.id.article_imageView_collect);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.articleTextViewTitle.setTextColor(Color.parseColor("#000000"));
        viewHolder.articleTextViewAuthor.setText(article.getAuthor());
        viewHolder.articleTextViewChapterName.setText(article.getChapterName());
        viewHolder.articleTextViewTitle.setText(article.getTitle());
        viewHolder.articleTextViewTime.setText(article.getNiceDate());
        return view;
    }

    class ViewHolder{

        TextView articleTextViewShowIsTopping;

        TextView articleTextViewTitle;

        TextView articleTextViewAuthor;

        TextView articleTextViewChapterName;

        TextView articleTextViewTime;

        ImageView articleImageViewCollect;
    }


    public void addData(List<Article> articles){
        if (articles != null && this.articleList != articles){
            this.articleList.addAll(articles);
        }
        notifyDataSetChanged();
    }


    public void clearData(){
        if (articleList != null){
            articleList.clear();
        }
        notifyDataSetChanged();
    }


    public List<Article> getArticleList(){
        return articleList;
    }




}
