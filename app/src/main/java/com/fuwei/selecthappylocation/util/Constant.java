package com.fuwei.selecthappylocation.util;

/**
 * Created by collin on 2015-10-02.
 */
public class Constant {
    // 主地址
    private static final String BASE_URL = "http://fu.lin3615.net/api/index/";
    // 登录接口
    private static final String LOGIN = "login";
    // 生辰八字选号接口
    private static final String SELECT_NUMBER_BY_BIRTHDAY = "byBirthday";
    // 随机选号提交
    private static final String SUBMIT_NUMBER_BY_RANDOM = "randomSubmit";
    // 我的选号接口
    private static final String MY_NUMBER = "xxxx";
    // 随机选号接口
    private static final String MY_RANDOM_SELECTION = "byRandDo";

    public static String getLoginUrl() {
        return BASE_URL + LOGIN;
    }

    public static String getSelectNumberByBirthdayUrl() {
        return BASE_URL + SELECT_NUMBER_BY_BIRTHDAY;
    }

    public static String getSubmitNumberByRandomUrl() {
        return BASE_URL + SUBMIT_NUMBER_BY_RANDOM;
    }

    public static String getMyNumberUrl() {
        return BASE_URL + MY_NUMBER;
    }

    public static String getRandomNumber() {
        return BASE_URL + MY_RANDOM_SELECTION;
    }
}
