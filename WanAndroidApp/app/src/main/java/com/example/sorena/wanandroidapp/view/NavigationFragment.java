package com.example.sorena.wanandroidapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.NavArticleListAdapter;
import com.example.sorena.wanandroidapp.adapter.NavListItemAdapter;
import com.example.sorena.wanandroidapp.bean.NavListItem;
import com.example.sorena.wanandroidapp.dao.NavigationDao;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.widget.RefreshLayout;

import java.util.List;

/**
 * 导航碎片
 */
public class NavigationFragment extends BaseFragment implements RefreshLayout.refreshListener {

    private ListView mListViewShowData;
    private ListView mListViewShowFlowData;
    private NavListItemAdapter mNavListItemAdapter;
    private NavArticleListAdapter mNavArticleListAdapter;
    private List<NavListItem> mNavListItems;
    private RefreshLayout mRefreshLayoutRefresh;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        loadData();
    }

    void initView(){
        if (getView() == null){
            return;
        }
        mListViewShowData = getView().findViewById(R.id.MainActivity_listView_showData);
        mListViewShowFlowData = getView().findViewById(R.id.MainActivity_listView_showFlowData);
        mRefreshLayoutRefresh = getView().findViewById(R.id.navFragment_refreshLayout_refresh);
        mRefreshLayoutRefresh.setRefreshListener(this);
        mListViewShowData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (mNavListItemAdapter != null){
                    mNavListItemAdapter.setSelected(position);
                    mListViewShowFlowData.setSelection(position);
                }
            }
        });
        mListViewShowFlowData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //右边滑动时设置左边的选中样式
//                if (firstVisibleItem != mPrevPoition){
//                    if (!isSelecting){
//                        if (firstVisibleItem < mNavListItemAdapter.getCount()){
//                            mNavListItemAdapter.setSelected(firstVisibleItem);
//                        }
//                        mListViewShowData.smoothScrollToPosition(firstVisibleItem);
//                        mPrevPoition = firstVisibleItem;
//                    }

            }
        });

    }


    private void loadData(){
        NavigationDao.loadNavigationData(getActivity(), new NavigationDao.NavigationDataLoadingListener() {
            @Override
            public void onFinish(List<NavListItem> navFlowItemList) {
                mNavListItems = navFlowItemList;
                if (getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(()->{
                    mNavListItemAdapter = new NavListItemAdapter(getActivity(),R.layout.item_nav_list, mNavListItems);
                    mListViewShowData.setAdapter(mNavListItemAdapter);
                    mNavArticleListAdapter = new NavArticleListAdapter(getActivity(),R.layout.item_nav_article_list,mNavListItems);
                    mListViewShowFlowData.setAdapter(mNavArticleListAdapter);
                    mRefreshLayoutRefresh.setVisibility(View.GONE);
                    mListViewShowFlowData.setVisibility(View.VISIBLE);
                    mListViewShowData.setVisibility(View.VISIBLE);
                });
            }
            @Override
            public void onError(Exception e) {
                LogUtil.e("日志:",e.getMessage());
                mListViewShowData.setVisibility(View.GONE);
                mListViewShowFlowData.setVisibility(View.GONE);
                mRefreshLayoutRefresh.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void refresh() {
        loadData();
        LogUtil.d("日志:","loadData执行");
    }
}






//HttpUtil.sendHttpRequest("https://www.wanandroid.com/navi/json", new HttpUtil.HttpCallBackListener() {
//@Override
//public void onFinish(String response) {
//        String data = JSONUtil.getValue("data",response,new String[]{});
//        Map<String,List<String>> dataMap = JSONUtil.getMapInArray(data,new String[]{"articles","name"});
//        List<String> articles = dataMap.get("articles");
//        if (articles == null){
//        LogUtil.d("日志:articles","null");
//        return;
//        }
//        List<String> names = dataMap.get("name");
//        if (names == null) {return;}
//        for (int i = 0; i < names.size(); i++){
//        Map<String,List<String>> flowItemDataMap =JSONUtil.getMapInArray(articles.get(i),new String[]{"link","title"});
//        List<String> links = flowItemDataMap.get("link");
//        List<String> titles = flowItemDataMap.get("title");
//        if (links == null || titles == null){
//        LogUtil.d("日志:links/titles","null");
//        return;
//        }
//        List<NavFlowItem> flowItems = new LinkedList<>();
//        int size = Math.min(links.size(),titles.size());
//        for (int j = 0; j < size; j++) {
//        flowItems.add(new NavFlowItem(titles.get(j),links.get(j)));
//        }
//        mNavListItems.add(new NavListItem(names.get(i),false,flowItems));
//        }
//        if (getActivity() == null){
//        return;
//        }
//        getActivity().runOnUiThread(new Runnable() {
//@Override
//public void run() {
//        mNavListItemAdapter = new NavListItemAdapter(getActivity(),R.layout.item_nav_list, mNavListItems);
//        mListViewShowData.setAdapter(mNavListItemAdapter);
//        mNavArticleListAdapter = new NavArticleListAdapter(getActivity(),R.layout.item_nav_article_list,mNavListItems);
//        mListViewShowFlowData.setAdapter(mNavArticleListAdapter);
//        }
//        });
//        }
//@Override
//public void onError(Exception e) {
//        LogUtil.e("日志:",e.getMessage());
//        }
//        });