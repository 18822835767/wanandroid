package com.example.sorena.wanandroidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.SearchResultListAdapter;
import com.example.sorena.wanandroidapp.bean.SearchResult;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.ApiConstants;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JudgeUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.FloatingButtonLayout;
import com.example.sorena.wanandroidapp.widget.SystemBarLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.delHTMLTag;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

/**
 * 展示搜索结果的活动
 */
public class ShowResultActivity extends AppCompatActivity {

    private SystemBarLayout mSystemBarLayoutTopBar;
    private SwipeRefreshLayout mSwipeRefreshLayoutRefresh;
    private ListView mListViewShowResult;
    private SearchResultListAdapter mResultListAdapter;
    private FloatingButtonLayout mFbToTop;
    private TextView mMessageTextView;
    private List<SearchResult> mResults;
    private String mData;
    private int mNextPage = 0;
    private int mMaxPage = 10;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        ViewUtil.cancelActionBar(this);
        Intent intent = getIntent();
        mData = intent.getStringExtra("data");
        LogUtil.d("日志:","搜索关键词为:" + mData);
        initView();
        initData();

    }




    private void initView(){
        mSystemBarLayoutTopBar = (SystemBarLayout) findViewById(R.id.showResultActivity_SystemBarLayout_topBar);
        mSwipeRefreshLayoutRefresh = (SwipeRefreshLayout) findViewById(R.id.showResultActivity_SwipeRefreshLayout_refresh);
        mListViewShowResult = (ListView) findViewById(R.id.showResultActivity_listView_showResult);
        mMessageTextView = findViewById(R.id.mySystemBar_textView_message);
        mFbToTop = findViewById(R.id.showResultActivity_fb_toTop);
        mFbToTop.setToTopListView(mListViewShowResult);
        mMessageTextView.setText(mData);

        mListViewShowResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mListViewShowResult.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {

                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mListViewShowResult.getChildAt(mListViewShowResult.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewShowResult.getHeight()) {
                        if (mIsLoading){
                            LogUtil.d("日志","正在加载....");
                            return;
                        }else {
                            LogUtil.d("日志","准备加载下一页");
                            mIsLoading = true;
                        }
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        mSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                new Thread(()->{
                    try {Thread.sleep(5000);}
                    catch (Exception e){ LogUtil.e("日志:","异常:" + e.getMessage()); }
                    if (mSwipeRefreshLayoutRefresh.isRefreshing()){
                        mSwipeRefreshLayoutRefresh.setRefreshing(false);
                        runOnUiThread(()->{Toast.makeText(ShowResultActivity.this,"哦豁,可能木有网络了哦",Toast.LENGTH_SHORT).show();});
                    }
                }).start();
            }
        });
        mListViewShowResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult result = (SearchResult)(mResultListAdapter.getItem(position));
                Intent intent = new Intent(ShowResultActivity.this,WebActivity.class);
                intent.putExtra("url",result.getLink());
                startActivity(intent);
            }
        });
    }

    private void refreshData(){
        if (mResultListAdapter == null){
            initData();
            return;
        }
        mNextPage = 0;
        mResultListAdapter.clearData();
        loadMoreData();
    }




    private void initData(){

        User user = SharedPreferencesHelper.getUserData();
        HttpUtil.sendHttpPostRequestWithCookie(ApiConstants.SearchAddress,  new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()},new String[]{"k"}, new String[]{mData}, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {

                LogUtil.d("日志","response:" + response);
                mResults = parseData(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResultListAdapter = new SearchResultListAdapter(ShowResultActivity.this,R.layout.search_result_item, mResults);
                        mListViewShowResult.setAdapter(mResultListAdapter);
                        mNextPage++;
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void loadMoreData(){

        if (mNextPage > mMaxPage){
            mIsLoading = false;
            return;
        }
        User user = SharedPreferencesHelper.getUserData();
        HttpUtil.sendHttpPostRequestWithCookie(ApiConstants.SearchAddressFirstHalf + mNextPage + ApiConstants.SearchAddressSecondHalf, new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()}, new String[]{"k"}, new String[]{mData}, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.d("日志","response:" + response);
                mResults = parseData(response);
                runOnUiThread(()->{
                    mResultListAdapter.addData(mResults);
                    mNextPage++;
                    mSwipeRefreshLayoutRefresh.setRefreshing(false);
                    mIsLoading  = false;
                });

            }
            @Override
            public void onError(Exception e) {
                mIsLoading  = false;
            }
        });
    }


    private List<SearchResult> parseData(String response){

        String data = getValue("data",response,new String[]{});
        String datas = getValue("datas",data,new String[]{});
        String pageCount = getValue("pageCount",data,new String[]{});
        mMaxPage = Integer.parseInt(pageCount) - 1;
        Map<String,List<String>> dataMap = getMapInArray(datas,new String[]{"author","chapterName","link","superChapterName","title","niceDate","id","collect"});
        List<String> authors = dataMap.get("author");
        List<String> chapterNames = dataMap.get("chapterName");
        List<String> links = dataMap.get("link");
        List<String> superChapters = dataMap.get("superChapterName");
        List<String> titles = dataMap.get("title");
        List<String> ids = dataMap.get("id");
        List<String> dates = dataMap.get("niceDate");
        List<String> collects = dataMap.get("collect");
        List<SearchResult> results = new LinkedList<>();
        LogUtil.d("日志:collect:",collects.toString());
        LogUtil.d("日志:response",response);
        if (JudgeUtil.dataContainsNull(authors,chapterNames,links,superChapters,titles,ids,dates,collects)){
            return results;
        }
        for (int i = 0; i < authors.size() ; i++){
            results.add(new SearchResult(authors.get(i),chapterNames.get(i),superChapters.get(i),delHTMLTag(titles.get(i)),Integer.parseInt(ids.get(i)),links.get(i),Boolean.parseBoolean(collects.get(i)),dates.get(i)));
        }
        return results;
    }

}
