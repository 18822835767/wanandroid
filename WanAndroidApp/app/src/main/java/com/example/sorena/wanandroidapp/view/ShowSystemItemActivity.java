package com.example.sorena.wanandroidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.SystemArticleBaseAdapter;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.SystemBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

public class ShowSystemItemActivity extends AppCompatActivity {


    private SystemBarLayout mMySystemBar;
    private SwipeRefreshLayout mSystemSwipeRefreshLayoutRefresh;
    private ListView mSystemListViewShowItem;
    private Integer mMaxPage = 0;
    private Integer mNextPage = 0;
    private FlowItem mFlowItem;
    private SystemArticleBaseAdapter mSystemArticleBaseAdapter;
    private TextView mSystemTextViewNoMessage;
    private TextView mMySystemBarTextViewMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_system_item);
        ViewUtil.cancelActionBar(this);
        Intent intent = getIntent();
        mFlowItem = (FlowItem) (intent.getSerializableExtra("data"));
        Log.d("日志:data", mFlowItem.toString());
        initView();
        initListData();
    }



    private void initView(){

        mMySystemBar = findViewById(R.id.mySystemBar);
        mSystemSwipeRefreshLayoutRefresh = findViewById(R.id.collect_SwipeRefreshLayout_refresh);
        mSystemListViewShowItem = findViewById(R.id.collect_listView_showItem);
        mSystemTextViewNoMessage = findViewById(R.id.collect_textView_noMessage);
        mMySystemBarTextViewMessage = findViewById(R.id.mySystemBar_textView_message);
        mMySystemBarTextViewMessage.setText(mFlowItem.getName());
        mSystemListViewShowItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mSystemListViewShowItem.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {

                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mSystemListViewShowItem.getChildAt(mSystemListViewShowItem.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mSystemListViewShowItem.getHeight()) {                   loadNextPageData();
                    }
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        mSystemSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                        if (mSystemSwipeRefreshLayoutRefresh.isRefreshing()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShowSystemItemActivity.this,"网络木有相应",Toast.LENGTH_SHORT).show();
                                    mSystemSwipeRefreshLayoutRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        mSystemListViewShowItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                if (o instanceof Article){
                    Article article = (Article)o;
                    Intent intent = new Intent(ShowSystemItemActivity.this,WebActivity.class);
                    intent.putExtra("url",article.getLink());
                    startActivity(intent);
                }
            }
        });



    }





    private void loadNextPageData(){


        String url = "https://www.wanandroid.com/article/list/"+mNextPage+"/json?cid=" +mFlowItem.getId();
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
                if (mNextPage > mMaxPage){
                    LogUtil.d("日志:","一页数据都没有");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSystemTextViewNoMessage.setVisibility(View.VISIBLE);
                        }
                    });
                    return;
                }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mSystemArticleBaseAdapter == null){
                            mSystemArticleBaseAdapter = new SystemArticleBaseAdapter(ShowSystemItemActivity.this,R.layout.article_item_layout,articles,null);
                            mSystemListViewShowItem.setAdapter(mSystemArticleBaseAdapter);
                        }else {
                            mSystemArticleBaseAdapter.addData(articles);
                            mSystemSwipeRefreshLayoutRefresh.setRefreshing(false);
                        }
                    }
                });
                mNextPage++;

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }



    private void initListData() {
        loadNextPageData();
    }
}
