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

import java.util.ArrayList;
import java.util.List;

/**
 * 首页文章适配器
 * 用于homeFragment
 */
public class BaseArticleAdapter extends BaseAdapter
{
    private List<Article> mAllArticle;
    private Context mContext;
    private int mResourceId;
    private int mToppingNum;


    public BaseArticleAdapter(Context context, int resourceId, @NonNull List<Article> normalArticle , @NonNull List<Article> toppingArticle){
        this.mContext = context;
        this.mResourceId = resourceId;
        mAllArticle = new ArrayList<>();
        mAllArticle.addAll(toppingArticle);
        mAllArticle.addAll(normalArticle);
        mToppingNum = toppingArticle.size();
        addToCollectManagerSet(mAllArticle);
    }

    private void addToCollectManagerSet(List<Article> articleList){
        if (articleList == null){
            return;
        }
        for (Article article: articleList) {
            if (article.isCollect()){
                CollectManager.getInstance().addToCollectSet(article.getId());
            }
        }
    }



    @Override
    public int getCount() {
        return mAllArticle.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllArticle.get(position);
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
            view = LayoutInflater.from(mContext).inflate(mResourceId,parent,false);
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
        if (mToppingNum >= position + 1){
            viewHolder.articleTextViewTitle.setTextColor(Color.parseColor("#1296db"));
        }
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
                                CollectManager.getInstance().addCollect(((Article) getItem(position)).getId(),viewHolder.articleImageViewCollect,(Activity) mContext);
                            }else {
                                CollectManager.getInstance().cancelCollect(((Article) getItem(position)).getId(),viewHolder.articleImageViewCollect,(Activity) mContext);
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


    public void addNormalArticleData(List<Article> articles){
        if (articles != null && this.mAllArticle != articles){
            this.mAllArticle.addAll(articles);
            addToCollectManagerSet(articles);
        }
        notifyDataSetChanged();
    }


    public void clearData(){
        if (mAllArticle != null){
            mAllArticle.clear();
        }
        mToppingNum = 0;
        notifyDataSetChanged();
    }

    //替换置顶文章
    public void resetToppingArticle(List<Article> toppingArticle){
        if (toppingArticle == null) return;
        if (mAllArticle.size() != 0  && mToppingNum != mAllArticle.size()){
            List<Article> normalArticles = new ArrayList<>();
            for (int i = mToppingNum; i < mAllArticle.size() ; i++){
                normalArticles.add(mAllArticle.get(i));
            }
            mAllArticle.clear();
            mAllArticle.addAll(toppingArticle);
            mAllArticle.addAll(normalArticles);
        }else {
            mAllArticle.addAll(toppingArticle);
        }
        mToppingNum = toppingArticle.size();
        addToCollectManagerSet(toppingArticle);
        notifyDataSetChanged();
    }


}
