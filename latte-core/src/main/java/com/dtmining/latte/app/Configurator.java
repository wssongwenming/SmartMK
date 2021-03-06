package com.dtmining.latte.app;

import android.app.Activity;
import android.os.Handler;

import com.blankj.utilcode.util.Utils;
import com.dtmining.latte.delegates.LatteDelegate;
import com.dtmining.latte.util.handler.MyHandler;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * author:songwenming
 * Date:2018/9/20 0020
 * Description:管理各种配置项目，配置文件的存储和获取
 *
 */
public class Configurator {
    private static final HashMap<Object,Object> LATTE_CONFIGS=new HashMap<>();
    private static final ArrayList<IconFontDescriptor> ICONS= new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS=new ArrayList<>();
    private static final Handler HANDLER=new Handler();
    private static final MyHandler MYHANDLER=new MyHandler();


    private Configurator() {
        LATTE_CONFIGS.put(ConfigKeys.CONFIG_READY,false);
        LATTE_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
        LATTE_CONFIGS.put(ConfigKeys.MYHANDLER,MYHANDLER);
    }

    //静态内部类实现单例模式
    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }
    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }

    final HashMap<Object,Object> getLatteConfigs(){
        return LATTE_CONFIGS;
    }

    public final void configure(){
        initIcons();
        LATTE_CONFIGS.put(ConfigKeys.CONFIG_READY,true);
        Utils.init(Latte.getApplicationContext());

    }

    public final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigKeys.API_HOST,host);
        return this;
    }

    private void initIcons(){
        if(ICONS.size()>0){
            //with(params)params=The IconDescriptor holding the ttf file reference and its mappings
            final Iconify.IconifyInitializer initializer=Iconify.with(ICONS.get(0));
            for (int i = 1; i <ICONS.size() ; i++) {
                initializer.with(ICONS.get(i));

            }
        }
    }
    private void checkConfiguration(){
        final boolean isReady=(boolean)LATTE_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if(!isReady){
            throw new RuntimeException("Configuration is not ready ,call configure");
        }
    }

    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }
    //由于配置文件存储的是Object，所以用泛型返回，免去了Object类型转换
 /*   final <T> T getConfiguration(Enum<ConfigKeys> key){
        checkConfiguration();
        return (T)LATTE_CONFIGS.get(key);
    }
*/
    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    private final Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;

    }

    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = LATTE_CONFIGS.get(key);
        if (value == null) {
            //不能随便抛出异常，这样会导致APP
            // throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) LATTE_CONFIGS.get(key);
    }


    public final Configurator withWeChatAppId(String appId)
    {
        LATTE_CONFIGS.put(ConfigKeys.WE_CHAT_APP_ID,appId);
        return this;
    }
    public final Configurator withWeChatAppSecret(String appSecret)
    {
        LATTE_CONFIGS.put(ConfigKeys.WE_CHAT_APP_SECRET,appSecret);
        return this;
    }
    public final Configurator withQQAppID(String appID){
        LATTE_CONFIGS.put(ConfigKeys.QQ_APP_ID,appID);
        return this;
    }
    //微信拉取他的回调Activity时会需要一个Activity的上下文,这时用全局的congtext是不合适的
    public final Configurator withActivity(Activity activity)
    {
        LATTE_CONFIGS.put(ConfigKeys.ACTIVITY,activity);
        return this;
    }
    public final Configurator withDelegate(LatteDelegate delegate)
    {
        LATTE_CONFIGS.put(ConfigKeys.ABOUNTMEDELEGATE,delegate);
        return this;
    }
    public final Configurator withMedicineMineDelegate(LatteDelegate delegate)
    {
        LATTE_CONFIGS.put(ConfigKeys.MEDICINEMINEDELEGATE,delegate);
        return this;
    }
    public final <T> Configurator withLocalUser(T userProfile ){
        LATTE_CONFIGS.put(ConfigKeys.LOCAL_USER,userProfile);
        return this;
    }

}
