package com.example.sorena.wanandroidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.BaseArticleAdapter;
import com.example.sorena.wanandroidapp.adapter.LooperPagerAdapter;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.dao.HomeDao;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.widget.FloatingButtonLayout;
import com.example.sorena.wanandroidapp.widget.MyViewPager;
import com.example.sorena.wanandroidapp.widget.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页碎片
 */
public class HomeFragment extends BaseFragment implements
        MyViewPager.OnViewPagerTouchListener, MyViewPager.OnPageChangeListener, View.OnClickListener,
        MyViewPager.OpenWeb, RefreshLayout.refreshListener {

    //轮播图viewPager
    private MyViewPager mLoopViewPager;
    //轮播图适配器
    private LooperPagerAdapter adapter;
    //轮播图的信息,图片链接,点击打开的链接
    private List<String> mMessage;
    private List<String> mURLs;
    private List<String> mWebURLs;
    //handle
    private Handler mHandler = new Handler();
    //判断轮播图是否在被触摸的标示量
    private boolean isTouch = false;
    //标识轮播图是否加载完成
    private Boolean mDataLoadingFinish = false;
    //轮播图左下角的信息
    private TextView mLoopTextViewShowMessage;
    //轮播图右下角的信息
    private TextView mLoopTextViewShowPosition;
    //普通文章数据列表
    private List<Article> mNormalArticleList = null;
    //置顶文章数据列表
    private List<Article> mToppingArticleList = null;
    //列表控件
    private ListView mHomeListViewShowArticle;
    //列表的适配器
    private BaseArticleAdapter mArticleAdapter = null;
    //标识普通文章是否加载完毕
    private Boolean mNormalIsLoadingFinish = false;
    //标识置顶文章是否加载完毕
    private Boolean mToppingIsLoadingFinish = false;
    private Integer nextLoadingNormalPage = 1;
    //最外层包着的用于下拉刷新的布局
    private SwipeRefreshLayout mHomeSwipeRefreshLayoutRefreshData;
    private View mLoopView;
    private RefreshLayout mMainActivityRefresh;
    private FloatingButtonLayout mFloatButtonToTop;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoopView();
        initListView();
        setLoopData();
        getData();
    }

    private void initLoopView(){

        mLoopView = LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_my,null);
        mLoopViewPager = mLoopView.findViewById(R.id.loop_viewPager);
        adapter = new LooperPagerAdapter(()->{
            if (getActivity() == null){return;}
            getActivity().runOnUiThread(() ->{
                    mDataLoadingFinish = true;
                    mLoopViewPager.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
            });
        });
        mLoopViewPager.setAdapter(adapter);
        mLoopViewPager.setViewPagerTouchLister(this);
        mLoopViewPager.addOnPageChangeListener(this);
        mLoopViewPager.setOnClickListener(this);
        mLoopViewPager.setOpenWeb(this);
        mLoopViewPager.setVisibility(View.INVISIBLE);
        mLoopTextViewShowMessage = mLoopView.findViewById(R.id.loop_textView_showMessage);
        mLoopTextViewShowPosition = mLoopView.findViewById(R.id.loop_textView_showPosition);
    }

    private void initListView(){

        if (getActivity() == null){
            return;
        }
        mHomeListViewShowArticle = getActivity().findViewById(R.id.home_listView_showArticle);
        mHomeSwipeRefreshLayoutRefreshData = getActivity().findViewById(R.id.home_swipeRefreshLayout_refreshData);
        mFloatButtonToTop = getActivity().findViewById(R.id.homeFragment_fb_toTop);
        mMainActivityRefresh =  getActivity().findViewById(R.id.mainActivity_refresh);
        mMainActivityRefresh.setRefreshListener(this);
        mHomeListViewShowArticle.setVisibility(View.GONE);
        mHomeListViewShowArticle.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
//                LogUtil.d("日志:firstVisibleItem", ""+ firstVisibleItem);
                if (firstVisibleItem == 0) {
                    //得到能显示的第一个项
                    View firstVisibleItemView = mHomeListViewShowArticle.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0)
                    {
                        mHomeSwipeRefreshLayoutRefreshData.setEnabled(true);
                    }
                    else {
                        mHomeSwipeRefreshLayoutRefreshData.setEnabled(false);
                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mHomeListViewShowArticle.getChildAt(mHomeListViewShowArticle.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mHomeListViewShowArticle.getHeight()) {
                        loadNextPageNormalData();
                    }
                }
                //其他时候设置不可刷新
                else {
                    mHomeSwipeRefreshLayoutRefreshData.setEnabled(false);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        mHomeListViewShowArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                if (o instanceof Article){
                    Article article = (Article)o;
                    Intent intent = new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("url",article.getLink());
                    startActivity(intent);
                }
            }
        });

        mHomeListViewShowArticle.addHeaderView(mLoopView);
        mHomeSwipeRefreshLayoutRefreshData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.d("日志:","onRefresh执行");
                nextLoadingNormalPage = 1;
                if (mArticleAdapter == null){
                    return;
                }
                mArticleAdapter.clearData();
                refreshToppingData();
                loadNextPageNormalData();
            }
        });

        mFloatButtonToTop.setListener(()->{
            if (mHomeListViewShowArticle.getVisibility() == View.VISIBLE){
                mHomeListViewShowArticle.smoothScrollToPosition(0);
            }
        });
    }

    private void setLoopData(){

        HomeDao.getLoopingData(getActivity(), new HomeDao.LoopingDataLoadingListener() {
            @Override
            public void onFinish(List<String> urls, List<String> messages, List<String> webUrls) {
                if (getActivity() == null){
                    return;
                }

                getActivity().runOnUiThread(()->{
                    mURLs = urls;
                    mMessage = messages;
                    mWebURLs = webUrls;
                    adapter.setmURLs(mURLs);
                    adapter.notifyDataSetChanged();
                });

            }

            @Override
            public void onError(Exception e) {
                LogUtil.d("日志:LoopingDataLoadingListener",e.getMessage());
            }
        });


    }

    private void noticeDataLoadingStatusChange(){
        if (mNormalIsLoadingFinish && mToppingIsLoadingFinish){
            setArticleListData();
        }
    }

    private void setArticleListData(){

        if (getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mArticleAdapter != null){
                    mArticleAdapter.addNormalArticleData(mNormalArticleList);
                    return;
                }
                mArticleAdapter = new BaseArticleAdapter(getActivity(),R.layout.article_item_layout, mNormalArticleList,mToppingArticleList);
                mHomeListViewShowArticle.setAdapter(mArticleAdapter);
                mHomeListViewShowArticle.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadNextPageNormalData(){

        HomeDao.loadNormalData(getActivity(),nextLoadingNormalPage,new HomeDao.NormalDataLoadingListener() {
            @Override
            public void onFinish(List<Article> articles) {
                if (getActivity() == null){
                    return;
                }
                if (mNormalArticleList == null){
                    mNormalArticleList = new ArrayList<>();
                }
                if (articles != null){
                    mNormalArticleList.clear();
                    mNormalArticleList.addAll(articles);
                }
                mNormalIsLoadingFinish = true;
                noticeDataLoadingStatusChange();
                nextLoadingNormalPage++;
                mHomeSwipeRefreshLayoutRefreshData.setRefreshing(false);
                getActivity().runOnUiThread(()->{
                    mMainActivityRefresh.setVisibility(View.GONE);
                });
            }
            @Override
            public void onError(Exception e) {
                if (mNormalIsLoadingFinish){
                    return;
                }
                if (getActivity() != null)
                {
                    getActivity().runOnUiThread(()->{
                        mMainActivityRefresh.setVisibility(View.VISIBLE);
                        mHomeSwipeRefreshLayoutRefreshData.setRefreshing(false);
                    });
                }
            }
        });
    }


    private void getData(){
        loadNextPageNormalData();
        loadToppingData();
    }

    private void loadToppingData(){
        HomeDao.getToppingData(getActivity(), new HomeDao.ToppingDataLoadingListener() {
            @Override
            public void onFinish(List<Article> articles) {
                if (getActivity() == null){return;}
                getActivity().runOnUiThread(()->{
                    mToppingArticleList = articles;
                    mToppingIsLoadingFinish = true;
                    noticeDataLoadingStatusChange();
                });
            }
            @Override
            public void onError(Exception e) {
                mToppingIsLoadingFinish = false;
            }
        });
    }

    private void refreshToppingData(){
        HomeDao.getToppingData(getActivity(), new HomeDao.ToppingDataLoadingListener() {
            @Override
            public void onFinish(List<Article> articles) {
                if (getActivity() == null){return;}
                getActivity().runOnUiThread(()->{
                    if (mArticleAdapter == null){
                        LogUtil.d("日志mArticleAdapter:","null");
                        return;
                    }
                    mArticleAdapter.resetToppingArticle(articles);
                });
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(loopTask);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(loopTask);
    }

    private Runnable loopTask = new Runnable() {
        @Override
        public void run() {
            if (!isTouch && mDataLoadingFinish){
                if (mURLs != null){
                    int currentItem = mLoopViewPager.getCurrentItem();
                    if (currentItem <= 50){
                        currentItem = mURLs.size() * 100 -1 + currentItem;
                        mLoopViewPager.setCurrentItem(++currentItem , false);
                    }else {
                        mLoopViewPager.setCurrentItem(++currentItem , true);
                    }
                }
            }
            if (mDataLoadingFinish){
                mHandler.postDelayed(this,4000);
            }else {
                mHandler.postDelayed(this,500);
            }
        }
    };



    @Override
    public void onPagerTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    //OnPageChangeListener的方法
    @Override
    public void onPageScrolled(int i, float v, int i1) {}

    @Override
    public void onPageSelected(int i) {
        mLoopTextViewShowMessage.setText(mMessage.get(i % mMessage.size()));
        mLoopTextViewShowPosition.setText( (i % mMessage.size()+1) + "/" + mMessage.size());
    }

    @Override
    public void onPageScrollStateChanged(int i) {}

    //View.OnClickListener相关方法
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            default:
                break;
        }
    }

    //MyViewPager.OpenWeb
    @Override
    public void openWeb() {
        Intent intent = new Intent(getActivity(),WebActivity.class);
        if (mWebURLs == null){
            return;
        }
        intent.putExtra("url",mWebURLs.get(mLoopViewPager.getCurrentItem() % mWebURLs.size()));
        startActivity(intent);
    }

    @Override
    public void refresh() {
        nextLoadingNormalPage = 1;
        setLoopData();
        loadNextPageNormalData();
        loadToppingData();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mHomeSwipeRefreshLayoutRefreshData.isRefreshing()){
                    mHomeSwipeRefreshLayoutRefreshData.setRefreshing(false);
                }
            }
        }, 5000);
    }
}


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                User user = SharedPreferencesHelper.getUserData();
//                HttpUtil.sendHttpGetRequestWithCookie("https://www.wanandroid.com/article/list/"
//                                + nextLoadingNormalPage + "/json",
//                        new String[]{"loginUserName","loginUserPassword"},
//                        new String[]{user.getUserName(),user.getUserPassword()},
//                        new HttpUtil.HttpCallBackListener() {
//                    @Override
//                    public void onFinish(String response) {
//
//                        if (getActivity() == null){
//                            return;
//                        }
//                        List<Article> articles = HomeDao.parseNormalArticleData(response);
//                        if (mNormalArticleList == null){
//                            mNormalArticleList = new ArrayList<>();
//                        }
//                        if (articles != null){
//                            mNormalArticleList.clear();
//                            mNormalArticleList.addAll(articles);
//                        }
//                        mNormalIsLoadingFinish = true;
//                        noticeDataLoadingStatusChange();
//                        nextLoadingNormalPage++;
//                        mHomeSwipeRefreshLayoutRefreshData.setRefreshing(false);
//                        getActivity().runOnUiThread(()->{
//                            mMainActivityRefresh.setVisibility(View.GONE);
//                        });
//                    }
//                    @Override
//                    public void onError(Exception e) {
//                        e.printStackTrace();
//                        if (mNormalIsLoadingFinish){
//                            return;
//                        }
//                        if (getActivity() != null)
//                        {
//                            getActivity().runOnUiThread(()->{
//                                mMainActivityRefresh.setVisibility(View.VISIBLE);
//                                mHomeSwipeRefreshLayoutRefreshData.setRefreshing(false);
//                            });
//                        }
//                    }
//                });
//
//            }
//        }).start();

//        HttpUtil.sendHttpRequest("https://www.wanandroid.com/banner/json", new HttpUtil.HttpCallBackListener() {
//            @Override
//            public void onFinish(String response) {
//                String data = JSONUtil.getValue("data",response,new String[]{});
//                String[] keys = {"desc","imagePath","title","url"};
//                Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,keys);
//                mURLs = stringListMap.get("imagePath");
//                mMessage = stringListMap.get("title");
//                mWebURLs = stringListMap.get("url");
//                adapter.setmURLs(mURLs);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Exception e) {
//                e.printStackTrace();
//            }
//        });