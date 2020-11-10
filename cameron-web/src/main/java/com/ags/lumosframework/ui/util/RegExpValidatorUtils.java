package com.ags.lumosframework.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpValidatorUtils {

    /**
     * @param regex
     *            正则表达式字符串
     * @param str
     *            要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证数字
     *
     * @param 待验证的字符串
     * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIsNumber(String str) {
        String regex = "^[0-9]+(.[0-9]*)?$";
        return match(regex, str);
    }

    /**
     * 验证正整数（>=0）
     *
     * @param 待验证的字符串
     * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIsPositive(String str) {
        String regex = "^[0-9]\\d*$";
        return match(regex, str);
    }
    /**
     * 验证正整数（>0）
     * 
     * **/
    public static boolean isPositive(String str) {
        String regex = "^[1-9]\\d*$";
        return match(regex, str);
    }
    
    /**
     * 验证温度数字   可正负
     *
     * @param 待验证的字符串
     * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
 	public static boolean isIsTemp(String str) {
 		String regex = "^(-?\\d+)(\\.\\d+)?$";
 		return str.matches(regex);// match(regex, str)
 	}
 	
 	/**
     * 验证温度数字   可正负 正负号为中文
     *
     * @param 待验证的字符串
     * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
 	public static boolean isIsTempOther(String str) {
 		String regex = "^([\\u6b63\\u8d1f]?\\d+)(\\.\\d+)?$";
 		return str.matches(regex);// match(regex, str)
 	}

 	 /****
     * 验证序列号
     * @param str
     * @return
     * 数字类型，但是可能是以0开头 01,11,...
     */
    public static boolean isMaterialSn(String str) {
        String regex = "^[0-9]\\d*$";
        return match(regex, str);
    }
    
    public static boolean isNumber(String str) {
    	 String regexFloat = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    	 String regexInteger = "^[0-9]\\d*$";
    	 return match(regexFloat, str) || match(regexInteger, str);
    }
}
