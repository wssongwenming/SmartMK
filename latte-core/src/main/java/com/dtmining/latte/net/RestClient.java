package com.dtmining.latte.net;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;

import com.dtmining.latte.net.callback.IError;
import com.dtmining.latte.net.callback.IFailure;
import com.dtmining.latte.net.callback.IRequest;
import com.dtmining.latte.net.callback.ISuccess;
import com.dtmining.latte.net.callback.RequestCallbacks;
import com.dtmining.latte.ui.LatteLoader;
import com.dtmining.latte.ui.LoaderStyle;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * author:songwenming
 * Date:2018/9/22
 * Description:进行网络请求的具体实现类
 */
public class RestClient {
    //网络框架，要灵活使用什么样的框架呢？既然是传入参数也没有顺序的要求，传入什么用什么的模式的话？
    //建造者模式最好不过，可以用android简化版的建造者模式
    private final String URL;
    private static final WeakHashMap<String,Object> PARAMS=RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;

    private final LoaderStyle LOADER_STYLE;
    //loaderstyle为dialog，显示时须传入一个context
    private final Context CONTEXT;

    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      LoaderStyle loaderStyle,
                      Context context
                      ) {
        this.URL = url;
        this.PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.LOADER_STYLE=loaderStyle;
        this.CONTEXT=context;
    }
    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }
    private void request(HttpMethod method){
        final RestService service=RestCreator.getRestService();
        Call<String> call=null;
        //请求开始
        if(REQUEST!=null)
        {
            REQUEST.onRequestStart();
        }
        if(LOADER_STYLE!=null){
            LatteLoader.showLoading(CONTEXT,LOADER_STYLE);
        }
        switch (method){
            case GET:
                call=service.get(URL,PARAMS);
                break;
            case POST:
                call=service.post(URL,PARAMS);
                break;
            case PUT:
                call=service.put(URL,PARAMS);
                break;
            case DELETE:
                call=service.delete(URL,PARAMS);
                break;
            default:
                break;

        }
        if(call!=null){
            //enqueue(Callback<T> callback)
            call.enqueue(getRequestCallback());

        }
    }
    private Callback<String> getRequestCallback(){
        return new RequestCallbacks(REQUEST,SUCCESS,FAILURE,ERROR,LOADER_STYLE);
    }
    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        request(HttpMethod.POST);
    }
    public final void put(){
        request(HttpMethod.PUT);
    }
    public final void delete(){
        request(HttpMethod.DELETE);
    }

}