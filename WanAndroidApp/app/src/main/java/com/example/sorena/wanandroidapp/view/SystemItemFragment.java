package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.SystemArticleAdapter;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.widget.FloatingButtonLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

/**
 * 用于展示体系chapter的viewPager的一个碎片
 */
public class SystemItemFragment extends BaseFragment
{
    private FlowItem mFlowItem;
    private Integer mMaxPage = 1;
    private Integer mNextPage = 0;
    private SystemArticleAdapter mSystemArticleBaseAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayoutRefresh;
    private FloatingButtonLayout systemItemFbToTop;
    private ListView mListViewShowItem;
    private AppCompatActivity mActivity;
    private boolean mIsLoading = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity)context;
    }

    public static SystemItemFragment getInstance(FlowItem flowItem){

        SystemItemFragment fragment = new SystemItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("flowItem",flowItem);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system_item,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        Bundle bundle = getArguments();
        if (bundle != null){
            mMaxPage = 1;
            mNextPage = 0;
            mSystemArticleBaseAdapter = null;
            mFlowItem  = (FlowItem)bundle.getSerializable("flowItem");
        }
        loadNextPageData();
    }

    private void initView() {
        mSwipeRefreshLayoutRefresh = getView().findViewById(R.id.showSystemItemFragment_SwipeRefreshLayout_refresh);
        mListViewShowItem = getView().findViewById(R.id.showSystemItemFragment_listView_showItem);
        systemItemFbToTop = getView().findViewById(R.id.systemItem_fb_toTop);
        systemItemFbToTop.setToTopListView(mListViewShowItem);
        mListViewShowItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mListViewShowItem.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        mSwipeRefreshLayoutRefresh.setEnabled(true);
                    }else {
                        mSwipeRefreshLayoutRefresh.setEnabled(false);
                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mListViewShowItem.getChildAt(mListViewShowItem.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewShowItem.getHeight()) {                   loadNextPageData();
                    }
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        mSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNextPage = 0;
                mSystemArticleBaseAdapter.clearData();
                loadNextPageData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (mSwipeRefreshLayoutRefresh.isRefreshing()){
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayoutRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        mListViewShowItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                if (o instanceof Article){
                    Article article = (Article)o;
                    Intent intent = new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("url",article.getLink());
                    intent.putExtra("title",article.getTitle());
                    startActivity(intent);
                }
            }
        });
    }


    private void loadNextPageData(){

        if (mNextPage > mMaxPage){
            return;
        }
        if (mIsLoading){
            return;
        }
        mIsLoading  = true;
        String url = "https://www.wanandroid.com/article/list/" + mNextPage+  "/json?cid=" + mFlowItem.getId();
        LogUtil.d("日志:发送http",url);
        User user = SharedPreferencesHelper.getUserData();
        HttpUtil.sendHttpGetRequestWithCookie(url,
                new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()},
                new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        //LogUtil.d("日志:返回response" , response);
                        String data = getValue("data",response,new String[]{});
                        String allPage = getValue("pageCount",data,new String[]{});
                        mMaxPage = Integer.parseInt(allPage) - 1;
                        String datas = getValue("datas",data,new String[]{});
                        Map<String,List<String>> stringListMap = getMapInArray(datas,new String[]{"author","id","link","niceDate","title","chapterName","collect"});
                        List<String> authors = stringListMap.get("author");
                        List<String> ids = stringListMap.get("id");
                        List<String> links = stringListMap.get("link");
                        List<String> niceDates = stringListMap.get("niceDate");
                        List<String> titles = stringListMap.get("title");
                        List<String> chapterNames = stringListMap.get("chapterName");
                        List<String> collects = stringListMap.get("collect");
                        final List<Article> articles = new ArrayList<>();
                        if (authors == null || ids == null || links == null || niceDates == null || titles == null || chapterNames == null || collects == null){
                            LogUtil.d("日志:","某个数据为空");
                            return;
                        }
                        for (int i = 0; i < authors.size(); i++) {
                            Article article = new Article(authors.get(i),links.get(i),titles.get(i),niceDates.get(i),chapterNames.get(i),Integer.parseInt(ids.get(i)),Boolean.parseBoolean(collects.get(i)));
                            articles.add(article);
                        }
                        getActivity().runOnUiThread(()->{
                            if (mSystemArticleBaseAdapter == null)
                            {
                                mSystemArticleBaseAdapter = new SystemArticleAdapter(getActivity(),R.layout.article_item_layout,articles,null);
                                mListViewShowItem.setAdapter(mSystemArticleBaseAdapter);
                                LogUtil.d("日志:","   listView:   " + mListViewShowItem + "  adapter:  " + mSystemArticleBaseAdapter + "   fragment   " + this);
                            }else {
                                mSystemArticleBaseAdapter.addData(articles);
                            }
                        });
                        mNextPage++;
                        mIsLoading = false;
                    }
                    @Override
                    public void onError(Exception e) {
                        mIsLoading = false;
                    }
                });
    }
}
