package com.example.sorena.wanandroidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.HistoryAdapt;
import com.example.sorena.wanandroidapp.bean.History;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseOperator;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.FlowLayout;
import com.example.sorena.wanandroidapp.widget.FlowLayoutFactory;

import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

/**
 * 搜索的活动
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener ,TextView.OnEditorActionListener {

    private ImageView mImageViewBack;
    private EditText mEditTextInputText;
    private LinearLayout mLinearLayoutShowHot;
    private FlowLayout mFlowLayoutShowHot;
    private ListView mListViewShowHistorySearch;
    private TextView mTextViewClear;
    private ImageView mImageViewClear;


    private HistoryAdapt historyAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewUtil.cancelActionBar(this);
        initView();
        getHotData();
        getHistoryData();
    }

    void initView(){
        mImageViewBack = findViewById(R.id.searchActivity_imageView_back);
        mEditTextInputText = findViewById(R.id.searchActivity_editText_inputText);
        mLinearLayoutShowHot = findViewById(R.id.activity_linearLayout_showHot);
        mFlowLayoutShowHot = findViewById(R.id.searchActivity_flowLayout_showHot);
        mListViewShowHistorySearch = findViewById(R.id.searchActivity_listView_showHistorySearch);
        mTextViewClear = findViewById(R.id.search_textView_clear);
        mImageViewClear = findViewById(R.id.search_imageView_clear);
        mImageViewClear.setOnClickListener(this);
        mTextViewClear.setOnClickListener(this);
        mEditTextInputText.setOnEditorActionListener(this);
        mListViewShowHistorySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startResultActivity(historyAdapt.getmHistoryList().get(position).getWord());
            }
        });
        mImageViewBack.setOnClickListener(this);
    }

    void getHotData(){
        HttpUtil.sendHttpRequest("https://www.wanandroid.com//hotkey/json", new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                String data = getValue("data", response,new String[]{});
                Map<String,List<String>> dataMap = getMapInArray(data,new String[]{"name"});
                List<String> names = dataMap.get("name");
                if (names == null) return;
                String[] strings = new String[names.size()];
                for (int i = 0 ; i < names.size() ; i++){
                    strings[i] = names.get(i);
                }
                runOnUiThread(()->{
                    FlowLayoutFactory.setFlowLayout(mFlowLayoutShowHot, SearchActivity.this, R.layout.search_flowlayout_tv, names, strings, (v)->{
                        startResultActivity(v.getTag().toString());
                    }, true);
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getHistoryData(){
        SearchHistoryDataBaseOperator operator = SearchHistoryDataBaseOperator.getInstance();
        historyAdapt = new HistoryAdapt(this,R.layout.item_search_history,operator.getDataList());

        historyAdapt.setListener(new HistoryAdapt.OnItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                History history = historyAdapt.getmHistoryList().get(i);
                historyAdapt.getmHistoryList().remove(i);
                historyAdapt.notifyDataSetChanged();
                SearchHistoryDataBaseOperator.getInstance().delData(history.getWord());
            }
        });
        mListViewShowHistorySearch.setAdapter(historyAdapt);
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.searchActivity_imageView_back:
                finish();
                break;
            case R.id.search_imageView_clear:
            case R.id.search_textView_clear:
                SearchHistoryDataBaseOperator.getInstance().clearData();
                historyAdapt.clearData();
                break;
            default:
                break;

        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            if (v.getId() == R.id.searchActivity_editText_inputText && !mEditTextInputText.getText().toString().equals("")){
                historyAdapt.addData(mEditTextInputText.getText().toString());
                startResultActivity(mEditTextInputText.getText().toString());
            }
        }
        return false;
    }

    private void startResultActivity(String data){

        Intent intent = new Intent(this,ShowResultActivity.class);
        intent.putExtra("data",data);
        startActivity(intent);
    }

}
