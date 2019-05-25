package com.example.sorena.wanandroidapp.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.SystemArticleAdapter;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.ApiConstants;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.FloatingButtonLayout;
import com.example.sorena.wanandroidapp.widget.SystemBarLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 展示收藏的活动
 */
public class ShowCollectActivity extends AppCompatActivity {

    private ListView mListViewShowCollects;
    private SwipeRefreshLayout mSwipeRefreshLayoutRefresh;
    private FloatingButtonLayout showCollectActivityFbToTop;
    private SystemBarLayout mSystemBarLayoutBar;
    private TextView nMessageTextView;
    private SystemArticleAdapter mArticleAdapter;
    private List<Article> mArticle;
    private User mUser;
    private int mNextPage = 0;
    private int mMaxPage = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_collect);
        ViewUtil.cancelActionBar(this);
        init();
        loadNextPageData();

    }

    private void init(){
        nMessageTextView = findViewById(R.id.mySystemBar_textView_message);
        mSwipeRefreshLayoutRefresh = findViewById(R.id.collect_SwipeRefreshLayout_refresh);
        showCollectActivityFbToTop = findViewById(R.id.showCollectActivity_fb_toTop);
        mSystemBarLayoutBar = findViewById(R.id.mySystemBar);
        nMessageTextView.setText("收藏");
        mUser = SharedPreferencesHelper.getUserData();
        mListViewShowCollects = findViewById(R.id.collect_listView_showItem);
        mArticleAdapter = new SystemArticleAdapter(this,R.layout.article_item_layout,new ArrayList<>(),new HashSet<>());
        mListViewShowCollects.setAdapter(mArticleAdapter);
        showCollectActivityFbToTop.setToTopListView(mListViewShowCollects);
        mListViewShowCollects.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mListViewShowCollects.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        mSwipeRefreshLayoutRefresh.setEnabled(true);
                    } else {
                        mSwipeRefreshLayoutRefresh.setEnabled(false);
                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mListViewShowCollects.getChildAt(mListViewShowCollects.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewShowCollects.getHeight()) {
                        loadNextPageData();
                        LogUtil.d("日志:ShowCollectActivity","加载下一页");
                    }
                }
                //其他时候设置不可刷新
                else {
                    mSwipeRefreshLayoutRefresh.setEnabled(false);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        mListViewShowCollects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowCollectActivity.this,WebActivity.class);
                Article article = (Article)(mArticleAdapter.getItem(position));
                intent.putExtra("url",article.getLink());
                startActivity(intent);
            }
        });

        mSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticleAdapter.clearData();
                mNextPage = 0;
                loadNextPageData();
                new Thread(()->{
                    try {
                        Thread.sleep(5000);
                        if (mSwipeRefreshLayoutRefresh.isRefreshing()){
                            runOnUiThread(()->Toast.makeText(ShowCollectActivity.this,"哦豁,可能莫得网络了",Toast.LENGTH_SHORT).show());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            }
        });

    }



    private void loadNextPageData(){
        if (mNextPage > mMaxPage){
            LogUtil.d("日志:ShowCollectActivity","mNextPage:" + mNextPage + "   mMaxPage:" + mMaxPage);
            return;
        }
        HttpUtil.sendHttpGetRequestWithCookie(ApiConstants.CollectListAddressFirstHalf + mNextPage + ApiConstants.CollectListAddressSecondHalf,
                new String[]{"loginUserName", "loginUserPassword"},
                new String[]{mUser.getUserName(), mUser.getUserPassword()},
                new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        mNextPage++;
                        LogUtil.d("日志:response",response);
                        mArticle = parseData(response);
                        runOnUiThread(()->{
                            mArticleAdapter.addData(mArticle);
                            mArticleAdapter.notifyDataSetChanged();
                            if (mSwipeRefreshLayoutRefresh.isRefreshing()){
                                mSwipeRefreshLayoutRefresh.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    private List<Article> parseData(String jsonString){
        String data = JSONUtil.getValue("datas",jsonString,new String[]{"data"});
        String max = JSONUtil.getValue("pageCount",jsonString,new String[]{"data"});
        mMaxPage = Integer.parseInt(max) - 1;
        Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,new String[]{"author","link","title","niceDate","chapterName","originId"});
        List<String> authors = stringListMap.get("author");
        List<String> links = stringListMap.get("link");
        List<String> titles = stringListMap.get("title");
        List<String> niceDates = stringListMap.get("niceDate");
        List<String> chapterNames = stringListMap.get("chapterName");
        List<String> ids = stringListMap.get("originId");
        ArrayList<Article> articles = new ArrayList<>();
        if (authors == null) return null;
        for (int i = 0; i < authors.size() ; i++) {
            articles.add(new Article(authors.get(i),links.get(i),titles.get(i),niceDates.get(i),chapterNames.get(i),Integer.parseInt(ids.get(i)),true));
        }
        return articles;
    }





}
