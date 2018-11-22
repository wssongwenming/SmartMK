package com.dtmining.latte.mk.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dtmining.latte.app.Latte;
import com.dtmining.latte.delegates.LatteDelegate;
import com.dtmining.latte.mk.ui.recycler.DataConverter;
import com.dtmining.latte.mk.ui.recycler.MultipleRecyclerAdapter;
import com.dtmining.latte.mk.ui.sub_delegates.medicine_take_plan.MedicinePlanExpandableListViewAdapter;
import com.dtmining.latte.mk.ui.sub_delegates.views.SwipeListLayout;
import com.dtmining.latte.mk.ui.sub_delegates.medicine_mine.MedicineMineRecyclerAdapter;
import com.dtmining.latte.net.RestClient;
import com.dtmining.latte.net.callback.ISuccess;
import com.dtmining.latte.util.log.LatteLogger;

import java.util.HashSet;
import java.util.Set;


public class RefreshHandler implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener{
    private final SwipeRefreshLayout REFESH_LAYOUT;
    private Set<SwipeListLayout> SETS =null;
    private final PagingBean BEAN;
    private Set<SwipeListLayout> sets = new HashSet();
    private final RecyclerView RECYCLERVIEW;
    private final ExpandableListView PLANEXPANDABLELISTVIEW;
    private  MultipleRecyclerAdapter mAdapter=null;
    private final DataConverter CONVERTER;
    private final LatteDelegate DELEGATE;
    public RefreshHandler(SwipeRefreshLayout swipeRefreshLayout,RecyclerView recyclerView,ExpandableListView expandableListView,
                          DataConverter converter,PagingBean pagingBean,LatteDelegate delegate,Set<SwipeListLayout> sets){
        this.REFESH_LAYOUT=swipeRefreshLayout;
        this.RECYCLERVIEW=recyclerView;
        this.PLANEXPANDABLELISTVIEW=expandableListView;
        this.CONVERTER=converter;
        this.BEAN=pagingBean;
        this.DELEGATE=delegate;
        this.SETS=sets;
        if(REFESH_LAYOUT!=null) {
            REFESH_LAYOUT.setOnRefreshListener(this);
        }
    }

    public static RefreshHandler create(SwipeRefreshLayout swipeRefreshLayout,RecyclerView recyclerViewHistoy,ExpandableListView expandableListView,
                                        DataConverter converter,LatteDelegate delegate,Set<SwipeListLayout> sets){
        return new RefreshHandler(swipeRefreshLayout,recyclerViewHistoy,expandableListView,converter,new PagingBean(),delegate,sets);
    }

    private void refresh(){
        if(REFESH_LAYOUT!=null)
        REFESH_LAYOUT.setRefreshing(true);
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //可以进行网络请求，REFESH_LAYOUT.setRefreshing(false);可以放入网络请求的success回调
                REFESH_LAYOUT.setRefreshing(false);
            }
        },2000);
    }


    public void get_medicine_plan(String url,String tel){

        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        MedicinePlanExpandableListViewAdapter medicinePlanExpandableListViewAdapter=new MedicinePlanExpandableListViewAdapter(response,sets,DELEGATE);
                        MedicinePlanExpandableListViewAdapter.convert(response);
                        PLANEXPANDABLELISTVIEW.setAdapter(medicinePlanExpandableListViewAdapter);
                    }
                })
                .build()
                .post();
    }
/*
    public void getHistoryMore(){
        mAdapter=MultipleRecyclerAdapter.getHistoryMore(CONVERTER,DELEGATE);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mAdapter.loadMoreEnd(true);
            }
        }, TOPRECYCLERVIEW);
        TOPRECYCLERVIEW.setAdapter(mAdapter);
    }
*/
    public void firstPage_medicine_history(String url,String tel){
        //BEAN.setDelayed(1000);
        BEAN.setPageIndex(0);
        BEAN.setPageSize(5);
        RestClient.builder()
                .url(url)
                .params("tel",tel)
                .params("page",BEAN.getPageIndex())
                .params("count",BEAN.getPageSize())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        // final JSONObject object=JSON.parseObject(response);
                        /*BEAN.setTotal(object.getInteger("total"))
                                .setPageSize(object.getInteger("page_size"));*/
                        //设置Adapter
                        mAdapter=MultipleRecyclerAdapter.getMedicineHistory(CONVERTER.setJsonData(response),DELEGATE);
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this,RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);

                        BEAN.addIndex();
                    }
                })
                .build()
                .get();
    }
    public void get_medicine_history(String url,String tel,String boxId)
    {
        BEAN.setPageIndex(0);
        BEAN.setPageSize(20);
        RestClient.builder()
                .url(url)
                .params("tel",tel)
                .params("boxId",boxId)
                .params("page",BEAN.getPageIndex())
                .params("count",BEAN.getPageSize())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        // final JSONObject object=JSON.parseObject(response);
                        /*BEAN.setTotal(object.getInteger("total"))
                                .setPageSize(object.getInteger("page_size"));*/
                        //设置Adapter
                        mAdapter=MultipleRecyclerAdapter.getMedicineHistoryForDetail(CONVERTER.setJsonData(response),DELEGATE);
                        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {

                            }
                        }, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);

                        BEAN.addIndex();
                    }
                })
                .build()
                .get();
    }
/*    public void firstPage_medicine_plan(String url){
        //BEAN.setDelayed(1000);
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(Latte.getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        //final JSONObject object= JSON.parseObject(response);
                       // BEAN.setTotal(object.getInteger("total"))
                       //         .setPageSize(object.getInteger("page_size"));
                        //设置Adapter
                        MultipleRecyclerAdapter mAdapter=MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response),DELEGATE);
                         mAdapter.setOnLoadMoreListener(RefreshHandler.this,RECYCLERVIEW);
                         RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .get();

    }*/

    public void firstPage_medicine_mine(String url){
        BEAN.setDelayed(1000);
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        MedicineMineRecyclerAdapter mAdapter= MedicineMineRecyclerAdapter.create(CONVERTER.setJsonData(response),SETS);
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this,RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .get();

    }
    @Override
    public void onRefresh() {
        refresh();

    }

    @Override
    public void onLoadMoreRequested() {
        paging("refresh.php?index=");
    }

    private void paging(final String url) {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getData().size() < pageSize || currentCount >= total) {
            mAdapter.loadMoreEnd(true);
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RestClient.builder()
                            .url(url + index)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    CONVERTER.clearData();
                                    mAdapter.addData(CONVERTER.setJsonData(response).convert());
                                    //累加数量
                                    BEAN.setCurrentCount(mAdapter.getData().size());
                                    mAdapter.loadMoreComplete();
                                    BEAN.addIndex();
                                }
                            })
                            .build()
                            .get();
                }
            }, 1000);
        }
    }
    private void paging_medicine_history(){

    }
}
