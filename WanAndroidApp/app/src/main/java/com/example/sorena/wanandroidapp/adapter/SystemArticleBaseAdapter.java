package com.example.sorena.wanandroidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.manager.CollectManager;
import com.example.sorena.wanandroidapp.util.LogUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemArticleBaseAdapter extends BaseAdapter
{
    private List<Article> allArticle;
    private int resourceId;
    private Context context;



    public SystemArticleBaseAdapter(Context context, int resourceId, @NonNull List<Article> allArticle, Set<Integer> collections){
        this.context = context;
        this.resourceId = resourceId;
        this.allArticle = allArticle;
        CollectManager.getInstance().addToCollectSet(allArticle);
    }




    @Override
    public int getCount() {
        return allArticle.size();
    }

    @Override
    public Object getItem(int position) {
        return allArticle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Article article = (Article) getItem(position);
        View view;
        SystemArticleBaseAdapter.ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId,parent,false);
            viewHolder = new SystemArticleBaseAdapter.ViewHolder();
            viewHolder.articleTextViewShowIsTopping = view.findViewById(R.id.article_textView_showIsTopping);
            viewHolder.articleTextViewTitle = view.findViewById(R.id.article_textView_title);
            viewHolder.articleTextViewAuthor = view.findViewById(R.id.article_textView_author);
            viewHolder.articleTextViewChapterName = view.findViewById(R.id.article_textView_chapterName);
            viewHolder.articleTextViewTime = view.findViewById(R.id.article_textView_time);
            viewHolder.articleImageViewCollect = view.findViewById(R.id.article_imageView_collect);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (SystemArticleBaseAdapter.ViewHolder)view.getTag();
        }
        viewHolder.articleTextViewTitle.setTextColor(Color.parseColor("#000000"));
        viewHolder.articleTextViewAuthor.setText(article.getAuthor());
        viewHolder.articleTextViewChapterName.setText(article.getChapterName());
        viewHolder.articleTextViewTitle.setText(article.getTitle());
        viewHolder.articleTextViewTime.setText(article.getNiceDate());
        if (CollectManager.getInstance().isCollect(article.getId())){
            viewHolder.articleImageViewCollect.setImageResource(R.drawable.ic_collect_selected);
            viewHolder.articleImageViewCollect.setTag(R.drawable.ic_collect_selected);
        }else {
            viewHolder.articleImageViewCollect.setImageResource(R.drawable.ic_collect_normal);
            viewHolder.articleImageViewCollect.setTag(R.drawable.ic_collect_normal);
        }
        viewHolder.articleImageViewCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.article_imageView_collect:
                        try {
                            ImageView imageView = (ImageView)v;
                            if (imageView.getTag().equals(R.drawable.ic_collect_normal)){
                                CollectManager.getInstance().addCollect(article.getId(),imageView,(Activity)context);
                            }else {
                                CollectManager.getInstance().cancelCollect(article.getId(),imageView,(Activity)context);
                            }
                        }catch (ClassCastException e){
                            e.printStackTrace();
                            LogUtil.e("日志:警告","不能强制转化");
                        }
                }
            }
        });
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
        if (articles != null && this.allArticle != articles){
            this.allArticle.addAll(articles);
            CollectManager.getInstance().addToCollectSet(articles);
        }
        notifyDataSetChanged();
    }


    public void clearData(){
        allArticle.clear();
        notifyDataSetChanged();
    }








}
