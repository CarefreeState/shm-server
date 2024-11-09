package com.macaron.homeschool.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 0:41
 */
@Slf4j
public class PasswordUtil {

    private final static String PASSWORD_SEPARATOR = "$";

    private static String assemble(String salt, String password) {
        return salt + PASSWORD_SEPARATOR + EncryptUtil.encrypt(salt, password);
    }

    public static String encrypt(String password) {
        return assemble(EncryptUtil.getSalt(), password);
    }

    public static boolean confirm(String inputPassword, String dbPassword) {
        try {
            int separatorIndex = dbPassword.indexOf(PASSWORD_SEPARATOR);
            String salt = dbPassword.substring(0, separatorIndex);
            String encryptPassword = dbPassword.substring(separatorIndex + 1);
            boolean confirm = EncryptUtil.confirm(salt, inputPassword, encryptPassword);
            log.info("inputPassword {}, dbPassword {} (salt {}, encryptPassword {}) -> {}",
                    inputPassword, dbPassword, salt, encryptPassword, confirm);
            return confirm;
        } catch (Exception e) {
            log.info("inputPassword {}, dbPassword {} -> false", inputPassword, dbPassword);
            return Boolean.FALSE;
        }
    }
}
