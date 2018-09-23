package com.dtmining.latte.ui;

import android.content.Context;
import android.print.PrinterId;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.dtmining.latte.R;
import com.dtmining.latte.ui.util.dimen.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * author:songwenming
 * Date:2018/9/23
 * Description:
 */
public class LatteLoader {

    private static final int LOADER_SIZE_SCALE=8;
    //loader的偏移量为屏幕高度的10分之一
    private static final int LOADER_OFFSET_SCALE=10;

    private static final ArrayList<AppCompatDialog>LOADERS=new ArrayList<>();

    private static final String DEFAULT_LOADER=LoaderStyle.BallClipRotatePulseIndicator.name();

    public static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }

    public static void showLoading(Context context, String type){
        final AppCompatDialog dialog=new AppCompatDialog(context, R.style.dialog);

        final AVLoadingIndicatorView avLoadingIndicatorView=LoaderCreator.create(type,context);
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth= DimenUtil.getScreenWidth();
        int deviceHeight=DimenUtil.getScreenHeight();

        final Window dialogWindow=dialog.getWindow();

        if(dialogWindow!=null){
            WindowManager.LayoutParams lp=dialogWindow.getAttributes();
            lp.width=deviceWidth/LOADER_SIZE_SCALE;
            lp.height=deviceHeight/LOADER_SIZE_SCALE;
            lp.height=lp.height+deviceHeight/LOADER_OFFSET_SCALE;
            lp.gravity= Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }
    public static void stopLoading(){
        for(AppCompatDialog dialog:LOADERS){
            if(dialog!=null){
                if(dialog.isShowing()){
                    //cancel有个回调方法Oncancel
                    dialog.cancel();
                    //dialog.dismiss();
                }
            }
        }
    }
}
