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
import com.example.sorena.wanandroidapp.adapter.SearchResultListAdapter;
import com.example.sorena.wanandroidapp.bean.SearchResult;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JudgeUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.SystemBarLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.delHTMLTag;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

public class ShowResultActivity extends AppCompatActivity {

    private SystemBarLayout showResultActivitySystemBarLayoutTopBar;
    private SwipeRefreshLayout showResultActivitySwipeRefreshLayoutRefresh;
    private ListView showResultActivityListViewShowResult;
    private SearchResultListAdapter resultListAdapter;
    private TextView mMessageTextView;
    private List<SearchResult> results;
    private String data;
    private int nextPage = 0;
    private int maxPage = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        ViewUtil.cancelActionBar(this);
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        LogUtil.d("日志:","搜索关键词为:" + data);
        initView();
        initData();

    }




    private void initView(){
        showResultActivitySystemBarLayoutTopBar = (SystemBarLayout) findViewById(R.id.showResultActivity_SystemBarLayout_topBar);
        showResultActivitySwipeRefreshLayoutRefresh = (SwipeRefreshLayout) findViewById(R.id.showResultActivity_SwipeRefreshLayout_refresh);
        showResultActivityListViewShowResult = (ListView) findViewById(R.id.showResultActivity_listView_showResult);
        mMessageTextView = findViewById(R.id.mySystemBar_textView_message);

        mMessageTextView.setText(data);
        showResultActivityListViewShowResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //到顶部时，设置可以刷新
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = showResultActivityListViewShowResult.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {

                    }
                }
                //到底部时,自动加载下一页
                else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = showResultActivityListViewShowResult.getChildAt(showResultActivityListViewShowResult.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == showResultActivityListViewShowResult.getHeight()) {
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        showResultActivitySwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                new Thread(()->{
                    try {Thread.sleep(5000);}
                    catch (Exception e){ LogUtil.e("日志:","异常:" + e.getMessage()); }
                    if (showResultActivitySwipeRefreshLayoutRefresh.isRefreshing()){
                        showResultActivitySwipeRefreshLayoutRefresh.setRefreshing(false);
                        runOnUiThread(()->{Toast.makeText(ShowResultActivity.this,"哦豁,可能木有网络了哦",Toast.LENGTH_SHORT).show();});
                    }
                }).start();
            }
        });
        showResultActivityListViewShowResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult result = (SearchResult)(resultListAdapter.getItem(position));
                Intent intent = new Intent(ShowResultActivity.this,WebActivity.class);
                intent.putExtra("url",result.getLink());
                startActivity(intent);
            }
        });
    }

    private void refreshData(){
        if (resultListAdapter == null){
            initData();
            return;
        }
        nextPage = 0;
        resultListAdapter.clearData();
        loadMoreData();
    }




    private void initData(){

        User user = SharedPreferencesHelper.getUserData();
        HttpUtil.sendHttpPostRequestWithCookie("https://www.wanandroid.com/article/query/0/json",  new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()},new String[]{"k"}, new String[]{data}, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {

                LogUtil.d("日志","response:" + response);
                results = parseData(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultListAdapter = new SearchResultListAdapter(ShowResultActivity.this,R.layout.search_result_item,results);
                        showResultActivityListViewShowResult.setAdapter(resultListAdapter);
                        nextPage++;
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void loadMoreData(){

        if (nextPage > maxPage){
            return;
        }
        User user = SharedPreferencesHelper.getUserData();
        HttpUtil.sendHttpPostRequestWithCookie("https://www.wanandroid.com/article/query/"+nextPage+"/json", new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()}, new String[]{"k"}, new String[]{data}, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.d("日志","response:" + response);
                results = parseData(response);
                runOnUiThread(()->{
                    resultListAdapter.addData(results);
                    nextPage++;
                    showResultActivitySwipeRefreshLayoutRefresh.setRefreshing(false);
                });

            }
            @Override
            public void onError(Exception e) {}
        });
    }




    private List<SearchResult> parseData(String response){

        String data = getValue("data",response,new String[]{});
        String datas = getValue("datas",data,new String[]{});
        String pageCount = getValue("pageCount",data,new String[]{});
        maxPage = Integer.parseInt(pageCount) - 1;
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
