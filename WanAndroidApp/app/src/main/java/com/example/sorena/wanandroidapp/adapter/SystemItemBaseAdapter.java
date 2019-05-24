package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.view.ShowSystemItemActivity;
import com.example.sorena.wanandroidapp.view.SystemActivity;
import com.example.sorena.wanandroidapp.widget.FlowLayout;
import com.example.sorena.wanandroidapp.widget.FlowLayoutFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * systemFragment的listView的列表适配器
 */
public class SystemItemBaseAdapter extends BaseAdapter implements View.OnClickListener
{

    private List<Chapter> mChapters;
    private int mResourceId;
    private Context mContext;

    public SystemItemBaseAdapter(Context context, int resourceId, List<Chapter> chapters) {
        super();
        this.mChapters = chapters;
        this.mResourceId = resourceId;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mChapters.size();
    }

    @Override
    public Object getItem(int position) {
        return mChapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        List<FlowItem> flowItemList = mChapters.get(position).getFlowItems();
        List<String> flowName = new ArrayList<>();
        for (int i = 0; i < flowItemList.size(); i++) {
            flowName.add(flowItemList.get(i).getName());
        }
        //return FlowLayoutFactory.getSystemItem(mContext, flowName, mChapters.get(position).getChapterName(), flowItemList, null);
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }else {
            view = LayoutInflater.from(mContext).inflate(mResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.systemItem_textView_character);
            viewHolder.flowLayout = view.findViewById(R.id.systemItem_flowLayout_item);
            view.setTag(viewHolder);
        }
        viewHolder.textView.setText(mChapters.get(position).getChapterName());
        FlowItem[] flowItems = new FlowItem[flowItemList.size()];
        int size = flowItemList.size();
        for (int i = 0 ; i < size ; i++){
            flowItems[i] = flowItemList.get(i);
        }
        Integer[] integers = new Integer[size];
        for (int i = 0; i < size; i++) {
            integers[i] = i;
        }
        FlowLayoutFactory.setFlowLayout(viewHolder.flowLayout, mContext,
                R.layout.system_flowlayout_tv, flowName, integers,
                (v)->
                {
                    Chapter chapter = mChapters.get(position);
                    Intent intent = new Intent(mContext,SystemActivity.class);
                    intent.putExtra("chapterData",chapter);
                    String string = v.getTag().toString();
                    intent.putExtra("position",string);
                    mContext.startActivity(intent);

                },true);
        return view;
    }


    class ViewHolder{

        TextView textView;
        FlowLayout flowLayout;

    }


    @Override
    public void onClick(View v) {

        try {
            FlowItem flowItem = (FlowItem)(v.getTag());
            Intent intent = new Intent(mContext,ShowSystemItemActivity.class);
            intent.putExtra("data",flowItem);
            mContext.startActivity(intent);
        }catch (ClassCastException e){
            LogUtil.d("日志:exception","转化失败");
            return;
        }


    }
}
