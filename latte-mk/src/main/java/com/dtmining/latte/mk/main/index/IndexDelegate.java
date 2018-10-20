package com.dtmining.latte.mk.main.index;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.dtmining.latte.delegates.bottom.BottomItemDelegate;
import com.dtmining.latte.mk.R;
import com.dtmining.latte.mk.R2;
import com.dtmining.latte.mk.main.MkBottomDelegate;
import com.dtmining.latte.ui.recycler.BaseDecoration;
import com.dtmining.latte.ui.recycler.SimpleDividerItemDecoration;
import com.dtmining.latte.ui.refresh.RefreshHandler;
import com.joanzapata.iconify.widget.IconTextView;
import butterknife.BindView;

/**
 * author:songwenming
 * Date:2018/10/8
 * Description:
 */
public class IndexDelegate extends BottomItemDelegate {
    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView=null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout=null;
 /*   @BindView(R2.id.tb_index)
    Toolbar mToolbar=null;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan=null;
    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchView=null;*/
    private RefreshHandler mRefreshHandler=null;
    private void initRefreshLayout(){
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_red_light
        );
        //
        mRefreshLayout.setProgressViewOffset(true,120,300);
    }
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
       /* mRecyclerView.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),1));*/
        mRefreshHandler=RefreshHandler.create(mRefreshLayout,mRecyclerView,new IndexDataConverter(),this.getParentDelegate());
        //final MkBottomDelegate mkBottomDelegate=getParentDelegate();
        //单击跳转，显示每个项目的详情
        //mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(mkBottomDelegate));
    }

    private void initRecyclerView(){
        final GridLayoutManager manager=new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.blue_btn_bg_color),1));
        //final EcBottomDelegate ecBottomDelegate=getParentDelegate();
        //单击跳转，显示每个项目的详情
        //mRecylerView.addOnItemTouchListener(IndexItemClickListener.create(ecBottomDelegate));
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        mRefreshHandler.firstPage("index");
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }


}
