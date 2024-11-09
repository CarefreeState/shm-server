package com.macaron.homeschool.common.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-09
 * Time: 0:35
 */
@Slf4j
public class HttpServletUtil {

    public static String encodeString(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public static void warn(HttpServletResponse response, String warn) {
        log.warn(warn);
        response.setHeader("warn", encodeString(warn));
    }

    public static void info(HttpServletResponse response, String info) {
        log.info(info);
        response.setHeader("info", encodeString(info));
    }

    public static void error(HttpServletResponse response, String error) {
        log.error(error);
        response.setHeader("error", encodeString(error));
    }

}
