package com.dtmining.latte.net;

import android.content.Context;

import com.dtmining.latte.net.callback.IError;
import com.dtmining.latte.net.callback.IFailure;
import com.dtmining.latte.net.callback.IRequest;
import com.dtmining.latte.net.callback.ISuccess;
import com.dtmining.latte.ui.LoaderStyle;

import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * author:songwenming
 * Date:2018/9/23
 * Description:用建造者模式实现RestClient，这里把建造者从它的宿主类RestClient中分离出来
 */
public class RestClientBuilder {
    private  String mUrl=null;
    private  static final Map<String,Object> PARAMS=RestCreator.getParams();
    private  IRequest mIRequest=null;
    private  ISuccess mISuccess=null;
    private  IFailure mIFailure;
    private  IError mIError;
    private  RequestBody mBody;
    private LoaderStyle mLoaderStyle;
    private Context mContext;

    RestClientBuilder(){}

    public final RestClientBuilder url(String url){
        this.mUrl=url;
        return this;
    }
    public final RestClientBuilder params(WeakHashMap<String,Object>params){
        this.PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key,Object value){

        this.PARAMS.put(key,value);
        return this;
    }
    public final RestClientBuilder raw(String raw){
        mBody=RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess=iSuccess;
        return this;
    }
    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure=iFailure;
        return this;
    }
    public final RestClientBuilder error(IError iError){
        this.mIError=iError;
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest){
        this.mIRequest=iRequest;
        return this;
    }

    public final RestClientBuilder loader(Context context,LoaderStyle style){
        this.mContext=context;
        this.mLoaderStyle=style;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContext=context;
        this.mLoaderStyle=LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }
    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,mIRequest,mISuccess,mIFailure,mIError,mBody,mLoaderStyle,mContext);
    }

}
