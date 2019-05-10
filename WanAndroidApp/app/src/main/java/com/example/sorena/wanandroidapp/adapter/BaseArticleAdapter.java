package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Set;

public class BaseArticleAdapter extends BaseAdapter
{
    private List<Article> allArticle;
    private Context context;
    private Set<Integer> collections;
    private int resourceId;
    private int toppingNum;


    public BaseArticleAdapter(Context context, int resourceId, @NonNull List<Article> normalArticle , @NonNull List<Article> toppingArticle){
        this.context = context;
        this.resourceId = resourceId;
        allArticle = new ArrayList<>();
        allArticle.addAll(toppingArticle);
        allArticle.addAll(normalArticle);
        toppingNum = toppingArticle.size();
        collections = new HashSet<>();
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
        BaseArticleAdapter.ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId,parent,false);
            viewHolder = new BaseArticleAdapter.ViewHolder();
            viewHolder.articleTextViewShowIsTopping = view.findViewById(R.id.article_textView_showIsTopping);
            viewHolder.articleTextViewTitle = view.findViewById(R.id.article_textView_title);
            viewHolder.articleTextViewAuthor = view.findViewById(R.id.article_textView_author);
            viewHolder.articleTextViewChapterName = view.findViewById(R.id.article_textView_chapterName);
            viewHolder.articleTextViewTime = view.findViewById(R.id.article_textView_time);
            viewHolder.articleImageViewCollect = view.findViewById(R.id.article_imageView_collect);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (BaseArticleAdapter.ViewHolder)view.getTag();
        }
        viewHolder.articleTextViewTitle.setTextColor(Color.parseColor("#000000"));
        if (toppingNum  >= position + 1){
            viewHolder.articleTextViewTitle.setTextColor(Color.parseColor("#1296db"));
        }
        viewHolder.articleTextViewAuthor.setText(article.getAuthor());
        viewHolder.articleTextViewChapterName.setText(article.getChapterName());
        viewHolder.articleTextViewTitle.setText(article.getTitle());
        viewHolder.articleTextViewTime.setText(article.getNiceDate());
        if (collections.contains(article.getId())){
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
                                imageView.setImageResource(R.drawable.ic_collect_selected);
                                imageView.setTag(R.drawable.ic_collect_selected);
                                collections.add(((Article) getItem(position)).getId());
                            }else {
                                imageView.setImageResource(R.drawable.ic_collect_normal);
                                imageView.setTag(R.drawable.ic_collect_normal);
                                collections.remove(((Article) getItem(position)).getId());
                            }
                        }catch (ClassCastException e){
                            e.printStackTrace();
                            LogUtil.d("日志:警告","不能强制转化");
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


    public void addNormalArticleData(List<Article> articles){
        if (articles != null && this.allArticle != articles){
            this.allArticle.addAll(articles);

        }
        notifyDataSetChanged();
    }


    public void clearData(){
        allArticle.clear();
        toppingNum = 0;
        notifyDataSetChanged();
    }

    public void resetToppingArticle(List<Article> toppingArticle){
        if (toppingArticle == null) return;
        if (allArticle.size() != 0  && toppingNum != allArticle.size()){
            List<Article> normalArticles = new ArrayList<>();
            for (int i = toppingNum ; i < allArticle.size() ; i++){
                normalArticles.add(allArticle.get(i));
            }
            allArticle.clear();
            allArticle.addAll(toppingArticle);
            allArticle.addAll(normalArticles);
        }else {
            allArticle.addAll(toppingArticle);
        }
        toppingNum = toppingArticle.size();
        notifyDataSetChanged();
    }


}
