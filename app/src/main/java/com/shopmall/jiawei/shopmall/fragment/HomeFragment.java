package com.shopmall.jiawei.shopmall.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shopmall.jiawei.shopmall.R;
import com.shopmall.jiawei.shopmall.bean.BannerModel;
import com.shopmall.jiawei.shopmall.http.OkHttpHelper;
import com.shopmall.jiawei.shopmall.http.SpotsCallBack;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * 主页
 */

public class HomeFragment extends BaseFragment {


    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";
    private List<BannerModel> mBanners;
    private Banner mBanner;

    private void initData() {
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
        mBanner.setImageLoader(new GlideImageLoader());
    }

    class GlideImageLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
