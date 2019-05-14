package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.SearchResult;

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
            viewHolder.searchResultTextViewShowCollect = view.findViewById(R.id.searchResult_textView_showCollect);
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
        viewHolder.searchResultTextViewShowCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了:" + position + "项" , Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }






    class ViewHolder{
        TextView searchTextViewShowAuthor;
        TextView searchResultTextViewShowChapterName;
        TextView searchResultTextViewShowSuperChapterName;
        TextView searchResultTextViewShowTitle;
        ImageView searchResultTextViewShowCollect;
        TextView searchResultTextViewShowTime;
    }

    public void addData(List<SearchResult> results){
        if (results == null){
            results = new LinkedList<>();
        }
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    public void clearData(){
        if (results != null){
            results.clear();
        }
    }


}
