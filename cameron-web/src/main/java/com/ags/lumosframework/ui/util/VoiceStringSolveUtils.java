package com.ags.lumosframework.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 语音字符串中文处理
 */
public class VoiceStringSolveUtils {
//    public static void main(String[] args) {
//        String str="一贰叁杠4我20打算点:abC";
//        System.out.println(solveString(str));
//    }

    public static String solveString(String strVoice) {
        String strReturn;
        //****替换0-10汉字
        strVoice = strVoice.replaceAll("零", "0");
        strVoice = strVoice.replaceAll("铃", "0");
        strVoice = strVoice.replaceAll("玲", "0");

        strVoice = strVoice.replaceAll("一", "1");
        strVoice = strVoice.replaceAll("壹", "1");

        strVoice = strVoice.replaceAll("二", "2");
        strVoice = strVoice.replaceAll("贰", "2");
        strVoice = strVoice.replaceAll("爱", "2");

        strVoice = strVoice.replaceAll("三", "3");
        strVoice = strVoice.replaceAll("叁", "3");

        strVoice = strVoice.replaceAll("四", "4");
        strVoice = strVoice.replaceAll("肆", "4");

        strVoice = strVoice.replaceAll("五", "5");

        strVoice = strVoice.replaceAll("六", "6");

        strVoice = strVoice.replaceAll("七", "7");

        strVoice = strVoice.replaceAll("八", "8");
        strVoice = strVoice.replaceAll("吧", "8");

        strVoice = strVoice.replaceAll("九", "9");
        strVoice = strVoice.replaceAll("酒", "9");

        strVoice = strVoice.replaceAll("十", "10");
        //*****处理“.”“-”汉字
        strVoice = strVoice.replaceAll("点", ".");
        strVoice = strVoice.replaceAll(":", ".");
        strVoice = strVoice.replaceAll("杠", "-");

        //******去除空格,“，”逗号
        strVoice = strVoice.replaceAll(" ", "");
        strVoice = strVoice.replaceAll("，", "");
        strVoice = strVoice.replaceAll(",", "");
        //***去除其余汉字
        String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(strVoice);
        strReturn = mat.replaceAll("");
        return strReturn;
    }
}
