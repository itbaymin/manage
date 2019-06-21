package com.byc.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by baiyc
 * 2019/6/3/003 19:19
 * Description：html工具
 */
public class HTMLUtil {
    private static final String TITLE_REG = "(?<=(<title>)).*?(?=(</title>))";
    private static final String ICON_REG = "https?://[a-zA-Z\\.0-9]*";

    public static String getTitle(String html){
        return getRegRes(TITLE_REG,html);
    }

    private static String getRegRes(String reg,String source){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(source);
        String result = "";
        int i = 0;
        while (matcher.find()){
            result += matcher.group(i++);
        }
        return result;
    }

    public static String getIcon(String url){
        return getRegRes(ICON_REG,url)+"/favicon.ico";
    }

}
