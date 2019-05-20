package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.History;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseOperator;

import java.util.List;

/**
 * 搜索历史适配器
 */
public class HistoryAdapt extends ArrayAdapter<History>
{
    private int mResourceId;
    private List<History> mHistoryList;

    public HistoryAdapt(Context context, int resource,List<History> objects) {
        super(context, resource, objects);
        mResourceId = resource;
        this.mHistoryList = objects;
    }




    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        History history = getItem(position);
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId,parent,false);
        }else {
            view = convertView;
        }
        TextView wordView = view.findViewById(R.id.historyList_textView_showWord);
        ImageView imageView = view.findViewById(R.id.historyList_imageView_delWord);
        wordView.setText(history.getWord());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(position);
            }
        });
        return view;
    }

    public void clearData() {
        if (mHistoryList != null){
            clear();
        }
    }

    class ViewHolder{

        ImageView delImage;
        TextView wordTextView;
    }



    public void addData(String word){
        History history  = new History(0,word);
        if (mHistoryList.contains(history)){
            int index = mHistoryList.indexOf(history);
            history = mHistoryList.get(index);
            mHistoryList.remove(index);
            mHistoryList.add(0,history);
        }else {
            mHistoryList.add(0,history);
            SearchHistoryDataBaseOperator.getInstance().addData(history.getWord());
        }
        notifyDataSetChanged();
    }

    private OnItemDeleteListener listener;

    public interface OnItemDeleteListener {
        void onDeleteClick(int i);
    }

    public void setListener(OnItemDeleteListener listener) {
        this.listener = listener;
    }

    public List<History> getmHistoryList(){
        return mHistoryList;
    }
}
