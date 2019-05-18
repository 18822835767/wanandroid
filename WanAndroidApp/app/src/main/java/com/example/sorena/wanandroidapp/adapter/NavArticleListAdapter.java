package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.NavFlowItem;
import com.example.sorena.wanandroidapp.bean.NavListItem;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.view.WebActivity;
import com.example.sorena.wanandroidapp.widget.FlowLayout;
import com.example.sorena.wanandroidapp.widget.FlowLayoutFactory;

import java.util.LinkedList;
import java.util.List;

public class NavArticleListAdapter extends BaseAdapter
{

    private List<NavListItem> mNavListItems;
    private Context mContext;
    private int mResourceId;

    public NavArticleListAdapter(Context context, int resourceId, List<NavListItem> navListItems){
        mNavListItems = navListItems;
        mContext = context;
        mResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return mNavListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        NavListItem navListItem = mNavListItems.get(position);
        if (convertView == null){

            view = LayoutInflater.from(mContext).inflate(R.layout.item_nav_article_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.navArticleListItemTextViewItem = view.findViewById(R.id.navArticleListItem_textView_item);
            viewHolder.navArticleListItemFlowLayoutItem = view.findViewById(R.id.navArticleListItem_flowLayout_item);
            view.setTag(viewHolder);

        }else {

            view = convertView;
            viewHolder = (ViewHolder)(view.getTag());
        }
        List<String> titles = new LinkedList<>();
        List<Object> links = new LinkedList<>();
        List<NavFlowItem> flowItems = navListItem.getFlowItems();
        int size = flowItems.size();
        for (int i = 0; i < size; i++) {
            titles.add(flowItems.get(i).getTitle());
            links.add(flowItems.get(i).getLink());
        }


        viewHolder.navArticleListItemTextViewItem.setText(navListItem.getName());
        FlowLayoutFactory.setFlowLayout(viewHolder.navArticleListItemFlowLayoutItem, mContext, R.layout.nav_tv, titles, links, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = v.getTag().toString();
                Intent intent = new Intent(mContext,WebActivity.class);
                intent.putExtra("url",link);
                mContext.startActivity(intent);
            }
        },true);
        return view;
    }


    class ViewHolder{
        TextView navArticleListItemTextViewItem;
        FlowLayout navArticleListItemFlowLayoutItem;
    }



}
