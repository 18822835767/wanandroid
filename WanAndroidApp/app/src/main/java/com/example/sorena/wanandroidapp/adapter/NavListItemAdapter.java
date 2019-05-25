package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.NavListItem;
import com.example.sorena.wanandroidapp.util.ViewHolder;

import java.util.List;

/**
 * 导航列表适配器
 * navFragment左边的列表适配器
 */
public class NavListItemAdapter extends BaseAdapter
{
    private Context mContext;
    private int mResourceId;
    private List<NavListItem> mListItems;
    private NavListItem mNavListItemSelected;
    private long mPrevSelectTime;



    public NavListItemAdapter(Context context, int resourceId, List<NavListItem> listItems) {
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mListItems = listItems;
        setSelected(0);
        mPrevSelectTime = System.currentTimeMillis();
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent,mResourceId,position);
        NavListItem navListItem = mListItems.get(position);
        ((TextView)viewHolder.getView(R.id.navListItem_textView_showName)).setText(navListItem.getName());
        if (navListItem == mNavListItemSelected){
            ((TextView)viewHolder.getView(R.id.navListItem_textView_showName)).setTextColor(Color.parseColor("#e98f36"));
        }else {
            ((TextView)viewHolder.getView(R.id.navListItem_textView_showName)).setTextColor(Color.parseColor("#ffffff"));
        }
        return viewHolder.getConvertView();
    }


    public void setSelected(int index){
        mNavListItemSelected = mListItems.get(index);
        notifyDataSetChanged();
    }


}

//        if (convertView == null){
//            view = LayoutInflater.from(mContext).inflate(mResourceId,parent,false);
//            viewHolder = new ViewHolder();
//            viewHolder.navListItemTextViewShowName = view.findViewById(R.id.navListItem_textView_showName);
//            view.setTag(viewHolder);
//        }
//        else {
//            view = convertView;
//            viewHolder = (ViewHolder) (view.getTag());
//        }
//        viewHolder.navListItemTextViewShowName.setText(navListItem.getName());
//        if (navListItem == mNavListItemSelected){
//            viewHolder.navListItemTextViewShowName.setTextColor(Color.parseColor("#e98f36"));
//        }else {
//            viewHolder.navListItemTextViewShowName.setTextColor(Color.parseColor("#ffffff"));
//        }
//    class ViewHolder{
//        TextView navListItemTextViewShowName;
//    }
