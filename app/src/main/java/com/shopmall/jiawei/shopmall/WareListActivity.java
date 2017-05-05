package com.shopmall.jiawei.shopmall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shopmall.jiawei.shopmall.adapter.BaseAdapter;
import com.shopmall.jiawei.shopmall.adapter.DividerItemDecoration;
import com.shopmall.jiawei.shopmall.adapter.HWAdatper;
import com.shopmall.jiawei.shopmall.bean.Page;
import com.shopmall.jiawei.shopmall.bean.Wares;
import com.shopmall.jiawei.shopmall.utils.Pager;
import com.shopmall.jiawei.shopmall.weight.MyToolBar;

import java.util.List;

public class WareListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares>, TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "WareListActivity";

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;

    private TabLayout mTablayout;
    private TextView mTxtSummary;
    private MyToolBar mToolbar;

    private int orderBy = 0;
    private long campaignId = 0;
    private XRecyclerView xrecyclerview;
    private Pager pager;
    private HWAdatper mWaresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_warelist);
        initView();
        initToolBar();
        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID, 0);
        initTab();
        getData();
    }

    private void initView() {
        mToolbar = (MyToolBar) findViewById(R.id.toolbar);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        xrecyclerview = (XRecyclerView) findViewById(R.id.xrecyclerview);
        mTxtSummary = (TextView) findViewById(R.id.txt_summary);
    }

    private void initToolBar() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.setRightButtonOnClickListener(this);
    }

    private void getData() {

        //让pager管理刷新和数据逻辑 最后回调过来
        pager = Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(xrecyclerview)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());

        pager.request();
    }


    private void initTab() {

        TabLayout.Tab tab = mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTablayout.addTab(tab);
        tab = mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTablayout.addTab(tab);
        mTablayout.addOnTabSelectedListener(this);
    }


    //OnPageListener 回调
    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {

        mTxtSummary.setText("共有" + totalCount + "件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HWAdatper(this, datas);
            mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                    Wares wares = mWaresAdapter.getItem(position);
//                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);
//                    intent.putExtra(Contants.WARE, wares);
//                    startActivity(intent);
                }
            });
            xrecyclerview.setAdapter(mWaresAdapter);
            xrecyclerview.setLayoutManager(new LinearLayoutManager(this));
            xrecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            xrecyclerview.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {

        mWaresAdapter.refreshData(datas);
        xrecyclerview.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {

        int action = (int) v.getTag();
        if (ACTION_LIST == action) {
            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            mToolbar.getRightButton().setTag(ACTION_GIRD);
            mWaresAdapter.resetLayout(R.layout.template_grid_wares);
            xrecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
            xrecyclerview.setAdapter(mWaresAdapter);

        } else if (ACTION_GIRD == action) {

            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            mToolbar.getRightButton().setTag(ACTION_LIST);
            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
            xrecyclerview.setLayoutManager(new LinearLayoutManager(this));
            xrecyclerview.setAdapter(mWaresAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTablayout.removeOnTabSelectedListener(this);
    }
}
