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
import com.example.sorena.wanandroidapp.adapter.CollectionAdapter;
import com.example.sorena.wanandroidapp.adapter.SystemArticleBaseAdapter;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.SystemBarLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ShowCollectActivity extends AppCompatActivity {

    private ListView mShowCollectActivityListViewShowCollects;
    private SwipeRefreshLayout mShowCollectActivitySwipeRefreshLayoutRefresh;
    private SystemBarLayout mShowCollectActivitySystemBarLayoutBar;
    private TextView nMessageTextView;
    private SystemArticleBaseAdapter mArticleAdapter;
    private List<Article> mArticle;
    private User user;
    private int nextPage = 0;
    private int maxPage = 0;



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
        mShowCollectActivitySwipeRefreshLayoutRefresh = findViewById(R.id.collect_SwipeRefreshLayout_refresh);
        mShowCollectActivitySystemBarLayoutBar = findViewById(R.id.mySystemBar);
        nMessageTextView.setText("收藏");
        user = SharedPreferencesHelper.getUserData();
        mShowCollectActivityListViewShowCollects = findViewById(R.id.collect_listView_showItem);
        mArticleAdapter = new SystemArticleBaseAdapter(this,R.layout.article_item_layout,new ArrayList<>(),new HashSet<>());
        mShowCollectActivityListViewShowCollects.setAdapter(mArticleAdapter);


        mShowCollectActivityListViewShowCollects.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mShowCollectActivityListViewShowCollects.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        mShowCollectActivitySwipeRefreshLayoutRefresh.setEnabled(true);
                    } else {
                        mShowCollectActivitySwipeRefreshLayoutRefresh.setEnabled(false);
                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mShowCollectActivityListViewShowCollects.getChildAt(mShowCollectActivityListViewShowCollects.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mShowCollectActivityListViewShowCollects.getHeight()) {
                        loadNextPageData();
                    }
                }
                //其他时候设置不可刷新
                else {
                    mShowCollectActivitySwipeRefreshLayoutRefresh.setEnabled(false);
                    LogUtil.d("日志:滚动中:","设置为不能上拉");
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        mShowCollectActivityListViewShowCollects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowCollectActivity.this,WebActivity.class);
                Article article = (Article)(mArticleAdapter.getItem(position));
                intent.putExtra("url",article.getLink());
                startActivity(intent);
            }
        });

        mShowCollectActivitySwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticleAdapter.clearData();
                nextPage = 0;
                loadNextPageData();
                new Thread(()->{
                    try {
                        Thread.sleep(5000);
                        if (mShowCollectActivitySwipeRefreshLayoutRefresh.isRefreshing()){
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
        if (nextPage > maxPage){
            return;
        }
        HttpUtil.sendHttpGetRequestWithCookie("https://www.wanandroid.com/lg/collect/list/"+nextPage+"/json",
                new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()},
                new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        nextPage++;
                        mArticle = parseData(response);
                        runOnUiThread(()->{
                            mArticleAdapter.addData(mArticle);
                            mArticleAdapter.notifyDataSetChanged();
                            if (mShowCollectActivitySwipeRefreshLayoutRefresh.isRefreshing()){
                                mShowCollectActivitySwipeRefreshLayoutRefresh.setRefreshing(false);
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
}
