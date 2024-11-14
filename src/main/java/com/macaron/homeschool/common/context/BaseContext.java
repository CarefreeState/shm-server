package com.macaron.homeschool.common.context;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */
public class BaseContext {

    private final static ThreadLocal<UserHelper> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(UserHelper userHelper ) {
        threadLocal.set(userHelper);
    }

    public static UserHelper getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }

}
