package com.example.sorena.wanandroidapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.ProjectListItemAdapter;
import com.example.sorena.wanandroidapp.bean.ProjectChapter;
import com.example.sorena.wanandroidapp.bean.ProjectListItem;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.ApiConstants;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.widget.FloatingButtonLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

/**
 * project碎片的viewPager的碎片
 */
public class ProjectViewPagerFragment extends BaseFragment
{

    private ProjectChapter mChapter;
    private SwipeRefreshLayout mProjectFragmentSwipeRefreshLayoutRefresh;
    private ListView mShowProjectItem;
    private FloatingButtonLayout mFbToTop;
    private ProjectListItemAdapter mItemAdapter;
    private int mMaxPage = 1;
    private int mNextLoadPage = 1;
    private List<ProjectListItem> mListItems;


    public static ProjectViewPagerFragment getInstance(ProjectChapter chapter){
        ProjectViewPagerFragment fragment = new ProjectViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("chapterData",chapter);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_viewpager,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProjectFragmentSwipeRefreshLayoutRefresh = view.findViewById(R.id.projectFragment_SwipeRefreshLayout_refresh);
        mShowProjectItem = view.findViewById(R.id.projectFragment_listView_showProjectItem);
        mFbToTop = view.findViewById(R.id.projectFragment_fb_toTop);
        Bundle bundle = getArguments();
        if (bundle != null){
            this.mChapter = (ProjectChapter) (bundle.getSerializable("chapterData"));
        }
        mShowProjectItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mShowProjectItem.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mShowProjectItem.getChildAt(mShowProjectItem.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mShowProjectItem.getHeight()) {
                        loadData();
                    }
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        mProjectFragmentSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNextLoadPage = 1;
                mItemAdapter.clearData();
                loadData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (mProjectFragmentSwipeRefreshLayoutRefresh.isRefreshing()){
                            if (getActivity() == null){
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"网络木有相应",Toast.LENGTH_SHORT).show();
                                    mProjectFragmentSwipeRefreshLayoutRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        mFbToTop.setToTopListView(mShowProjectItem);

        initData();
    }


    private void initData(){
        if (mChapter == null){
            return;
        }
        User user = SharedPreferencesHelper.getUserData();
        HttpUtil.sendHttpGetRequestWithCookie(ApiConstants.ProjectItemAddressFirst + 1 + ApiConstants.ProjectItemAddressSecond + mChapter.getId(),
                new String[]{"loginUserName","loginUserPassword"},
                new String[]{user.getUserName(),user.getUserPassword()},new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                if (getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListItems = parseData(response);
                        mItemAdapter = new ProjectListItemAdapter(getActivity(),R.layout.project_list_item, mListItems);
                        mShowProjectItem.setAdapter(mItemAdapter);
                    }
                });
                mNextLoadPage++;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void loadData(){

        if (mNextLoadPage > mMaxPage){
            return;
        }else {
            User user = SharedPreferencesHelper.getUserData();
            HttpUtil.sendHttpGetRequestWithCookie(ApiConstants.ProjectItemAddressFirst + mNextLoadPage + ApiConstants.ProjectItemAddressSecond + mChapter.getId(),
                    new String[]{"loginUserName","loginUserPassword"},
                    new String[]{user.getUserName(),user.getUserPassword()},
                    new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(final String response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListItems = parseData(response);
                            mItemAdapter.addData(mListItems);
                        }
                    });
                    mNextLoadPage++;
                    mProjectFragmentSwipeRefreshLayoutRefresh.setRefreshing(false);
                }

                @Override
                public void onError(Exception e) {

                }
            });

        }
    }



    private List<ProjectListItem> parseData(String response){

        String data = getValue("data", response,new String[]{});
        String maxPageIndex = getValue("pageCount",data,new String[]{});
        mMaxPage = Integer.parseInt(maxPageIndex);
        String datas =getValue("datas",data,new String[]{});
        Map<String,List<String>> dataMap = getMapInArray(datas,new String[]{"author","desc","envelopePic","id","link","title","niceDate","collect"});
        if (dataMap == null) return null;
        List<String> authors = dataMap.get("author");
        List<String> descriptions = dataMap.get("desc");
        List<String> pictures = dataMap.get("envelopePic");
        List<String> ids = dataMap.get("id");
        List<String> links = dataMap.get("link");
        List<String> titles = dataMap.get("title");
        List<String> dates = dataMap.get("niceDate");
        List<String> collects = dataMap.get("collect");
        if (authors == null || descriptions == null || pictures == null || ids == null || links ==  null || titles == null || dates == null || collects == null){
            return null;
        }
        List<ProjectListItem> list = new ArrayList<>();
        for (int i = 0; i < authors.size(); i++) {
            list.add(new ProjectListItem(pictures.get(i),links.get(i),titles.get(i),descriptions.get(i),dates.get(i),
                    authors.get(i),Integer.parseInt(ids.get(i)),Boolean.parseBoolean(collects.get(i))));
        }
        return list;
    }

}
