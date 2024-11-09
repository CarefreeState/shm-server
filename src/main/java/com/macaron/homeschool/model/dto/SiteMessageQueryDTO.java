package com.macaron.homeschool.model.dto;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 23:20
 */
@Data
public class SiteMessageQueryDTO {

    private Integer current;

    private Integer pageSize;

    private Long classId;

    private Long oppositeId; // 消息的对方

    private Boolean isFromMe; // 是否发送给我 null 代表全部， false 代表我收到的，true 代表我发送的

}
