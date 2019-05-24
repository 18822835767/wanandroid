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
import com.example.sorena.wanandroidapp.util.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * 搜索结果列表适配器
 */
public class SearchResultListAdapter extends BaseAdapter
{
    private List<SearchResult> mResults;
    private Context mContext;
    private int mResourceId;


    public SearchResultListAdapter(Context context, int resourceId, List<SearchResult> results) {
        super();
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mResults = results;
        CollectManager.getInstance().addToCollectSetByResult(results);
    }


    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchResult result = mResults.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent,mResourceId,position);
        ((TextView)viewHolder.getView(R.id.search_textView_showAuthor)).setText(result.getAuthor());
        ((TextView)viewHolder.getView(R.id.searchResult_textView_showChapterName)).setText(result.getChapterName());
        ((TextView)viewHolder.getView(R.id.searchResult_textView_showSuperChapterName)).setText(result.getSuperChapterName());
        ((TextView)viewHolder.getView(R.id.searchResult_textView_showTitle)).setText(result.getTitle());
        ((TextView)viewHolder.getView(R.id.searchResult_textView_showTime)).setText(result.getDate());
        CollectManager.getInstance().setCollectImageView((Activity) mContext,
                (viewHolder.getView(R.id.searchResult_imageView_showCollect)),result.getId());
        return viewHolder.getConvertView();
    }

//    class ViewHolder{
//        TextView searchTextViewShowAuthor;
//        TextView searchResultTextViewShowChapterName;
//        TextView searchResultTextViewShowSuperChapterName;
//        TextView searchResultTextViewShowTitle;
//        ImageView searchResultImageViewShowCollect;
//        TextView searchResultTextViewShowTime;
//    }

    public void addData(List<SearchResult> results){
        if (results == null){
            results = new LinkedList<>();
        }
        this.mResults.addAll(results);
        CollectManager.getInstance().addToCollectSetByResult(results);
        notifyDataSetChanged();
    }

    public void clearData(){
        if (mResults != null){
            mResults.clear();
        }
    }

}
//    ViewHolder viewHolder;
//        if (convertView == null){
//                view = LayoutInflater.from(mContext).inflate(mResourceId,parent,false);
//                viewHolder = new ViewHolder();
//                viewHolder.searchTextViewShowAuthor = view.findViewById(R.id.search_textView_showAuthor);
//                viewHolder.searchResultTextViewShowChapterName = view.findViewById(R.id.searchResult_textView_showChapterName);
//                viewHolder.searchResultTextViewShowSuperChapterName = view.findViewById(R.id.searchResult_textView_showSuperChapterName);
//                viewHolder.searchResultTextViewShowTitle = view.findViewById(R.id.searchResult_textView_showTitle);
//                viewHolder.searchResultImageViewShowCollect = view.findViewById(R.id.searchResult_imageView_showCollect);
//                viewHolder.searchResultTextViewShowTime = view.findViewById(R.id.searchResult_textView_showTime);
//                view.setTag(viewHolder);
//                }else {
//                view = convertView;
//                viewHolder = (ViewHolder) (view.getTag());
//                }
//
//                viewHolder.searchTextViewShowAuthor.setText(result.getAuthor());
//                viewHolder.searchResultTextViewShowChapterName.setText(result.getChapterName());
//                viewHolder.searchResultTextViewShowSuperChapterName.setText(result.getSuperChapterName());
//                viewHolder.searchResultTextViewShowTitle.setText(result.getTitle());
//                viewHolder.searchResultTextViewShowTime.setText(result.getDate());
//                CollectManager.getInstance().setCollectImageView((Activity) mContext,
//                viewHolder.searchResultImageViewShowCollect,result.getId());
//                return view;
