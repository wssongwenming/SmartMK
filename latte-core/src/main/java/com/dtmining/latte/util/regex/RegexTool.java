package com.dtmining.latte.util.regex;

import android.text.TextUtils;

/**
 * author:songwenming
 * Date:2018/10/13
 * Description:
 */
public final class RegexTool {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }


    public static boolean isCode(String code){
        String codeRegex="^[0-9]{11,15}$";
        if (TextUtils.isEmpty(code)) return false;
        else return code.matches(codeRegex);
    }

    //正则密码
    public static boolean ispassword(String pwd) {

        String telRegex = "^[a-zA-Z0-9]{6,16}$";
        System.out.println(pwd.matches(telRegex));
        if (TextUtils.isEmpty(pwd)) return false;
        else return pwd.matches(telRegex);
    }


}
