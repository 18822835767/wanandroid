package com.example.sorena.wanandroidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.ProjectListItem;
import com.example.sorena.wanandroidapp.manager.CollectManager;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.NetImageLoad;
import com.example.sorena.wanandroidapp.util.ViewHolder;
import com.example.sorena.wanandroidapp.util.WebActivityUtil;
import com.example.sorena.wanandroidapp.view.WebActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于展示项目的图文列表
 *
 */
public class ProjectListItemAdapter extends BaseAdapter
{
    private List<ProjectListItem> mProjectListItems;
    private Map<String,Bitmap> mBitmapMap;
    private int mResourceId;
    private Context mContext;


    public ProjectListItemAdapter(Context context, int resourceId, final List<ProjectListItem> projectListItems) {
        this.mProjectListItems = projectListItems;
        this.mResourceId = resourceId;
        this.mBitmapMap = new HashMap<>();
        this.mContext = context;
        CollectManager.getInstance().addToCollectSetByProjectItem(projectListItems);
    }

    @Override
    public int getCount() {
        if (mProjectListItems == null){
            return 0;
        }else {
            return mProjectListItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mProjectListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //密恐福利
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent,mResourceId,position);
        ProjectListItem item = mProjectListItems.get(position);
        ((TextView)(viewHolder.getView(R.id.project_textView_showAuthor))).setText(mProjectListItems.get(position).getAuthor());
        ((TextView)(viewHolder.getView(R.id.project_textView_showDescription))).setText(mProjectListItems.get(position).getDescription());
        ((TextView)(viewHolder.getView(R.id.project_textView_showTime))).setText(mProjectListItems.get(position).getDate());
        ((TextView)(viewHolder.getView(R.id.project_textView_showTitle))).setText(mProjectListItems.get(position).getTitle());
        CollectManager.getInstance().setCollectImageView((Activity)mContext,((viewHolder.getView(R.id.project_imageView_showCollect))),item.getId());
        final String downLoadURL = item.getPictureLink();
        ((viewHolder.getView(R.id.project_imageView_showProjectPicture))).setTag(downLoadURL);
        if (mBitmapMap.get(downLoadURL) == null){
            new NetImageLoad().downloadImage(((viewHolder.getView(R.id.project_imageView_showProjectPicture))),downLoadURL,mBitmapMap);
            ((ImageView)(viewHolder.getView(R.id.project_imageView_showProjectPicture))).setImageResource(R.drawable.pic_project_default);
        }else {
            ((ImageView)(viewHolder.getView(R.id.project_imageView_showProjectPicture))).setImageBitmap(mBitmapMap.get(downLoadURL));
        }
        viewHolder.getConvertView().setOnClickListener((v)->{
            WebActivityUtil.startWebActivity(mContext,item.getProjectLink());
        });
        return viewHolder.getConvertView();
    }


    public void addData(List<ProjectListItem> items){
        mProjectListItems.addAll(items);
        CollectManager.getInstance().addToCollectSetByProjectItem(mProjectListItems);
        notifyDataSetChanged();
    }

    public void clearData(){
        if (mProjectListItems != null){
            mProjectListItems.clear();
        }
        notifyDataSetChanged();
    }



}

//if (mBitmapMap.get(downLoadURL) == null){
//        new NetImageLoad(){
//@Override
//public void loadImage(ImageView imageView, Bitmap bitmap) {
//        if(imageView.getTag()!=null && imageView.getTag().equals(downLoadURL)){
//        ((AppCompatActivity)mContext).runOnUiThread(()->imageView.setImageBitmap(bitmap));
//        mBitmapMap.put(imageView.getTag().toString(),bitmap);
//        }
//
//        }
//        }.downloadImage(viewHolder.projectImageViewShowProjectPicture,downLoadURL);
//        viewHolder.projectImageViewShowProjectPicture.setImageResource(R.drawable.pic_project_default);


//    View view;
//    ViewHolder viewHolder;
//    ProjectListItem item = mProjectListItems.get(position);
//        if (convertView == null){
//
//                view = LayoutInflater.from(mContext).inflate(R.layout.project_list_item,parent,false);
//                viewHolder = new ViewHolder();
//                viewHolder.projectImageViewShowProjectPicture = view.findViewById(R.id.project_imageView_showProjectPicture);
//                viewHolder.projectTextViewShowTitle = view.findViewById(R.id.project_textView_showTitle);
//                viewHolder.projectTextViewShowDescription = view.findViewById(R.id.project_textView_showDescription);
//                viewHolder.projectImageViewShowCollect = view.findViewById(R.id.project_imageView_showCollect);
//                viewHolder.projectTextViewShowTime = view.findViewById(R.id.project_textView_showTime);
//                viewHolder.projectTextViewShowAuthor = view.findViewById(R.id.project_textView_showAuthor);
//                view.setTag(viewHolder);
//
//                }else {
//
//                view = convertView;
//                viewHolder = (ViewHolder)(view.getTag());
//                }
//                viewHolder.projectTextViewShowAuthor.setText(mProjectListItems.get(position).getAuthor());
//                viewHolder.projectTextViewShowDescription.setText(mProjectListItems.get(position).getDescription());
//                viewHolder.projectTextViewShowTime.setText(mProjectListItems.get(position).getDate());
//                viewHolder.projectTextViewShowTitle.setText(mProjectListItems.get(position).getTitle());
//                if (CollectManager.getInstance().isCollect(item.getId())){
//                viewHolder.projectImageViewShowCollect.setImageResource(R.drawable.ic_collect_selected);
//                viewHolder.projectImageViewShowCollect.setTag(R.drawable.ic_collect_selected);
//                }else {
//                viewHolder.projectImageViewShowCollect.setImageResource(R.drawable.ic_collect_normal);
//                viewHolder.projectImageViewShowCollect.setTag(R.drawable.ic_collect_normal);
//                }
//                viewHolder.projectImageViewShowCollect.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        switch (v.getId()){
//        case R.id.project_imageView_showCollect:
//        try {
//        ImageView imageView = (ImageView)v;
//        if (imageView.getTag().equals(R.drawable.ic_collect_normal)){
//        CollectManager.getInstance().addCollect(item.getId(),imageView,(Activity) mContext);
//        }else {
//        CollectManager.getInstance().cancelCollect(item.getId(),imageView,(Activity) mContext);
//        }
//        }catch (ClassCastException e){
//        e.printStackTrace();
//        LogUtil.e("日志:SearchResultListAdapter:警告","不能强制转化");
//        }
//        }
//        }
//        });
//final String downLoadURL = item.getPictureLink();
//        viewHolder.projectImageViewShowProjectPicture.setTag(downLoadURL);
//        if (mBitmapMap.get(downLoadURL) == null){
//        new NetImageLoad().downloadImage((Activity)mContext, viewHolder.projectImageViewShowProjectPicture,downLoadURL,mBitmapMap);
//        viewHolder.projectImageViewShowProjectPicture.setImageResource(R.drawable.pic_project_default);
//        }else {
//        viewHolder.projectImageViewShowProjectPicture.setImageBitmap(mBitmapMap.get(downLoadURL));
//        }
//        view.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(mContext,WebActivity.class);
//        intent.putExtra("url",item.getProjectLink());
//        mContext.startActivity(intent);
//        }
//        });
//
//        return view;

//    class ViewHolder{
//
//        ImageView projectImageViewShowProjectPicture;
//        TextView projectTextViewShowTitle;
//        TextView projectTextViewShowDescription;
//        ImageView projectImageViewShowCollect;
//        TextView projectTextViewShowTime;
//        TextView projectTextViewShowAuthor;
//    }
