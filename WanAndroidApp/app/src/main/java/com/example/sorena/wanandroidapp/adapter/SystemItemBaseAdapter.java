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

public class SystemItemBaseAdapter extends BaseAdapter implements View.OnClickListener
{

    private List<Chapter> chapters;
    private int resourceId;
    private Context context;

    public SystemItemBaseAdapter(Context context, int resourceId, List<Chapter> chapters) {
        super();

        this.chapters = chapters;
        this.resourceId = resourceId;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public Object getItem(int position) {
        return chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        List<FlowItem> flowItemList = chapters.get(position).getFlowItems();
        List<String> flowName = new ArrayList<>();
        for (int i = 0; i < flowItemList.size(); i++) {
            flowName.add(flowItemList.get(i).getName());
        }
        //return FlowLayoutFactory.getSystemItem(context, flowName, chapters.get(position).getChapterName(), flowItemList, null);
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.systemItem_textView_character);
            viewHolder.flowLayout = view.findViewById(R.id.systemItem_flowLayout_item);
            view.setTag(viewHolder);
        }
        viewHolder.textView.setText(chapters.get(position).getChapterName());
        FlowItem[] flowItems = new FlowItem[flowItemList.size()];
        int size = flowItemList.size();
        for (int i = 0 ; i < size ; i++){
            flowItems[i] = flowItemList.get(i);
        }
        Integer[] integers = new Integer[size];
        for (int i = 0; i < size; i++) {
            integers[i] = i;
        }
        FlowLayoutFactory.setFlowLayout(viewHolder.flowLayout, context,
                R.layout.system_flowlayout_tv, flowName, integers,
                (v)->
                {
                    Chapter chapter = chapters.get(position);
                    Intent intent = new Intent(context,SystemActivity.class);
                    intent.putExtra("chapterData",chapter);
                    String string = v.getTag().toString();
                    intent.putExtra("position",string);
                    context.startActivity(intent);

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
            Intent intent = new Intent(context,ShowSystemItemActivity.class);
            intent.putExtra("data",flowItem);
            context.startActivity(intent);
        }catch (ClassCastException e){
            LogUtil.d("日志:exception","转化失败");
            return;
        }


    }
}
