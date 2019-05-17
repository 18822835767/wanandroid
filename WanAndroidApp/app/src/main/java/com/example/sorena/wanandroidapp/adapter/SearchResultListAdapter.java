package com.example.sorena.wanandroidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.SearchResult;
import com.example.sorena.wanandroidapp.manager.CollectManager;
import com.example.sorena.wanandroidapp.util.LogUtil;

import java.util.LinkedList;
import java.util.List;

public class SearchResultListAdapter extends BaseAdapter
{
    private List<SearchResult> results;
    private Context context;
    private int resouceId;


    public SearchResultListAdapter(Context context, int resourceId, List<SearchResult> results) {
        super();
        this.context = context;
        this.resouceId = resourceId;
        this.results = results;
        CollectManager.getInstance().addToCollectSetByResult(results);
    }


    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchResult result = results.get(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resouceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.searchTextViewShowAuthor = view.findViewById(R.id.search_textView_showAuthor);
            viewHolder.searchResultTextViewShowChapterName = view.findViewById(R.id.searchResult_textView_showChapterName);
            viewHolder.searchResultTextViewShowSuperChapterName = view.findViewById(R.id.searchResult_textView_showSuperChapterName);
            viewHolder.searchResultTextViewShowTitle = view.findViewById(R.id.searchResult_textView_showTitle);
            viewHolder.searchResultImageViewShowCollect = view.findViewById(R.id.searchResult_imageView_showCollect);
            viewHolder.searchResultTextViewShowTime = view.findViewById(R.id.searchResult_textView_showTime);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) (view.getTag());
        }

        viewHolder.searchTextViewShowAuthor.setText(result.getAuthor());
        viewHolder.searchResultTextViewShowChapterName.setText(result.getChapterName());
        viewHolder.searchResultTextViewShowSuperChapterName.setText(result.getSuperChapterName());
        viewHolder.searchResultTextViewShowTitle.setText(result.getTitle());
        viewHolder.searchResultTextViewShowTime.setText(result.getDate());
        if (CollectManager.getInstance().isCollect(result.getId())){
            viewHolder.searchResultImageViewShowCollect.setImageResource(R.drawable.ic_collect_selected);
            viewHolder.searchResultImageViewShowCollect.setTag(R.drawable.ic_collect_selected);
        }else {
            viewHolder.searchResultImageViewShowCollect.setImageResource(R.drawable.ic_collect_normal);
            viewHolder.searchResultImageViewShowCollect.setTag(R.drawable.ic_collect_normal);
        }
        viewHolder.searchResultImageViewShowCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.searchResult_imageView_showCollect:
                        try {
                            ImageView imageView = (ImageView)v;
                            if (imageView.getTag().equals(R.drawable.ic_collect_normal)){
                                CollectManager.getInstance().addCollect(result.getId(),imageView,(Activity)context);
                            }else {
                                CollectManager.getInstance().cancelCollect(result.getId(),imageView,(Activity)context);
                            }
                        }catch (ClassCastException e){
                            e.printStackTrace();
                            LogUtil.e("日志:SearchResultListAdapter:警告","不能强制转化");
                        }
                }
            }
        });
        return view;
    }

    class ViewHolder{
        TextView searchTextViewShowAuthor;
        TextView searchResultTextViewShowChapterName;
        TextView searchResultTextViewShowSuperChapterName;
        TextView searchResultTextViewShowTitle;
        ImageView searchResultImageViewShowCollect;
        TextView searchResultTextViewShowTime;
    }

    public void addData(List<SearchResult> results){
        if (results == null){
            results = new LinkedList<>();
        }
        this.results.addAll(results);
        CollectManager.getInstance().addToCollectSetByResult(results);
        notifyDataSetChanged();
    }

    public void clearData(){
        if (results != null){
            results.clear();
        }
    }

}
