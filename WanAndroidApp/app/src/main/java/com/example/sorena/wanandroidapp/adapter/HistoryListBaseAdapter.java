package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.History;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseOperator;

import java.util.List;

public class HistoryListBaseAdapter extends BaseAdapter implements View.OnClickListener
{
    private List<History> historyList;
    private int resourceId;
    private Context context;
    public HistoryListBaseAdapter(Context context, int resourceId, List<History> historyList) {
        this.resourceId = resourceId;
        this.context = context;
        this.historyList = historyList;

    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        History history = historyList.get(position);
        View view;
        ViewHolder holder;
        if (convertView != null){

            view = convertView;
            holder  =(ViewHolder)(view.getTag());


        }else {
            view = LayoutInflater.from(context).inflate(resourceId,parent,false);
            holder = new ViewHolder();
            holder.historyListTextViewShowWord = view.findViewById(R.id.historyList_textView_showWord);
            holder.delImageView = view.findViewById(R.id.historyList_imageView_delWord);
            holder.delImageView.setTag(history);
            holder.delImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onDeleteClick(position);
                    }
                }
            });
            view.setTag(holder);
        }
        holder.historyListTextViewShowWord.setText(history.getWord());

        return view;
    }

    private class ViewHolder{

        private TextView historyListTextViewShowWord;
        private ImageView delImageView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.historyList_imageView_delWord:
                String word = v.getTag().toString();
                historyList.remove(new History(0,word));
                notifyDataSetChanged();
                SearchHistoryDataBaseOperator.getInstance().delData(word);
                break;
            default:
                break;
        }
    }

    public void addData(String word){
        History history = new History(0,word);
        if (historyList.contains(history)){



        }else {
            SearchHistoryDataBaseOperator.getInstance().addData(word);
            historyList.add(history);
            notifyDataSetChanged();
        }
    }

    public List<History> getHistoryList() {
        return historyList;
    }


    private OnItemDeleteListener listener;
    public interface OnItemDeleteListener{
        void onDeleteClick(int i);
    }

    public void setListener(OnItemDeleteListener listener){
        this.listener = listener;
    }

    public void delData(History history){
        historyList.remove(history);
        notifyDataSetChanged();
        SearchHistoryDataBaseOperator.getInstance().delData(history.getWord());
        notifyDataSetInvalidated();
    }

}
