package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.MainActivity;
import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.BaseArticleAdapter;
import com.example.sorena.wanandroidapp.adapter.LooperPagerAdapter;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.util.BaseFragment;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LazyFragment;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends LazyFragment implements
        MyViewPager.OnViewPagerTouchListener, MyViewPager.OnPageChangeListener , View.OnClickListener , MyViewPager.OpenWeb{

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




    @Override
    protected void lazyLoad() {
        setLoopData();
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoopView();
        initListView();
    }

    private void initLoopView(){

        mLoopView = LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_my,null);
        mLoopViewPager = mLoopView.findViewById(R.id.loop_viewPager);
        adapter = new LooperPagerAdapter(()->{
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

        mHomeListViewShowArticle = getActivity().findViewById(R.id.home_listView_showArticle);
        mHomeListViewShowArticle.setVisibility(View.GONE);
        mHomeListViewShowArticle.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mHomeListViewShowArticle.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        mHomeSwipeRefreshLayoutRefreshData.setEnabled(true);
                        LogUtil.d("日志", "##### 滚动到顶部 #####");
                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mHomeListViewShowArticle.getChildAt(mHomeListViewShowArticle.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mHomeListViewShowArticle.getHeight()) {
                        LogUtil.d("日志", "##### 滚动到底部 准备加载下一页######");
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
        mHomeSwipeRefreshLayoutRefreshData = getActivity().findViewById(R.id.home_swipeRefreshLayout_refreshData);
        mHomeSwipeRefreshLayoutRefreshData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.d("日志:","onRefresh执行");
                nextLoadingNormalPage = 1;
                if (mArticleAdapter == null){
                    return;
                }
                mArticleAdapter.clearData();
                mArticleAdapter.resetToppingArticle(mToppingArticleList);
                loadNextPageNormalData();
            }
        });


    }



    private void setLoopData(){

        HttpUtil.sendHttpRequest("https://www.wanandroid.com/banner/json", new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                String data = JSONUtil.getValue("data",response,new String[]{});
                String[] keys = {"desc","imagePath","title","url"};
                Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,keys);
                mURLs = stringListMap.get("imagePath");
                mMessage = stringListMap.get("title");
                mWebURLs = stringListMap.get("url");
                adapter.setURLs(mURLs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void noticeDataLoadingStatusChange(){
        if (mNormalIsLoadingFinish && mToppingIsLoadingFinish){
            setArticleListData();
        }
    }

    private void setArticleListData(){

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendHttpRequest("https://www.wanandroid.com/article/list/" + nextLoadingNormalPage + "/json", new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {

                        if (mNormalArticleList == null){
                            mNormalArticleList = new ArrayList<>();
                        }
                        List<Article> articles = parseData(response);
                        if (articles != null){
                            mNormalArticleList.clear();
                            mNormalArticleList.addAll(articles);
                        }
                        mNormalIsLoadingFinish = true;
                        noticeDataLoadingStatusChange();
                        nextLoadingNormalPage++;
                        mHomeSwipeRefreshLayoutRefreshData.setRefreshing(false);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }).start();

    }

    private List<Article> parseData(String jsonString){

        String data = JSONUtil.getValue("datas",jsonString,new String[]{"data"});
        Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,new String[]{"author","link","title","niceDate","chapterName","id"});
        List<String> authors = stringListMap.get("author");
        List<String> links = stringListMap.get("link");
        List<String> titles = stringListMap.get("title");
        List<String> niceDates = stringListMap.get("niceDate");
        List<String> chapterNames = stringListMap.get("chapterName");
        List<String> ids = stringListMap.get("id");
        ArrayList<Article> articles = new ArrayList<>();
        if (authors == null) return null;
        for (int i = 0; i < authors.size() ; i++) {
            articles.add(new Article(authors.get(i),links.get(i),titles.get(i),niceDates.get(i),chapterNames.get(i),Integer.parseInt(ids.get(i))));
        }
        return articles;
    }


    private void getData(){

        loadNextPageNormalData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendHttpRequest("https://www.wanandroid.com/article/top/json", new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        String data = JSONUtil.getValue("data",response,new String[]{});
                        System.out.println(data);
                        Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,new String[]{"author","link","title","niceDate","chapterName","id"});List<String> authors = stringListMap.get("author");
                        List<String> links = stringListMap.get("link");
                        List<String> titles = stringListMap.get("title");
                        List<String> niceDates = stringListMap.get("niceDate");
                        List<String> chapterNames = stringListMap.get("chapterName");
                        List<String> ids = stringListMap.get("id");
                        mToppingArticleList = new ArrayList<>();
                        if (authors == null) return;
                        for (int i = 0; i < authors.size() ; i++) {
                            mToppingArticleList.add(new Article(authors.get(i),links.get(i),titles.get(i),niceDates.get(i),chapterNames.get(i),Integer.parseInt(ids.get(i))));
                        }
                        mToppingIsLoadingFinish = true;
                        noticeDataLoadingStatusChange();
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }).start();

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
                    }
                    mLoopViewPager.setCurrentItem(++currentItem , true);
                    LogUtil.d("日志", ""  +  currentItem);
                }
            }
            mHandler.postDelayed(this,4000);
        }
    };

    @Override
    public void onPagerTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    //OnPageChangeListener的方法
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        LogUtil.d("日志","信息:" + mMessage.get(i % mMessage.size()) + "  位置:" + i);
        mLoopTextViewShowMessage.setText(mMessage.get(i % mMessage.size()));
        mLoopTextViewShowPosition.setText( (i % mMessage.size()+1) + "/" + mMessage.size());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }




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

}
