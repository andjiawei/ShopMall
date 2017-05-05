package com.shopmall.jiawei.shopmall.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shopmall.jiawei.shopmall.Contants;
import com.shopmall.jiawei.shopmall.R;
import com.shopmall.jiawei.shopmall.WareListActivity;
import com.shopmall.jiawei.shopmall.adapter.CardViewtemDecortion;
import com.shopmall.jiawei.shopmall.adapter.HomeCatgoryAdapter;
import com.shopmall.jiawei.shopmall.bean.BannerModel;
import com.shopmall.jiawei.shopmall.bean.Campaign;
import com.shopmall.jiawei.shopmall.bean.HomeCampaign;
import com.shopmall.jiawei.shopmall.http.BaseCallback;
import com.shopmall.jiawei.shopmall.http.OkHttpHelper;
import com.shopmall.jiawei.shopmall.http.SpotsCallBack;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 主页
 */

public class HomeFragment extends BaseFragment {


    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";
    private List<BannerModel> mBanners;
    private Banner mBanner;
    private HomeCatgoryAdapter mAdatper;
    private RecyclerView mRecyclerView;

    private void initData() {
        //banner
        httpHelper.get(url, new SpotsCallBack<List<BannerModel>>(getActivity()) {

            @Override
            public void onSuccess(Response response, List<BannerModel> banners) {
                mBanners=banners;
                initBannersData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
        //recyclerView
        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initRecyclerData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    private void initRecyclerData(List<HomeCampaign> homeCampaigns) {
        mAdatper = new HomeCatgoryAdapter(homeCampaigns,getActivity());
        mAdatper.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    private void initBannersData() {
        List<String> imageUrls=new ArrayList<>();
        for (BannerModel model: mBanners){
            imageUrls.add(model.getImgUrl());
        }
        mBanner.setImages(imageUrls);
        mBanner.start();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        return  inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBanner = (Banner) view.findViewById(R.id.banner);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mBanner.setImageLoader(new GlideImageLoader());
    }

    class GlideImageLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
