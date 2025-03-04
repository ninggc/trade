package com.ninggc.trade.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ninggc.trade.DAO.Commodity;
import com.ninggc.trade.R;
import com.ninggc.trade.activity.c_d_activity.CommodityListFragment;
import com.ninggc.trade.adapter.CommodityRecyclerViewAdapter;
import com.ninggc.trade.adapter.MyFragmentPagerAdapter;
import com.ninggc.trade.util.http.HttpResponseListener;
import com.ninggc.trade.util.tool.IGson;
import com.ninggc.trade.util.tool.ITAG;
import com.ninggc.trade.util.http.Server;
import com.ninggc.trade.util.constants.Constant;
import com.ninggc.trade.util.exception.NotSupportNowException;
import com.ninggc.trade.util.image.GlideImageLoader;
import com.ninggc.trade.view.MyViewPager;
import com.yanzhenjie.nohttp.rest.Response;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ning on 12/10/2017 0010.
 */

public class CampusMarketFragment extends Fragment implements ITAG, IGson {
    View view;
    Banner banner;
//    RecyclerView recyclerView;
//    CommodityRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_notice;
    TabLayout tabLayout;
    MyViewPager viewPager;
    CommodityRecyclerViewAdapter recyclerViewAdapter;
    MyFragmentPagerAdapter pagerAdapter;

    ///或许可以只在adapter保存一个集合的引用就可以，将会考虑删掉这个
    @Deprecated
    List<Commodity> commodities = new ArrayList<>(10);
    List<String> titles;
    List<Fragment> fragments;
    List<Commodity> commodityList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_campus_market, container, false);

        initView();
        initViewPager();
        initList();

        return view;
    }

    private void initView() {
        banner = (Banner) view.findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(Arrays.asList(Constant.image1, Constant.image2, Constant.image3));
        banner.start();
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncList();
            }
        });
        tv_notice = (TextView) view.findViewById(R.id.campus_market_notice);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (MyViewPager) view.findViewById(R.id.view_pager);
    }

    void initViewPager() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        commodityList = new ArrayList<>();
        recyclerViewAdapter = new CommodityRecyclerViewAdapter(getContext());
        Collections.addAll(titles, getResources().getStringArray(R.array.title_book));
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setTag(titles.get(i)));
            fragments.add(CommodityListFragment.newInstance(recyclerViewAdapter));
        }
        pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setOffscreenPageLimit(titles.size());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                syncRecyclerView(null);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //初始化集合数组
        //第一个集合设为页面最开始显示的列表
        syncRecyclerView(null);
    }

    private void initList() {
        syncList();
    }

    private void syncList() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        int id = 1;
        try {
            Server.showCommodityListWithCampus(id, new HttpResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    super.onSucceed(what, response);
//                    String s = response.get();
//                    Log.e(TAG_NOHTTP + TAG_INFO, "onSucceed: response.get: " + s);
//                    try {
//                        List<Commodity> list = gson.fromJson(s, new TypeToken<List<Commodity>>(){}.getType());
//                        Log.e(TAG_INFO, "onSucceed: gson解析后: " + gson.toJson(list));
//                        syncRecyclerView(list);
//                    } catch (JsonSyntaxException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }

                    // FIXME: 12/27/2017 0027 添加了十条测试数据，每次刷新都会添加
                    for (int i = 0; i < 10; i++) {
                        commodityList.add(Commodity.getTestInstance());
                    }
                    syncRecyclerView(null);
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    super.onFailed(what, response);
                    Log.e(TAG_NOHTTP, "onFailed: " + response.get());
                }

                @Override
                public void onFinish(int what) {
                    super.onFinish(what);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (NotSupportNowException e) {
            e.printStackTrace();
        }
    }

    private void syncRecyclerView(List<Commodity> list) {
        Set<Commodity> set = new LinkedHashSet<>(commodityList.size() * 2);
        if (list != null) {
            set.addAll(commodityList);
            set.addAll(list);
            commodityList = new ArrayList<>(set);
        }

        int[] value = getResources().getIntArray(R.array.sort_value_book);
        int sort = value[tabLayout.getSelectedTabPosition()];

        Collection<Commodity> collection = Collections2.filter(commodityList, new Predicate<Commodity>() {
            @Override
            public boolean apply(Commodity input) {
                return sort == input.getSort();
            }
        });
        recyclerViewAdapter.changeList(new ArrayList<Commodity>(collection));
    }
}
