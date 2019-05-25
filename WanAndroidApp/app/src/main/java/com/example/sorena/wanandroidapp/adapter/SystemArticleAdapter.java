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
import com.example.sorena.wanandroidapp.util.ViewHolder;

import java.util.List;
import java.util.Set;

/**
 * 体系文章列表的适配器
 */
public class SystemArticleAdapter extends BaseAdapter
{
    private List<Article> mAllArticle;
    private int mResourceId;
    private Context mContext;



    public SystemArticleAdapter(Context context, int resourceId, @NonNull List<Article> allArticle, Set<Integer> collections){
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mAllArticle = allArticle;
        CollectManager.getInstance().addToCollectSetByArticle(allArticle);
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
        ViewHolder viewHolder = new ViewHolder(mContext,parent,mResourceId,position);
        ((TextView)viewHolder.getView(R.id.article_textView_title)).setTextColor(Color.parseColor("#000000"));
        ((TextView)viewHolder.getView(R.id.article_textView_author)).setText(article.getAuthor());
        ((TextView)viewHolder.getView(R.id.article_textView_chapterName)).setText(article.getChapterName());
        ((TextView)viewHolder.getView(R.id.article_textView_title)).setText(article.getTitle());
        ((TextView)viewHolder.getView(R.id.article_textView_time)).setText(article.getNiceDate());
        CollectManager.getInstance().setCollectImageView((Activity)mContext,
                ((ImageView)viewHolder.getView(R.id.article_imageView_collect)),article.getId());
        return viewHolder.getConvertView();
    }

    public void addData(List<Article> articles){
        if (articles != null && this.mAllArticle != articles){
            this.mAllArticle.addAll(articles);
            CollectManager.getInstance().addToCollectSetByArticle(articles);
        }
        notifyDataSetChanged();
    }


    public void clearData(){
        mAllArticle.clear();
        notifyDataSetChanged();
    }

}


//    View view;
//    SystemArticleAdapter.ViewHolder viewHolder;
//        if (convertView == null){
//                view = LayoutInflater.from(mContext).inflate(mResourceId,parent,false);
//                viewHolder = new SystemArticleAdapter.ViewHolder();
//                viewHolder.articleTextViewShowIsTopping = view.findViewById(R.id.article_textView_showIsTopping);
//                viewHolder.articleTextViewTitle = view.findViewById(R.id.article_textView_title);
//                viewHolder.articleTextViewAuthor = view.findViewById(R.id.article_textView_author);
//                viewHolder.articleTextViewChapterName = view.findViewById(R.id.article_textView_chapterName);
//                viewHolder.articleTextViewTime = view.findViewById(R.id.article_textView_time);
//                viewHolder.articleImageViewCollect = view.findViewById(R.id.article_imageView_collect);
//                view.setTag(viewHolder);
//                }else {
//                view = convertView;
//                viewHolder = (SystemArticleAdapter.ViewHolder)view.getTag();
//                }
//                viewHolder.articleTextViewTitle.setTextColor(Color.parseColor("#000000"));
//                viewHolder.articleTextViewAuthor.setText(article.getAuthor());
//                viewHolder.articleTextViewChapterName.setText(article.getChapterName());
//                viewHolder.articleTextViewTitle.setText(article.getTitle());
//                viewHolder.articleTextViewTime.setText(article.getNiceDate());
//                CollectManager.getInstance().setCollectImageView((Activity)mContext,
//                viewHolder.articleImageViewCollect,article.getId());
//                return view;

//    class ViewHolder{
//
//        TextView articleTextViewShowIsTopping;
//
//        TextView articleTextViewTitle;
//
//        TextView articleTextViewAuthor;
//
//        TextView articleTextViewChapterName;
//
//        TextView articleTextViewTime;
//
//        ImageView articleImageViewCollect;
//    }
