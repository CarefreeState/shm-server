package com.macaron.homeschool.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-01-21
 * Time: 16:11
 */
public class EncryptUtil {

    // 获取盐值
    public static String getSalt() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 加盐加密
    public static String encrypt(String salt, String password) {
        return md5(salt + password);
    }

    // 验证密码
    public static boolean confirm(String salt, String inputPassword, String encryptPassword) {
        return StringUtils.hasText(salt) &&
                StringUtils.hasText(inputPassword) &&
                StringUtils.hasText(encryptPassword) &&
                encryptPassword.equals(md5(salt + inputPassword));
    }

    // md5 加密
    public static String md5(String normal) {
        return DigestUtils.md5Hex(normal);
    }

    public static String sha1(String... strings) {
        StringBuilder builder = new StringBuilder();
        for(String s : strings) {
            builder.append(s);
        }
        // 加密
        return DigestUtils.sha1Hex(builder.toString());
    }
}
