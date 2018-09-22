package com.dtmining.latte.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;

import com.dtmining.latte.R;
import com.dtmining.latte.delegates.LatteDelegate;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * author:songwenming
 * Date:2018/9/21
 * Description:该Activity仅仅是作为容器
 */
public abstract class ProxyActivity extends SupportActivity{
    //用来返回根delegate
    public abstract LatteDelegate setRootDelegate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }
    private void initContainer(@Nullable Bundle savedInstanceState){
        //一般容纳fragment的容器是framlayout
        final ContentFrameLayout container=new ContentFrameLayout(this);
        container.setId(R.id.delegate_container);
        setContentView(container);
        //如果是第一次加载的话
        if(savedInstanceState==null){
            //加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
            loadRootFragment(R.id.delegate_container,setRootDelegate());
        }
    }
    //单activity架构，如果activity退出了应用也就结束了
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }
}
