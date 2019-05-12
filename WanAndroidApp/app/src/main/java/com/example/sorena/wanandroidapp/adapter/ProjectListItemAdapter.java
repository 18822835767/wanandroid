package com.example.sorena.wanandroidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.ProjectListItem;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.NetImageLoad;
import com.example.sorena.wanandroidapp.view.WebActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectListItemAdapter extends BaseAdapter
{
    private List<ProjectListItem> projectListItems;
    private Map<String,Bitmap> bitmapMap;
    private int resourceId;
    private Context context;


    public ProjectListItemAdapter(Context context, int resourceId, final List<ProjectListItem> projectListItems) {
        this.projectListItems = projectListItems;
        this.resourceId = resourceId;
        this.bitmapMap = new HashMap<>();
        this.context = context;
        LogUtil.i("日志","创建适配器");
        //在这里加载也莫得用处......
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < projectListItems.size(); i++) {
//                    bitmapMap.put(projectListItems.get(i).getPictureLink(),PictureUtil.getBitmaps(projectListItems.get(i).getPictureLink()));
//                }
//            }
//        }).start();
    }

    @Override
    public int getCount() {
        if (projectListItems == null){
            return 0;
        }else {
            return projectListItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return projectListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        if (convertView == null){

            view = LayoutInflater.from(context).inflate(R.layout.project_list_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.projectImageViewShowProjectPicture = view.findViewById(R.id.project_imageView_showProjectPicture);
            viewHolder.projectTextViewShowTitle = view.findViewById(R.id.project_textView_showTitle);
            viewHolder.projectTextViewShowDescription = view.findViewById(R.id.project_textView_showDescription);
            viewHolder.projectImageViewShowCollect = view.findViewById(R.id.project_imageView_showCollect);
            viewHolder.projectTextViewShowTime = view.findViewById(R.id.project_textView_showTime);
            viewHolder.projectTextViewShowAuthor = view.findViewById(R.id.project_textView_showAuthor);
            view.setTag(viewHolder);

        }else {

            view = convertView;
            viewHolder = (ViewHolder)(view.getTag());
        }

        viewHolder.projectTextViewShowAuthor.setText(projectListItems.get(position).getAuthor());
        viewHolder.projectTextViewShowDescription.setText(projectListItems.get(position).getDescription());
        viewHolder.projectTextViewShowTime.setText(projectListItems.get(position).getDate());
        viewHolder.projectTextViewShowTitle.setText(projectListItems.get(position).getTitle());

        ProjectListItem item = projectListItems.get(position);
        final String downLoadURL = item.getPictureLink();
        viewHolder.projectImageViewShowProjectPicture.setTag(downLoadURL);
        if (bitmapMap.get(downLoadURL) == null){
            new NetImageLoad(){
                @Override
                public void loadImage(ImageView imageView, Bitmap bitmap) {
                    if(imageView.getTag()!=null && imageView.getTag().equals(downLoadURL)){
                        imageView.setImageBitmap(bitmap);
                        bitmapMap.put(imageView.getTag().toString(),bitmap);
                    }

                }
            }.downloadImage(viewHolder.projectImageViewShowProjectPicture,downLoadURL);
            viewHolder.projectImageViewShowProjectPicture.setImageResource(R.drawable.pic_project_default);
        }else {
            viewHolder.projectImageViewShowProjectPicture.setImageBitmap(bitmapMap.get(downLoadURL));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("url",item.getProjectLink());
                context.startActivity(intent);
            }
        });

        return view;
    }


    class ViewHolder{

        ImageView projectImageViewShowProjectPicture;
        TextView projectTextViewShowTitle;
        TextView projectTextViewShowDescription;
        ImageView projectImageViewShowCollect;
        TextView projectTextViewShowTime;
        TextView projectTextViewShowAuthor;
    }

    public void addData(List<ProjectListItem> items){
        projectListItems.addAll(items);
        notifyDataSetChanged();
    }

    public void clearData(){
        if (projectListItems != null){
            projectListItems.clear();
        }
        notifyDataSetChanged();
    }



}
