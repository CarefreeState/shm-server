package com.macaron.homeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <span>
 * <h3> global service status code </h3>
 * Please note that status code definitions are module-specific
 * and do not occupy other business modules when defining them.
 * </span>
 *
 */
@Getter
@AllArgsConstructor
public enum GlobalServiceStatusCode {

    /* 成功, 默认200 */
    SYSTEM_SUCCESS(200, "操作成功"),

    /* 系统错误 500 - 1000 */
    SYSTEM_SERVICE_FAIL(-4396, "操作失败"),
    SYSTEM_SERVICE_ERROR(-500, "系统异常"),
    SYSTEM_TIME_OUT(-1, "请求超时"),

    /* 参数错误：1001～2000 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    PARAM_FAILED_VALIDATE(1005, "参数未通过验证"),

    REQUEST_NOT_VALID(1101, "请求无效"),

    /* 用户错误 2001-3000 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_ACCOUNT_REGISTER_ERROR(2010, "账号注册错误"),

    USER_TYPE_EXCEPTION(2101, "用户类别异常"),

    USER_NO_PERMISSION(2403, "用户无权限"),
    USER_CAPTCHA_CODE_ERROR(2500, "验证码错误"),
    USER_USERNAME_PASSWORD_ERROR(2501, "用户名或密码错误"),

    /* 审核状态 3001-4000 */
    AUDIT_STATUS_EXCEPTION(3001, "审核状态异常"),
    AUDIT_STATUS_NOT_APPROVED(3002, "未通过审核，无法进行该操作"),
    AUDIT_STATUS_APPROVED(3002, "已通过审核，无法进行该操作"),

    /* 消息 4001-5000 */
    SYSTEM_MESSAGE_NOT_EXISTS(4001, "系统消息不存在"),
    CLASS_MESSAGE_NOT_EXISTS(4002, "班级通知不存在"),
    SITE_MESSAGE_NOT_EXISTS(4003, "站内信不存在"),

    /* 班级 5001-6000 */
    SCHOOL_CLASS_NOT_EXISTS(5001, "班级不存在"),
    SCHOOL_CLASS_USER_ATTENDED(5002, "用户已申请或已加入该班级"),
    USER_NOT_CLASS_CREATOR(5003, "用户不是班级的创建者"),
    USER_NOT_CLASS_PARTNER(5004, "用户不是班级的一员"),

    /* -------------- */;

    private final Integer code;
    private final String message;

    /**
     * 根据code获取message
     *
     * @param code 状态码的code
     * @return 状态码的状态信息
     */
    public static String getStatusMsgByCode(Integer code) {
        for (GlobalServiceStatusCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
